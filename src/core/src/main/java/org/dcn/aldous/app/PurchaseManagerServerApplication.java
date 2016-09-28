package org.dcn.aldous.app;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dcn.aldous.app.dependencies.PurchaseManagerModule;
import org.dcn.aldous.app.services.rest.ManagerHealthCheck;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class PurchaseManagerServerApplication extends Application<Configuration> {

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    GuiceBundle<Configuration> guiceBundle = GuiceBundle.builder()
        .modules(new PurchaseManagerModule())
        .build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    environment.jersey().packages("org.dcn.aldous.app.services.rest");
    environment.healthChecks().register("app", new ManagerHealthCheck());
  }

  public static void main(String[] args) throws Exception {
    new PurchaseManagerServerApplication().run(args);
  }
}
