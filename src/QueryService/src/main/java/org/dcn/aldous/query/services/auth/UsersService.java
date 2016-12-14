package org.dcn.aldous.query.services.auth;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.database.users.UsersDAO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class UsersService {

  private final UsersDAO usersDAO;

  private final Map<String, Integer> sessionToUserId;

  public Pair<String, AldousUser> sessionByCredentials(String login, String password) {
    Optional<AldousUser> optional = usersDAO.getUserByCredentials(login, password);
    if (optional.isPresent()) {
      String session = UUID.randomUUID().toString();
      AldousUser user = optional.get();
      sessionToUserId.put(session, user.getId());
      return Pair.of(session, user);
    } else {
      return null;
    }
  }

  public Optional<AldousUser> userBySession(String session) {
    Preconditions.checkArgument(sessionToUserId.containsKey(session), "No such session");
    return usersDAO.getById(sessionToUserId.get(session));
  }
}