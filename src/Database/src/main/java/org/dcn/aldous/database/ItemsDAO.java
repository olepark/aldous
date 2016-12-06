package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import rx.Observable;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class ItemsDAO {

  private final Database database;

  private static final String delimiter = "/,/";

  public void addItem(Item item) {
    int inserted = database
        .update("insert into items(" + columnsString() + ") values(?, ?, ?, ?, ?, ?)")
        .parameter(item.vendor())
        .parameter(item.name())
        .parameter(item.url())
        .parameter(item.price())
        .parameter(getJoined(item.tags()))
        .parameter(getJoined(item.properties()))
        .execute();
    Preconditions.checkState(inserted == 1, "Failed to insert " + item);
  }

  private String columnsString() {
    return columns().collect(joining(", "));
  }

  private Stream<String> columns() {
    return Arrays.asList(Item.class.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class))
        .map(f -> f.getAnnotation(Column.class).name());
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
    String where = "where " + columns()
        .map(col -> format("UPPER(%s) LIKE '%%%s%%'", col, word.toUpperCase()))
        .collect(joining(" OR "));
    return selectWithAppend(where);
  }

  private Observable<Item> selectWithAppend(String append) {
    return database.select("select * from items " + append)
        .get(rs -> new Item(rs.getInt(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5),
            Arrays.asList(rs.getString(6).split(delimiter)),
            Arrays.asList(rs.getString(7).split(delimiter))));
  }

  private String getJoined(List<String> strings) {
    return strings.stream().collect(joining(delimiter));
  }
}
