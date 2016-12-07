package org.dcn.aldous.database.lists;

import com.github.davidmoten.rx.jdbc.Database;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import rx.Observable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class ListsDAO {

  private final Database database;

  public Optional<ItemList> getListById(Integer id) {
    Observable<ItemList> list = database.select("* from users where id=" + id)
        .get(rs -> new ItemList(rs.getInt(1), rs.getString(2),
            getIds(rs.getString(3)), getIds(rs.getString(4))));
    return firstOfEmpty(list);
  }

  private List<Integer> getIds(String string) {
    return Stream.of(string.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

  private Optional<ItemList> firstOfEmpty(Observable<ItemList> observable) {
    try {
      return Optional.of(observable.toBlocking().first());
    } catch (NoSuchElementException ex) {
      return Optional.empty();
    }
  }
}
