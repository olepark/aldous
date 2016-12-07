package org.dcn.aldous.database.users;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.lists.ListsDAO;
import rx.Observable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class UsersDAO {

  private final Database database;

  private final PasswordEncriptor encriptor;

  private final ListsDAO listsDAO;

  public Optional<AldousUser> getUserByCredentials(String username, String password) {
    Observable<AldousUser> user = database
        .select("* from users where username=" + username + " AND password=" + encriptor.encrypt(password))
        .get(rs -> new AldousUser(rs.getInt(1), rs.getString(2), getIds(rs.getString(3))));
    return firstOfEmpty(user);
  }

  public Optional<AldousUser> getUserById(Integer id) {
    Observable<AldousUser> user = database.select("* from users where id=" + id)
        .get(rs -> new AldousUser(rs.getInt(1), rs.getString(2), getIds(rs.getString(3))));
    return firstOfEmpty(user);
  }

  private Optional<AldousUser> firstOfEmpty(Observable<AldousUser> observable) {
    try {
      return Optional.of(observable.toBlocking().first());
    } catch (NoSuchElementException ex) {
      return Optional.empty();
    }
  }

  private List<Integer> getIds(String string) {
    return Stream.of(string.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

}
