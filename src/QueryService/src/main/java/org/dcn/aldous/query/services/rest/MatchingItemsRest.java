package org.dcn.aldous.query.services.rest;

import com.google.inject.Inject;
import feign.Headers;
import io.dropwizard.auth.Auth;
import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.items.ItemsSearcher;
import org.dcn.aldous.database.users.AldousUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1.0/query")
@Produces(MediaType.APPLICATION_JSON)
public class MatchingItemsRest {

  private static final int LIMIT = 20;

  private final ItemsSearcher repository;

  @Inject
  public MatchingItemsRest(final ItemsSearcher repository) {
    this.repository = repository;
  }

  @GET
  @Path("getMatchingItems")
  @Headers("Content-Encoding: gzip")
  public List<Item> getMatchingItems(
      @Auth AldousUser user,
      @QueryParam("query") String query) {
    List<Item> items = repository.find(query);
    if (items.size() > LIMIT) {
      return items.subList(0, LIMIT);
    }
    return items;
  }

}
