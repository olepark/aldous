package org.dcn.aldous.query.services.rest;

import com.google.common.base.Preconditions;
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

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@Path("api/1.0/list")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ListsRest {

  private final ListsDAO listsDAO;

  private final UsersDAO usersDAO;

  @GET
  @Path("addList")
  @Headers("Content-Encoding: gzip")
  public Integer addList(@Auth AldousUser user,
                         @QueryParam("name") String name) {
    Integer ownerId = user.getId();
    Integer listId = listsDAO.add(new ItemList(0, name, newArrayList(), ownerId));
    List<Integer> listIds = user.getListIds();
    listIds.add(listId);
    usersDAO.update(ownerId, "listIds", listIds);
    return listId;
  }

  @GET
  @Path("addItem")
  @Headers("Content-Encoding: gzip")
  public Integer addItemToList(@Auth AldousUser user,
                               @QueryParam("listId") Integer listId,
                               @QueryParam("itemId") @NotNull Integer itemId) {
    Optional<ItemList> byId = listsDAO.getById(listId);
    checkState(byId.isPresent(), format("List with id %d not found", listId));
    ItemList list = byId.get();
    Integer ownerId = list.getOwnerId();
    Integer userId = user.getId();
    checkState(ownerId.equals(userId), format("List %d does not belong to user %d", listId, userId));
    List<Integer> itemIds = list.getItemIds();
    itemIds.add(itemId);
    listsDAO.update(listId, "itemIds", itemIds);
    return listId;
  }

  @GET
  @Path("getList")
  @Headers("Content-Encoding: gzip")
  public ItemList getList(@Auth AldousUser user,
                         @QueryParam("listId") Integer listId) {
    Optional<ItemList> list = listsDAO.getById(listId);
    checkState(list.isPresent(), format("List with id %d not found", listId));
    return list.get();
  }

}
