/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.*;
import gw.lang.reflect.*;

import java.util.List;

public interface IGosuMethodInfo extends IAttributedFeatureInfo, IGenericMethodInfo, IMethodInfo, IOptionalParamCapable, Comparable, IDFSBackedFeatureInfo
{
  boolean isMethodForProperty();

  List<IReducedSymbol> getArgs();

  IGosuMethodInfo getBackingMethodInfo();
}
