/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.testharness;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Critical {
}
