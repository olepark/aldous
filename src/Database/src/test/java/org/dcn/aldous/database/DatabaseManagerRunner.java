package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.typesafe.config.Config;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.providers.DBProvider;
import org.dcn.aldous.database.users.UsersDAO;
import org.dcn.aldous.providers.ConfigProvider;

public class DatabaseManagerRunner {

  private static final Config config = new ConfigProvider().get();

  private static final Database database = new DBProvider(config).get();

  public static void main(String[] args) throws Exception {
    createAll();
    viewAll();
  }

  private static void dropAll() {
    ItemsDAO.create(database).tableManager().dropTable();
    UsersDAO.create(database).tableManager().dropTable();
    ListsDAO.create(database).tableManager().dropTable();
  }

  private static void createAll() {
    ItemsDAO.create(database).tableManager().createIfAbsent();
    UsersDAO.create(database).tableManager().createIfAbsent();
    ListsDAO.create(database).tableManager().createIfAbsent();
  }

  private static void viewAll() {
    DatabaseManager manager = DatabaseManager.of(config);
//    manager.viewTable("items");
    manager.viewTable("users");
    manager.viewTable("lists");
  }

}
