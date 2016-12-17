package org.dcn.aldous.query.services.rest;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.query.services.auth.UsersService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("api/1.0/auth")
@AllArgsConstructor(onConstructor = @__(@Inject))
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationRest {

  private final UsersService usersService;

  public static final String ALDOUS_SESSION = "AldousSession";

  @POST
  @Path("byPassword")
  public Response byPassword(
      @Context HttpServletRequest request,
      @NotNull @FormParam("login") String login,
      @NotNull @FormParam("password") String password) {
    Pair<String, AldousUser> session = usersService.sessionByCredentials(login, password);
    if (session != null) {
      return Response.ok(session.getValue())
          .status(Response.Status.OK)
          .cookie(new NewCookie(ALDOUS_SESSION,
              session.getKey(), "/", null, null,
              NewCookie.DEFAULT_MAX_AGE, false, false))
          .build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

}
