/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.ide.ApplicationLoadListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.idea.IdeaApplication;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationStarter;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GosuApplicationStarter implements ApplicationStarter, ApplicationLoadListener {
  private static final Logger LOG = Logger.getInstance(GosuApplicationStarter.class);

  public static final String COM_GUIDEWIRE = "com.guidewire.";
  public static final String COM_GUIDEWIRE_GOSU = COM_GUIDEWIRE + "gosu";
  public static final String COM_GUIDEWIRE_GUNIT = COM_GUIDEWIRE + "gunit";

  private ApplicationStarter defaultStarter;

  public GosuApplicationStarter() {
    Class<?> aClass = lookupStarterClass( "com.intellij.idea.IdeaUltimateApplication", "IdeaUltimateStarter" );
    if (aClass == null) {
      aClass = lookupStarterClass( "com.intellij.idea.IdeaApplication", "IdeStarter" );
    }

    try {
      final Constructor<?> constructor = aClass.getDeclaredConstructors()[0];
      constructor.setAccessible(true);
      defaultStarter = (ApplicationStarter) constructor.newInstance(IdeaApplication.getInstance());
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  @Override
  public void beforeApplicationLoaded(Application application) {
    // When started without "gosu" command line argument
    fixClassLoaders();
  }

  private Class<?> lookupStarterClass(String appClass, String starterClassName) {
    try {
      // IDEA Ultimate
      Class<?> aClass = Class.forName( appClass );
      for (Class<?> starterClass : aClass.getDeclaredClasses()) {
        if (starterClass.getSimpleName().equals(starterClassName)) {
          return starterClass;
        }
      }
    } catch ( ClassNotFoundException ex ) {
      // Just ignore
    }
    return null;
  }

  @NotNull
  @Override
  public String getCommandName() {
    return "gosu";
  }

  @Override
  public void premain(String[] args) {
    removeCommandLineArgument();
    // When started with "gosu" command line argument
    fixClassLoaders();
    defaultStarter.premain(args);
  }

  public static void fixClassLoaders() {
    IdeaPluginDescriptor mainPlugin = GosuAppComponent.getEditorPlugin();
    // Only fix if we are running in regular IntelliJ (not in tests)
    if (mainPlugin.getPluginClassLoader() instanceof PluginClassLoader) {
      PluginClassLoader mainClassLoader = (PluginClassLoader) mainPlugin.getPluginClassLoader();
      for (IdeaPluginDescriptor plugin : PluginManager.getPlugins()) {
        String id = plugin.getPluginId().getIdString();
        if (id.startsWith(COM_GUIDEWIRE) && !id.equals(COM_GUIDEWIRE_GOSU)) {
          recoverParents(plugin, mainClassLoader);
          recoverClasspath(plugin, mainClassLoader);
  //        recoverDependencies(plugin, (IdeaPluginDescriptorImpl) mainPlugin);
          ((IdeaPluginDescriptorImpl)plugin).setLoader(mainClassLoader, false);
        }
      }
    }
  }

  private static void recoverParents(IdeaPluginDescriptor plugin, PluginClassLoader mainPluginPluginClassLoader) {
    try {
      final Field field = IdeaReflectionUtil.getField("myParents", "a", PluginClassLoader.class);
      field.setAccessible(true);

      List<ClassLoader> mainParents = new ArrayList<>(Arrays.asList((ClassLoader[]) field.get(mainPluginPluginClassLoader)));
      for (ClassLoader cl : (ClassLoader[])field.get(plugin.getPluginClassLoader())) {
        if (!mainParents.contains(cl) && !((PluginClassLoader)cl).getPluginId().getIdString().startsWith(COM_GUIDEWIRE)) {
          mainParents.add(cl);
        }
      }
      field.set(mainPluginPluginClassLoader, mainParents.toArray(new ClassLoader[mainParents.size()]));
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  private void recoverDependencies(IdeaPluginDescriptor plugin, IdeaPluginDescriptorImpl mainPlugin) {
    HashSet<PluginId> existingPlugins = new HashSet<>(Arrays.asList(mainPlugin.getDependentPluginIds()));
    for (PluginId id : plugin.getDependentPluginIds()) {
      if (!existingPlugins.contains(id) && !id.equals(mainPlugin.getPluginId())) {
        mainPlugin.insertDependency(PluginManager.getPlugin(id));
      }
    }
  }

  private static void recoverClasspath(@NotNull IdeaPluginDescriptor plugin, @NotNull PluginClassLoader mainClassLoader) {
    HashSet<URL> existingURLs = new HashSet<>(mainClassLoader.getUrls());
    for (URL url : ((PluginClassLoader) plugin.getPluginClassLoader()).getUrls()) {
      if (!existingURLs.contains(url)) {
        mainClassLoader.addURL(url);
      }
    }
  }

  private void removeCommandLineArgument() {
    try {
      final Field field = IdeaReflectionUtil.getField("myArgs", "a", IdeaApplication.class);
      field.setAccessible(true);
      field.set(IdeaApplication.getInstance(), new String[0]);
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  @Override
  public void main(String[] args) {
    defaultStarter.main(new String[0]);
  }
}
