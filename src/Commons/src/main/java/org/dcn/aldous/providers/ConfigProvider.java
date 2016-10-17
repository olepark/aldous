package org.dcn.aldous.providers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigParseOptions;

import javax.inject.Provider;

import static com.typesafe.config.ConfigFactory.parseResources;
import static java.lang.Thread.currentThread;

public class ConfigProvider implements Provider<Config> {

  @Override
  public Config get() {
    final ConfigParseOptions options = ConfigParseOptions.defaults().setAllowMissing(false);
    final ClassLoader classLoader = currentThread().getContextClassLoader();
    final Config c = parseResources(classLoader, "application.conf", options);
    return c.resolve();
  }
}