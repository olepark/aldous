package org.dcn.aldous.database.users;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsersDAO {

  private final Database database;

  private final PasswordEncriptor encriptor;

  public AldousUser getUserByCredentials(String username, String password) {
    return database.select("* from users where username=" + username + " AND password=" + encriptor.encrypt(password))
        .get(rs -> new AldousUser(rs.getString(2)))
        .toBlocking().first();
  }

}
