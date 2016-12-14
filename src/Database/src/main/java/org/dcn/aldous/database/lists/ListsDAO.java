package org.dcn.aldous.database.lists;

import com.github.davidmoten.rx.jdbc.Database;
import org.dcn.aldous.database.DAO;
import org.dcn.aldous.database.ORM;
import org.dcn.aldous.database.PostgresTypeMapper;

public class ListsDAO extends DAO<ItemList> {

  private ListsDAO(Database database, ORM<ItemList> orm) {
    super(database, orm);
  }

  public static ListsDAO create(Database database) {
    return new ListsDAO(database, new ORM<>(ItemList.class, new PostgresTypeMapper()));
  }
}
