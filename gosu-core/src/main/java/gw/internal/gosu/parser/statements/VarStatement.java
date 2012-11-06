/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.ModifierInfo;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;


import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.util.GosuObjectUtil;

import java.util.List;

/**
 * Represents a var statement as specified in the Gosu grammar:
 * <pre>
 * <i>var-statement</i>
 *   <b>var</b> &lt;identifier&gt; [scope-attribute] [ <b>:</b> &lt;type-expression&gt; ] <b>=</b> &lt;expression&gt;
 *   <b>var</b> &lt;identifier&gt; [scope-attribute] <b>:</b> &lt;type-expression&gt; [ <b>=</b> &lt;expression&gt; ]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class VarStatement extends Statement implements IVarStatement
{
  private CaseInsensitiveCharSequence _strPropertyName;
  protected Expression _expression;
  protected TypeLiteral _typeLiteral;
  protected boolean _hasProperty = false;
  protected GlobalScope _scope;
  protected ModifierInfo _modifiers;
  private ISymbol _symbol;
  private IScriptPartId _scriptPartId;
  private int _iNameOffset;
  private int _iPropertyNameOffset;
  private boolean _bDefinitionParsed;

  public VarStatement()
  {
  }

  public CaseInsensitiveCharSequence getIdentifierName()
  {
    return _symbol.getCaseInsensitiveName();
  }

  public ISymbol getSymbol()
  {
    return _symbol;
  }

  public void setSymbol( ISymbol symbol )
  {
    _symbol = symbol;
  }

  public CaseInsensitiveCharSequence getPropertyName()
  {
    return _strPropertyName;
  }
  public void setPropertyName( CaseInsensitiveCharSequence strPropertyName )
  {
    _strPropertyName = strPropertyName;
  }

  public GlobalScope getScope()
  {
    return _scope;
  }
  public void setScope( GlobalScope scope )
  {
    _scope = scope;
  }

  public TypeLiteral getTypeLiteral()
  {
    return _typeLiteral;
  }
  public void setTypeLiteral( TypeLiteral typeLiteral )
  {
    detachDeclTypeLiteral();
    _typeLiteral = typeLiteral;
  }

  private void detachDeclTypeLiteral()
  {
    if( _typeLiteral != null && getLocation() != null )
    {
      getLocation().removeChild( _typeLiteral.getLocation() );
    }
  }

  public Expression getAsExpression()
  {
    return _expression;
  }
  public void setAsExpression( Expression expression )
  {
    _expression = expression;
  }

  public void setType( IType newType )
  {
    _symbol.setType( newType );
  }

  public boolean hasProperty()
  {
    return _hasProperty;
  }

  public void setHasProperty( boolean hasProperty )
  {
    _hasProperty = hasProperty;
  }

  public ModifierInfo getModifierInfo()
  {
    return _modifiers;
  }
  public void setModifierInfo( ModifierInfo modifiers )
  {
    _modifiers = modifiers;
  }

  public int getModifiers()
  {
    return _modifiers.getModifiers();
  }

  public boolean isStatic()
  {
    return Modifier.isStatic( _modifiers.getModifiers() );
  }

  public void setStatic( boolean bStatic )
  {
    _modifiers.setModifiers( Modifier.setStatic( _modifiers.getModifiers(), bStatic ) );
  }

  public boolean isPrivate()
  {
    return Modifier.isPrivate( _modifiers.getModifiers() ) || (!isInternal() && !isProtected() && !isPublic());
  }

  public void setPrivate( boolean bPrivate )
  {
    _modifiers.setModifiers( Modifier.setPrivate( _modifiers.getModifiers(), bPrivate ) );
  }

  public boolean isInternal()
  {
    return Modifier.isInternal( _modifiers.getModifiers() );
  }

  public void setInternal( boolean bInternal )
  {
    _modifiers.setModifiers( Modifier.setInternal( _modifiers.getModifiers(), bInternal ) );
  }

  public boolean isProtected()
  {
    return Modifier.isProtected( _modifiers.getModifiers() );
  }

  public void setProtected( boolean bProtected )
  {
    _modifiers.setModifiers( Modifier.setProtected( _modifiers.getModifiers(), bProtected ) );
  }

  public boolean isPublic()
  {
    return Modifier.isPublic( _modifiers.getModifiers() );
  }

  public void setPublic( boolean bPublic )
  {
    _modifiers.setModifiers( Modifier.setPublic( _modifiers.getModifiers(), bPublic ) );
  }

  public boolean isFinal()
  {
    return Modifier.isFinal( getModifiers() );
  }

  public void setFinal( boolean bFinal )
  {
    _modifiers.setModifiers( Modifier.setFinal( _modifiers.getModifiers(), bFinal ) );
  }

  public boolean isEnumConstant() {
    return Modifier.isEnum( getModifiers() );
  }

  public void setEnumConstant( boolean bEnumConstant ) {
    _modifiers.setModifiers( Modifier.setEnum( _modifiers.getModifiers(), bEnumConstant ) );
  }

  public boolean isTransient()
  {
    return Modifier.isTransient( getModifiers() );  
  }

  public IType getType()
  {
    return _symbol == null ? null : _symbol.getType();
  }

  public void setScriptPart( IScriptPartId partId )
  {
    _scriptPartId = partId;
  }

  public IScriptPartId getScriptPart()
  {
    return _scriptPartId;
  }

  /**
   * Executes the Var statement.  The algorithm for the execution follows:
   * <ol>
   * <li> Evaluate the ValueExpression (if one exists).
   * <li> Create and map a symbol for the Identifier with type reflecting the ValueExpression's type.
   * <li> If a TypeLiteral is specified, set the Identifier's type accordingly.
   * </ol>
   */
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }
    
    throw new CannotExecuteGosuException();
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }

  @Override
  public String toString()
  {
    return "var " + getIdentifierName() +
           (getTypeLiteral() != null ? (" : " + getTypeLiteral().toString()) : "" ) +
           (getAsExpression() != null ? (" = " + getAsExpression().toString()) : "" );
  }

  @Override
  public String getFunctionName()
  {
    if( getParent() instanceof ClassStatement )
    {
      return "_init";
    }
    else
    {
      return super.getFunctionName();
    }
  }

  @Override
  public int getNameOffset( CaseInsensitiveCharSequence identifierName )
  {
    return identifierName == null || identifierName.equals( getIdentifierName() ) 
           ? _iNameOffset
           : identifierName.equals( getPropertyName() )
             ? _iPropertyNameOffset
             : -1;
  }
  @Override
  public void setNameOffset( int iOffset, CaseInsensitiveCharSequence identifierName )
  {
    if( identifierName != null && getPropertyName() != null && identifierName.equals( getPropertyName() ) )
    {
      _iPropertyNameOffset = iOffset;
    }
    else
    {
      _iNameOffset = iOffset;
    }
  }

  public boolean declares( CaseInsensitiveCharSequence identifierName )
  {
    return GosuObjectUtil.equals(getIdentifierName(), identifierName ) ||
           GosuObjectUtil.equals(getPropertyName(), identifierName );
  }

  public String[] getDeclarations() {
    if (getPropertyName() == null) {
      return new String[] {getIdentifierName().toString()};
    } else {
      return new String[] {getIdentifierName().toString(), getPropertyName().toString()};
    }
  }

  private IFeatureInfo findOwningFeatureInfoOfDeclaredSymbols(CaseInsensitiveCharSequence identifierName)
  {
    // sct: The only cases that I know of:
    // 1. var has no enclosing type, or
    // 2. it is a "child" of ClassStatement, or
    // 3. it is local to a function or property (i.e. a "child" of FunctionStatement or PropertyStatement), or
    // 4. it is local to a block
    IParsedElement parsedElement = findAncestorParsedElementByType( ClassStatement.class,
                                                                    FunctionStatement.class,
                                                                    PropertyStatement.class,
                                                                    BlockExpression.class );
    if( parsedElement == null )
    {
      return null;
    }
    else if( parsedElement instanceof ClassStatement )
    {
      return ((ClassStatement)parsedElement).getGosuClass().getTypeInfo();
    }
    else if( parsedElement instanceof FunctionStatement )
    {
      DynamicFunctionSymbol dfs = ((FunctionStatement)parsedElement).getDynamicFunctionSymbol();
      if( dfs != null )
      {
        return dfs.getMethodOrConstructorInfo();
      }
      else
      {
        return null;
      }
    }
    else if( parsedElement instanceof PropertyStatement )
    {
      return ((PropertyStatement)parsedElement).getPropertyGetterOrSetter().getDynamicFunctionSymbol().getMethodOrConstructorInfo();
    }
    else
    {
      return null;
    }
  }

  public List<IGosuAnnotation> getGosuAnnotations()
  {
    return _modifiers.getAnnotations();
  }

  public String getFullDescription()
  {
    return _modifiers == null ? "" : _modifiers.getDescription();
  }

  public void clearDefn()
  {
    _expression = null;
  }

  public boolean isDefinitionParsed()
  {
    return _bDefinitionParsed;
  }
  public void setDefinitionParsed( boolean bParsed )
  {
    _bDefinitionParsed = bParsed;
  }
  
  public boolean getHasInitializer()
  {
    return _expression != null;
  }

  public boolean isImplicitlyUsed() {
    return !isPrivate() || hasProperty();
  }

  public int getPropertyNameOffset() {
    return _iPropertyNameOffset;
  }

  @Override
  public boolean isFieldDeclaration() {
    return getParent() instanceof IClassStatement;
  }
    
}
