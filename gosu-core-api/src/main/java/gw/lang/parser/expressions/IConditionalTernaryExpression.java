/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IConditionalTernaryExpression extends IExpression
{
  IExpression getCondition();

  IExpression getFirst();

  IExpression getSecond();
}
