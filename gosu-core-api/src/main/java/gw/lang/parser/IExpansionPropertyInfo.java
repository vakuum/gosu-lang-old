/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IPropertyInfo;

public interface IExpansionPropertyInfo extends IPropertyInfo
{
  IPropertyInfo getDelegate();
}
