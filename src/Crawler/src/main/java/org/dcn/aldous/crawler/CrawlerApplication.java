package org.dcn.aldous.crawler;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dcn.aldous.crawler.dependencies.CrawlerGuiceModule;
import org.dcn.aldous.crawler.services.rest.CrawlerHealthCheck;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class CrawlerApplication extends Application<Configuration> {

  private static final String REST_PACKAGE = "org.dcn.aldous.crawler.services.rest";

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    GuiceBundle<Configuration> guiceBundle = GuiceBundle.builder()
        .modules(new CrawlerGuiceModule())
        .enableAutoConfig(REST_PACKAGE)
        .build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    environment.healthChecks().register("crawler", new CrawlerHealthCheck());
  }

  public static void main(String[] args) throws Exception {
    new CrawlerApplication().run(args);
  }
}
