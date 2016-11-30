package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemsDAO {

  private final Database database;

  private static final String delimiter = "/,/";

  public void addItem(Item item) {
    int inserted = database
        .update("insert into items(vendor_code, vendor, name, url, price, tags, properties) values(?, ?, ?, ?, ?, ?, ?)")
        .parameter(item.vendorCode())
        .parameter(item.vendor())
        .parameter(item.name())
        .parameter(item.url())
        .parameter(item.price())
        .parameter(getJoined(item.tags()))
        .parameter(getJoined(item.properties()))
        .execute();
    Preconditions.checkState(inserted == 1, "Failed to insert " + item);
  }

  public Observable<Item> getMatchingItems(String description) {
    return database.select("select * from items")
        .get(rs -> new Item(rs.getString(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5),
            Arrays.asList(rs.getString(6).split(delimiter)),
            Arrays.asList(rs.getString(7).split(delimiter))));
  }

  private String getJoined(List<String> strings) {
    String collect = strings.stream().collect(Collectors.joining(delimiter));
    return collect;
  }
}
