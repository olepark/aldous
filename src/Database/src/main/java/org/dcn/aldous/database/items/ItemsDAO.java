package org.dcn.aldous.database.items;

import com.github.davidmoten.rx.jdbc.Database;
import org.dcn.aldous.database.DAO;
import org.dcn.aldous.database.ORM;
import org.dcn.aldous.database.PostgresTypeMapper;
import rx.Observable;

import javax.persistence.Column;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class ItemsDAO extends DAO<Item> {

  private static final String delimiter = "/,/";

  private ItemsDAO(Database database, ORM<Item> orm) {
    super(database, orm);
  }

  public Observable<Item> getMatchingItems(String description) {
    if (description.isEmpty()) {
      return Observable.empty();
    }
    return Stream.of(description.split(" "))
        .distinct()
        .map(this::whereContainsWord)
        .reduce(Observable::mergeWith)
        .orElseGet(Observable::empty)
        .distinct(Item::id);
  }

  private Observable<Item> whereContainsWord(String word) {
    String where = columns()
        .map(col -> format("UPPER(%s) LIKE '%%%s%%'", col, word.toUpperCase()))
        .collect(joining(" OR "));
    return selectWhere(where);
  }

  private Stream<String> columns() {
    return Arrays.asList(Item.class.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class))
        .map(f -> f.getAnnotation(Column.class).name());
  }

  private static Item getItem(ResultSet rs) {
    try {
      return new Item(rs.getInt(1),
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          rs.getString(5),
          Arrays.asList(rs.getString(6).split(delimiter)),
          Arrays.asList(rs.getString(7).split(delimiter)));
    } catch (SQLException e) {
      throw new IllegalStateException("Cannot parse item from sql result");
    }
  }

  public static ItemsDAO create(Database database) {
    return new ItemsDAO(database, new ORM<>(Item.class, new PostgresTypeMapper()));
  }
}
