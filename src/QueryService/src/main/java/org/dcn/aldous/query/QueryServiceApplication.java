package org.dcn.aldous.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dcn.aldous.query.dependencies.QueryServiceModule;
import org.dcn.aldous.query.services.rest.QueryServiceHealthCheck;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class QueryServiceApplication extends Application<Configuration> {

  private static final String REST_PACKAGE = "org.dcn.aldous.query.services.rest";

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    GuiceBundle<Configuration> guiceBundle = GuiceBundle.builder()
        .modules(new QueryServiceModule())
        .enableAutoConfig(REST_PACKAGE)
        .build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    final ObjectMapper mapper = environment.getObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    mapper.findAndRegisterModules();
    environment.healthChecks().register("query", new QueryServiceHealthCheck());
  }

  public static void main(String[] args) throws Exception {
    new QueryServiceApplication().run(args);
  }
}
