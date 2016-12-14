package org.dcn.aldous.query.services.auth;

import io.dropwizard.auth.Authorizer;
import org.dcn.aldous.database.users.AldousUser;

public class AldousAuthorizer implements Authorizer<AldousUser> {

  @Override
  public boolean authorize(AldousUser aldousUser, String s) {
    return aldousUser != null;
  }
}
