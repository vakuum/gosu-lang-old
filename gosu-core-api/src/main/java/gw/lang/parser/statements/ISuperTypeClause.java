/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;

public interface ISuperTypeClause extends IExpression
{
  IType getSuperType();
}
