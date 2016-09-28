package org.dcn.aldous.app.services.rest;

import com.google.inject.Inject;
import org.dcn.aldous.app.services.ExampleService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api/1.0/example")
@Produces(MediaType.APPLICATION_JSON)
public class ExampleRest {

  private final ExampleService service;

  @Inject
  public ExampleRest(final ExampleService service) {
    this.service = service;
  }

  @GET
  @Path("ping")
  public String ping() {
    return service.ping();
  }

}
