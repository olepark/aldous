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

  @POST
  @Path("add")
  public Integer addUser(@FormParam("name") String name,
                         @FormParam("username") String username,
                         @FormParam("password") String password) {
    if (usersDAO.exists(username)) {
      return null;
    } else {
      return usersDAO.add(new AldousUser(null, name, newArrayList(), username, password));
    }
  }

}
