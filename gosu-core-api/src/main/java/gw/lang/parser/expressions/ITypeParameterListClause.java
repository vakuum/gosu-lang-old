/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;


public interface ITypeParameterListClause extends IExpression, Cloneable
{
  ITypeLiteralExpression[] getTypes();
}
