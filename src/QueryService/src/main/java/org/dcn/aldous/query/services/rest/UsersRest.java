package org.dcn.aldous.query.services.rest;

import com.google.common.collect.Lists;
import feign.Headers;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.database.users.UsersDAO;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Path("api/1.0/user")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__(@Inject))
public class UsersRest {

  private final UsersDAO usersDAO;

  @GET
  @Path("add")
  @Headers("Content-Encoding: gzip")
  public Integer addUser(@QueryParam("name") String name,
                         @QueryParam("username") String username,
                         @QueryParam("password") String password) {
    return usersDAO.add(new AldousUser(null, name, newArrayList(), username, password));
  }

  @GET
  @Path("get")
  @Headers("Content-Encoding: gzip")
  public AldousUser getUser(@QueryParam("userId") @NotNull Integer id) {
    return usersDAO.getById(id)
        .orElseThrow(() -> new IllegalStateException("User not found for id " + id));
  }

}
