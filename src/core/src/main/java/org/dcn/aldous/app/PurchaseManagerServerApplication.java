package org.dcn.aldous.app;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PurchaseManagerServerApplication extends Application<Configuration> {

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {

  }

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {

  }

  public static void main(String[] args) throws Exception {
    new PurchaseManagerServerApplication().run(args);
  }
}
