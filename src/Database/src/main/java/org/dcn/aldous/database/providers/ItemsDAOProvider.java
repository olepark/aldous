package org.dcn.aldous.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.database.items.ItemsDAO;

public class ItemsDAOProvider implements Provider<ItemsDAO> {

  private final DBProvider dbProvider;

  @Inject
  public ItemsDAOProvider(DBProvider dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ItemsDAO get() {
    return ItemsDAO.create(dbProvider.get());
  }

}
