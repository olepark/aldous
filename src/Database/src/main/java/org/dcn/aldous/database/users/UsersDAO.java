package org.dcn.aldous.database.users;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.DAO;
import org.dcn.aldous.database.SerializingUtil;
import rx.Observable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class UsersDAO extends DAO<AldousUser> {

  private final PasswordEncryptor encryptor;

  public UsersDAO(Database database,
                  Class<AldousUser> entityClass,
                  Function<ResultSet, AldousUser> rsParser,
                  BiFunction<Field, AldousUser, String> fieldSerializer,
                  PasswordEncryptor encryptor) {
    super(database, entityClass, rsParser, fieldSerializer);
    this.encryptor = encryptor;
  }

  public Optional<AldousUser> getUserByCredentials(String username, String password) {
    String where = "username=" + username + " AND password=" + encryptor.encrypt(password);
    return selectFirstWhere(where);
  }

  public static UsersDAO create(Database database) {
    return new UsersDAO(database, AldousUser.class, UsersDAO::getAldousUser,
        SerializingUtil::serialize, new PasswordEncryptor());
  }

  protected static AldousUser getAldousUser(ResultSet rs) {
    try {
      return new AldousUser(rs.getInt(1), rs.getString(2), Stream.of(rs.getString(3).split("/,/"))
          .map(Integer::parseInt)
          .collect(toList()));
    } catch (SQLException e) {
      throw new IllegalStateException("Can't parse user");
    }
  }

}
