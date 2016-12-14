package org.dcn.aldous.query.services.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.users.AldousUser;

import javax.inject.Inject;
import java.util.Optional;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class AldousAuthenticator implements Authenticator<String, AldousUser> {

  private final UsersService usersService;

  @Override
  public Optional<AldousUser> authenticate(String session) throws AuthenticationException {
    return usersService.userBySession(session);
  }
}
