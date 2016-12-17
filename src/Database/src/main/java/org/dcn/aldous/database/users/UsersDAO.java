package org.dcn.aldous.database.users;

import com.github.davidmoten.rx.jdbc.Database;
import org.dcn.aldous.database.DAO;
import org.dcn.aldous.database.ORM;
import org.dcn.aldous.database.PostgresTypeMapper;

import java.util.Optional;

public class UsersDAO extends DAO<AldousUser> {

  private final PasswordEncryptor encryptor;

  public UsersDAO(Database database,
                  Class<AldousUser> entityClass,
                  PasswordEncryptor encryptor) {
    super(database, new ORM<>(entityClass, new PostgresTypeMapper()));
    this.encryptor = encryptor;
  }

  public Optional<AldousUser> getUserByCredentials(String username, String password) {
    String where = String.format("username='%s' AND password='%s'", username, password);// encryptor.encrypt(password));
    return selectFirstWhere(where);
  }

  public boolean exists(String username) {
    return selectFirstWhere(String.format("username='%s'", username)).isPresent();
  }

  public static UsersDAO create(Database database) {
    return new UsersDAO(database, AldousUser.class, new PasswordEncryptor());
  }
}
