package org.dcn.aldous.database;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class ItemsDAO {

  private final Set<Item> items;

  public Set<Item> getAllItems() {
    return items;
  }

  public void addItem(Item item) {
    items.add(item);
  }

}
