package org.dcn.aldous.query.services.query;

import com.google.inject.Inject;
import org.dcn.aldous.database.Description;
import org.dcn.aldous.database.DescriptionParser;
import org.dcn.aldous.database.Item;
import org.dcn.aldous.database.ItemsSearcher;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MatchingItemsService {

  private final ItemsSearcher repository;

  @Inject
  public MatchingItemsService(ItemsSearcher repository) {
    this.repository = repository;
  }

  public Map<String, Item> getMatchingItems(String description) {
    return repository.find(description).stream()
        .collect(toMap(Item::fullName, item -> item));
  }
}
