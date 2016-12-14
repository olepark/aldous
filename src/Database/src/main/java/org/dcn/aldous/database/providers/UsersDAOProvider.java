package org.dcn.aldous.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.users.UsersDAO;

public class UsersDAOProvider implements Provider<UsersDAO> {

  private final DBProvider dbProvider;

  @Inject
  public UsersDAOProvider(DBProvider dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public UsersDAO get() {
    return UsersDAO.create(dbProvider.get());
  }

}
