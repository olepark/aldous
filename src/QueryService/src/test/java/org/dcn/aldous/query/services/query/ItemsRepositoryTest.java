package org.dcn.aldous.query.services.query;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class ItemsRepositoryTest {

  @Test
  public void testFind() {
    Set<Item> items = newHashSet(
        new Item("001", "Samsung", "Galaxy S5", newArrayList(""), newArrayList("smartphone"), newArrayList("10 inches")),
        new Item("002", "Apple", "Iphone 6", newArrayList(""), newArrayList("smartphone"), newArrayList("5 inches")),
        new Item("003", "Apple", "Iphone 5", newArrayList(""), newArrayList("smartphone"), newArrayList("4 inches")),
        new Item("004", "Bogatyr'", "Socks", newArrayList(""), newArrayList("socks", "men's"), newArrayList("size 41"))
    );
    ItemsRepository repository = new ItemsRepository(items);
    Description description = new Description(
        "phone Apple Iphone",
        "Apple",
        "Iphone",
        newArrayList("phone"),
        newArrayList());
    List<Item> result = repository.find(description);
  }
}