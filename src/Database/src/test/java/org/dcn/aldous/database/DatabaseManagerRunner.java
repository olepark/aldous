package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.typesafe.config.Config;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.providers.DBProvider;
import org.dcn.aldous.database.users.UsersDAO;
import org.dcn.aldous.providers.ConfigProvider;

public class DatabaseManagerRunner {

  public static void main(String[] args) throws Exception {
    createAll();
  }

  private static void createAll() {
    Config config = new ConfigProvider().get();
    Database database = new DBProvider(config).get();
    ItemsDAO.create(database).tableManager(DAO.DB.POSTGESQL).createIfAbsent();
    UsersDAO.create(database).tableManager(DAO.DB.POSTGESQL).createIfAbsent("username TEXT", "password TEXT");
    ListsDAO.create(database).tableManager(DAO.DB.POSTGESQL).createIfAbsent();
    DatabaseManager manager = DatabaseManager.of(config);
    manager.viewTable("items");
    manager.viewTable("users");
    manager.viewTable("lists");
  }

}
