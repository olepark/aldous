package org.dcn.aldous.query.services.rest;

import feign.Headers;
import io.dropwizard.auth.Auth;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.lists.ItemList;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.database.users.UsersDAO;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Path("api/1.0/list")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ListsRest {

  private final ListsDAO listsDAO;

  private final UsersDAO usersDAO;

  @GET
  @Path("add")
  @Headers("Content-Encoding: gzip")
  public void addList(@Auth AldousUser user,
                      @QueryParam("name") String name) {
    Integer ownerId = user.getId();
    listsDAO.add(new ItemList(0, name, newArrayList(), ownerId));
  }

  @GET
  @Path("add")
  @Headers("Content-Encoding: gzip")
  public void addItemToList(@QueryParam("listId") Integer listId,
                            @QueryParam("itemId") @NotNull Integer itemId) {
    List<Integer> itemIds = listsDAO.getById(listId).get().getItemIds();
    itemIds.add(itemId);
    listsDAO.update(listId, "itemIds", itemIds);
  }

}
