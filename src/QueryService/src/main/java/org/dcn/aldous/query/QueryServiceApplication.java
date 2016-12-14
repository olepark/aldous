package org.dcn.aldous.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.query.dependencies.QueryServiceModule;
import org.dcn.aldous.query.services.auth.AldousAuthenticator;
import org.dcn.aldous.query.services.auth.AldousAuthorizer;
import org.dcn.aldous.query.services.rest.QueryServiceHealthCheck;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
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
    environment.jersey().register(new AuthDynamicFeature(
        new OAuthCredentialAuthFilter.Builder<AldousUser>()
            .setAuthenticator(new AldousAuthenticator())
            .setAuthorizer(new AldousAuthorizer())
            .setPrefix("Bearer")
            .buildAuthFilter()));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AldousUser.class));
  }

  public static void main(String[] args) throws Exception {
    new QueryServiceApplication().run(args);
  }
}
