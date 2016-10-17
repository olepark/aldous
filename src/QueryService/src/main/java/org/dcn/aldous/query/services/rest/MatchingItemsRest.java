package org.dcn.aldous.query.services.rest;

import com.google.inject.Inject;
import org.dcn.aldous.database.Item;
import org.dcn.aldous.query.services.query.MatchingItemsService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1.0/query")
@Produces(MediaType.APPLICATION_JSON)
public class MatchingItemsRest {

  private final MatchingItemsService service;

  @Inject
  public MatchingItemsRest(final MatchingItemsService service) {
    this.service = service;
  }

  @GET
  @Path("getMatchingItems")
  public List<Item> getMatchingItems(@QueryParam("query") String query) {
    return service.getMatchingItems(query);
  }

}
