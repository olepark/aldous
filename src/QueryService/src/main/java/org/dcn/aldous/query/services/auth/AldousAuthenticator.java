package org.dcn.aldous.query.services.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.dcn.aldous.database.users.AldousUser;

import java.util.Optional;

public class AldousAuthenticator implements Authenticator<String, AldousUser> {

  @Override
  public Optional<AldousUser> authenticate(String s) throws AuthenticationException {
    return Optional.empty();
  }
}
