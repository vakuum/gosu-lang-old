/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 * Allows clearing static typeloader data. Do not use lightly.
 */
public interface IUninitializableTypeLoader {
  void uninitialize();
}
