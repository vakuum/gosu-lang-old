/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRSyntheticStatement extends IRStatement {
  private IRExpression _expression;

  public IRSyntheticStatement(IRExpression expression) {
    _expression = expression;
    expression.setParent( this );
  }

  public IRExpression getExpression() {
    return _expression;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
