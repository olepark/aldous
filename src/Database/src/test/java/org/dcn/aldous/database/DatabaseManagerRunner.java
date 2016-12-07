package org.dcn.aldous.database;

import org.dcn.aldous.database.lists.ItemList;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.providers.ConfigProvider;

public class DatabaseManagerRunner {

  public static void main(String[] args) throws Exception {
    DatabaseManager manager = DatabaseManager.of(new ConfigProvider().get());
    manager.dropTable(ItemList.class);
    manager.createTable(ItemList.class);
    manager.viewTable("lists");
  }

}
