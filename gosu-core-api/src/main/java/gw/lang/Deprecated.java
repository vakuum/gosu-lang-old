/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Deprecated {
  public String value();
}
