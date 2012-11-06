/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.EvalExpression;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.IType;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IParseResult;
import gw.config.CommonServices;
import gw.util.GosuExceptionUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.lang.reflect.Constructor;

/**
 */
public class EvalExpressionTransformer extends EvalBasedTransformer<EvalExpression>
{
  public static final List<EvalExpression> EVAL_EXPRESSIONS = Collections.synchronizedList( new ArrayList<EvalExpression>() );

  public static IRExpression compile( TopLevelTransformationContext cc, EvalExpression expr )
  {
    EvalExpressionTransformer compiler = new EvalExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private EvalExpressionTransformer( TopLevelTransformationContext cc, EvalExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    int iEvalExprId = getEvalExpressionId( _expr() );

    return callStaticMethod( EvalExpressionTransformer.class, "compileAndRunEvalSource", new Class[]{Object.class, Object.class, Object[].class, IType[].class, IType.class, int.class},
            exprList(
                    boxValue( _expr().getType(), ExpressionTransformer.compile( _expr().getExpression(), _cc() ) ),
                    pushEnclosingContext(),
                    pushCapturedSymbols( getGosuClass(), _expr().getCapturedForBytecode() ),
                    pushEnclosingFunctionTypeParamsInArray( _expr() ),
                    pushType( getGosuClass() ),
                    pushConstant( iEvalExprId )
            ));
  }

  public static int getEvalExpressionId( EvalExpression expr )
  {
    int iEvalExprId;
    synchronized( EVAL_EXPRESSIONS )
    {
      iEvalExprId = EVAL_EXPRESSIONS.size();
      EVAL_EXPRESSIONS.add( expr );
    }
    return iEvalExprId;
  }

  public static Object compileAndRunEvalSource( Object source, Object outer, Object[] capturedValues,
                                                IType[] immediateFuncTypeParams, IType enclosingClass, int iEvalExprId )
  {
    EvalExpression evalExpr = EVAL_EXPRESSIONS.get( iEvalExprId );
    return compileAndRunEvalSource( source, outer, capturedValues, immediateFuncTypeParams, enclosingClass, evalExpr );
  }

  public static Object compileAndRunEvalSource( Object source, Object outer, Object[] capturedValues,
                                                IType[] immediateFuncTypeParams, IType enclosingClass, IParsedElement evalExpr )
  {
    return compileAndRunEvalSource( source, outer, capturedValues, immediateFuncTypeParams, enclosingClass, evalExpr, null, null );
  }
  public static Object compileAndRunEvalSource( Object source, Object outer, Object[] capturedValues,
                                                IType[] immediateFuncTypeParams, IType enclosingClass, IParsedElement evalExpr,
                                                ISymbolTable compileTimeLocalContextSymbols, IExternalSymbolMap runtimeLocalSymbolValues )
  {
    String strSource = CommonServices.getCoercionManager().makeStringFrom( source );
    IGosuProgramParser parser = GosuParserFactory.createProgramParser();
    List<ICapturedSymbol> capturedSymbols = evalExpr instanceof EvalExpression ? ((EvalExpression)evalExpr).getCapturedForBytecode() : Collections.<ICapturedSymbol>emptyList();
    //debugInfo( compileTimeLocalContextSymbols );
    IParseResult res = parser.parseEval( strSource, capturedSymbols, enclosingClass, evalExpr, compileTimeLocalContextSymbols );
    IGosuProgram gp = res.getProgram();
    if( !gp.isValid() )
    {
      throw GosuExceptionUtil.forceThrow(gp.getParseResultsException());
    }

    Class<?> javaClass = gp.getBackingClass();
    List<Object> args = new ArrayList<Object>();
    if( !gp.isStatic() )
    {
      args.add( outer );
    }

    addCapturedValues( (IGosuProgramInternal)gp, capturedSymbols, capturedValues, args );

    addEnclosingTypeParams( immediateFuncTypeParams, args );

    Constructor ctor = javaClass.getConstructors()[0];
    Class[] parameterTypes = ctor.getParameterTypes();
    if( parameterTypes.length != args.size() )
    {
      if( parameterTypes.length > args.size() &&
          parameterTypes[parameterTypes.length-1].getName().equals( IExternalSymbolMap.class.getName() ) )
      {
        args.add( runtimeLocalSymbolValues );
      }
      else
      {
        throw new IllegalStateException( "Eval constructor param count is not " + args.size() + "\nPassed in args " + printArgs( args ) + "\nActual args: " + printArgs( ctor.getParameterTypes() ) );
      }
    }
    try
    {
//      Class[] parameterTypes = ctor.getParameterTypes();
//      for( int i = 0; i < parameterTypes.length; i++ ) {
//        System.out.println( "PARAM: " + parameterTypes[i].getName() + "  ARG: " + args.get( i ) );
//      }
      IProgramInstance evalInstance = (IProgramInstance)ctor.newInstance( args.toArray() );
      return evalInstance.evaluate( runtimeLocalSymbolValues );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private static String printArgs( List<Object> args ) {
    String str = "";
    for( Object a: args ) {
      str += a + ", ";
    }
    return str;
  }

  private static String printArgs( Class[] parameterTypes ) {
    String str = "";
    for( Class c: parameterTypes ) {
      str += c.getName() + ", ";
    }
    return str;
  }

  private static void debugInfo( ISymbolTable compileTimeLocalContextSymbols ) {
    if( compileTimeLocalContextSymbols != null ) {
      Map symbols = compileTimeLocalContextSymbols.getSymbols();
      for( Object key : symbols.keySet() ) {
        Object o = symbols.get( key );
        System.out.println( "SYMBOL NAME: " + key + " SYMBOL: " + o );
      }
    }
  }

  private static void addCapturedValues( IGosuProgramInternal gp, List<ICapturedSymbol> capturedSymbols, Object[] capturedValues, List<Object> args )
  {
    if( capturedValues != null )
    {
      // Note: must add the captured symbols in the order of the eval class' ctor, which is the order of the values in its map of captured symbols.

      Map<CaseInsensitiveCharSequence, ICapturedSymbol> capturedSymbolsByName = gp.getCapturedSymbols();
      if( capturedSymbolsByName != null )
      {
        for( ICapturedSymbol sym : capturedSymbolsByName.values() )
        {
          args.add( capturedValues[capturedSymbols.indexOf( sym )] );
        }
      }

      if (requiresExternalSymbolCapture(gp)) {
        args.add( capturedValues[capturedValues.length - 1] );
      }
    }
  }

  public static void clearEvalExpressions() {
    EVAL_EXPRESSIONS.clear();
  }

}
