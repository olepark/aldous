package org.dcn.aldous.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.lists.ListsDAO;

public class ListsDAOProvider implements Provider<ListsDAO> {

  private final DBProvider dbProvider;

  @Inject
  public ListsDAOProvider(DBProvider dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ListsDAO get() {
    return ListsDAO.create(dbProvider.get());
  }

}
