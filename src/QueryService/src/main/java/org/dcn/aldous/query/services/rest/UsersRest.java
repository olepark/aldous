package org.dcn.aldous.query.services.rest;

import feign.Headers;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.database.users.UsersDAO;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.google.common.collect.Lists.newArrayList;

@Path("api/1.0/user")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__(@Inject))
public class UsersRest {

  private final UsersDAO usersDAO;

  @GET
  @Path("add")
  @Headers("Content-Encoding: gzip")
  public Integer addUser(@QueryParam("name") String name) {
    return usersDAO.add(new AldousUser(null, name, newArrayList()));
  }

  @GET
  @Path("get")
  @Headers("Content-Encoding: gzip")
  public AldousUser getUser(@NotNull Integer id) {
    return usersDAO.getById(id)
        .orElseThrow(() -> new IllegalStateException("User not found for id " + id));
  }

}
