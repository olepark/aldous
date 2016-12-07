package org.dcn.aldous.database;

import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.items.ItemsSearcher;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemsSearcherTest {

  private ItemsSearcher repository;

  @Before
  public void setUp() {
    Set<Item> items = newHashSet(
        new Item(0, "Samsung", "Galaxy S5", "some-url", "", newArrayList("smartphone"), newArrayList("10 inches")),
        new Item(1, "Apple", "Iphone 6", "some-url", "", newArrayList("smartphone"), newArrayList("5 inches")),
        new Item(2, "Apple", "Iphone 5", "some-url", "", newArrayList("smartphone"), newArrayList("4 inches")),
        new Item(3, "Bogatyr'", "Socks", "some-url", "", newArrayList("socks", "men's"), newArrayList("size 41"))
    );
    repository = new ItemsSearcher(new ItemsDAO(null));
  }

  @Test
  public void testIphone() {
    List<Item> result = repository.find("phone Apple Iphone");
    assertThat(result).hasSize(2);
    assertThat(result).allMatch(i -> i.vendor().equals("Apple"));
  }

  @Test
  public void testSmartPhone() {
    List<Item> result = repository.find("smartphone");
    assertThat(result).hasSize(3);
    assertThat(result).allMatch(i -> !i.vendor().equals("Bogatyr'"));
  }

  @Test
  public void testSocks() {
    List<Item> result = repository.find("socks");
    assertThat(result).hasSize(1);
    assertThat(result).allMatch(i -> i.vendor().equals("Bogatyr'"));
  }
}