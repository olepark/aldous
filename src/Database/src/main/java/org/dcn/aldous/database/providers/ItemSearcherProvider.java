package org.dcn.aldous.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.items.ItemsSearcher;

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
