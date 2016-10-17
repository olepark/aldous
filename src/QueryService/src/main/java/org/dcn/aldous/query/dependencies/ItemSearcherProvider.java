package org.dcn.aldous.query.dependencies;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.database.ItemsDAO;
import org.dcn.aldous.query.services.query.ItemsSearcher;

public class ItemSearcherProvider implements Provider<ItemsSearcher> {

  private final ItemsDAO itemsDAO;

  @Inject
  public ItemSearcherProvider(ItemsDAO itemsDAO) {
    this.itemsDAO = itemsDAO;
  }

  @Override
  public ItemsSearcher get() {
    return new ItemsSearcher(itemsDAO);
  }
}
