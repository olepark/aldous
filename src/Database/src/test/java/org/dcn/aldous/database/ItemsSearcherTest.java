package org.dcn.aldous.database;

import org.dcn.aldous.database.Description;
import org.dcn.aldous.database.Item;
import org.dcn.aldous.database.ItemsDAO;
import org.dcn.aldous.database.ItemsSearcher;
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
        new Item("001", "Samsung", "Galaxy S5", "some-url", "", newArrayList("smartphone"), newArrayList("10 inches")),
        new Item("002", "Apple", "Iphone 6", "some-url", "", newArrayList("smartphone"), newArrayList("5 inches")),
        new Item("003", "Apple", "Iphone 5", "some-url", "", newArrayList("smartphone"), newArrayList("4 inches")),
        new Item("004", "Bogatyr'", "Socks", "some-url", "", newArrayList("socks", "men's"), newArrayList("size 41"))
    );
    repository = new ItemsSearcher(new DescriptionParser(), new ItemsDAO(null));
  }

  @Test
  public void testIphone() {
    Description description = new Description(
        "phone Apple Iphone",
        "Apple",
        "Iphone",
        newArrayList("phone"),
        newArrayList());
    List<Item> result = repository.find("phone Apple Iphone");
    assertThat(result).hasSize(2);
    assertThat(result).allMatch(i -> i.vendor().equals("Apple"));
  }

  @Test
  public void testSmartPhone() {
    Description description = new Description(
        "smartphone",
        "",
        "",
        newArrayList("smartphone"),
        newArrayList());
    List<Item> result = repository.find("smartphone");
    assertThat(result).hasSize(3);
    assertThat(result).allMatch(i -> !i.vendor().equals("Bogatyr'"));
  }

  @Test
  public void testSocks() {
    Description description = new Description(
        "socks",
        "",
        "",
        newArrayList("socks"),
        newArrayList());
    List<Item> result = repository.find("socks");
    assertThat(result).hasSize(1);
    assertThat(result).allMatch(i -> i.vendor().equals("Bogatyr'"));
  }
}