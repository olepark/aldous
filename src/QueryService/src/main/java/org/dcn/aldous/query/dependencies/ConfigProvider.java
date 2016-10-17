package org.dcn.aldous.query.dependencies;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigParseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.File;

import static com.typesafe.config.ConfigFactory.parseResources;
import static java.lang.Thread.currentThread;

public class ConfigProvider implements Provider<Config> {

  @Override
  public Config get() {
    final ConfigParseOptions options = ConfigParseOptions.defaults().setAllowMissing(false);
    final ClassLoader classLoader = currentThread().getContextClassLoader();
    final Config c = parseResources(classLoader, "application.conf", options);
    final File folder = new File(c.origin().filename()).getParentFile();
    return c.resolve();
  }
}