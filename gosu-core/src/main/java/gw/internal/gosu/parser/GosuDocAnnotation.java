/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.Param;
import gw.lang.Throws;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuEscapeUtil;

import java.io.Serializable;

/**
 * Holds annotation information for a feature
 */
public class GosuDocAnnotation implements Serializable, IGosuAnnotation
{
  private IType _type;
  private String[] _args;
  private volatile Expression _expression;
  private ICompilableType _ownersType;

  public GosuDocAnnotation( ICompilableType ownersType, IType type, String... args )
  {
    _ownersType = ownersType;
    _type = type;
    _args = args;
  }

  public String getName()
  {
    return getType().getRelativeName();
  }

  public IType getType()
  {
    return _type;
  }

  @Override
  public String getNewExpressionAsString()
  {
    return "new " + _type.getName() + "(" + makeArgsForNewExpression() + ")";
  }

  @Override
  public Expression getExpression()
  {
    if( _expression == null )
    {
      TypeSystem.lock();
      try
      {
        if( _expression == null )
        {
          NewExpression newExpression = initLocation( new NewExpression() );
          newExpression.setType( _type );
          if( _type.getName().equals( Throws.class.getName() ) )
          {
            // throws starts with a type literal, then a string description
            newExpression.setConstructor( _type.getTypeInfo().getConstructor(JavaTypes.ITYPE(), JavaTypes.STRING() ) );
            newExpression.setArgs( exprArray( new TypeLiteral( TypeSystem.getByFullName( _args[0] ) ), new StringLiteral( _args[1] ) ) );
          }
          else if( _type.getName().equals( Param.class.getName() ) )
          {
            // throws starts with a type literal
            newExpression.setConstructor( _type.getTypeInfo().getConstructor( JavaTypes.STRING(), JavaTypes.STRING() ) );
            newExpression.setArgs( exprArray( new StringLiteral( _args[0] ), new StringLiteral( _args[1] ) ) );
          }
          else
          {
            // a vanilla annotation
            newExpression.setConstructor( _type.getTypeInfo().getConstructor( JavaTypes.STRING() ) );
            newExpression.setArgs( exprArray( new StringLiteral( _args[0] ) ) );
          }
          _expression = newExpression;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _expression;
  }

  @Override
  public void clearExpression() {
    _expression = null;
  }

  @Override
  public boolean shouldPersistToClass()
  {
    return false;
  }

  @Override
  public boolean shouldRetainAtRuntime() {
    return false;
  }

  @Override
  public ICompilableType getOwnersType()
  {
    return _ownersType;
  }

  private Expression[] exprArray( Expression... exprs )
  {
    for( Expression expr : exprs )
    {
      initLocation( expr );
    }
    return exprs;
  }

  private <T extends ParsedElement> T initLocation( T pe )
  {
    pe.setLocation( new ParseTree( pe, 0, 0, null ) );
    return pe;
  }

  private String makeArgsForNewExpression()
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < _args.length; i++ )
    {
      if( i != 0 )
      {
        sb.append( "," );
      }
      if( _type.getName().equals( Throws.class.getName() ) && i == 0 )
      {
        sb.append( _args[i] );
      }
      else
      {
        // make a string literal for everything else
        sb.append( "\"" ).append( GosuEscapeUtil.escapeForGosuStringLiteral( _args[i] ) ).append( "\"" );
      }
    }
    return sb.toString();
  }
}