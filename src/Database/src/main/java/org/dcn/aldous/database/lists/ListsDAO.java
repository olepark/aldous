package org.dcn.aldous.database.lists;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.dcn.aldous.database.DAO;
import org.dcn.aldous.database.SerializingUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListsDAO extends DAO<ItemList> {

  private ListsDAO(Database database,
                   Class<ItemList> entityClass,
                   Function<ResultSet, ItemList> rsParser,
                   BiFunction<Field, ItemList, String> fieldSerializer) {
    super(database, entityClass, rsParser, fieldSerializer);
  }

  @SneakyThrows(SQLException.class)
  private static ItemList getList(ResultSet rs) {
    return new ItemList(rs.getInt(1), rs.getString(2),
        getIds(rs.getString(3)), getIds(rs.getString(4)));
  }

  private static List<Integer> getIds(String string) {
    return Stream.of(string.split("/,/"))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

  public static ListsDAO create(Database database) {
    return new ListsDAO(database, ItemList.class, ListsDAO::getList, SerializingUtil::serialize);
  }
}
