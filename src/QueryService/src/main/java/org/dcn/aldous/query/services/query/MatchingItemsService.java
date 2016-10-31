package org.dcn.aldous.query.services.query;

import com.google.inject.Inject;
import org.dcn.aldous.database.Item;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MatchingItemsService {

  private final DescriptionParser descriptionParser;

  private final ItemsSearcher repository;

  @Inject
  public MatchingItemsService(DescriptionParser descriptionParser, ItemsSearcher repository) {
    this.descriptionParser = descriptionParser;
    this.repository = repository;
  }

  public Map<String, Item> getMatchingItems(String itemDescription) {
    Description description = descriptionParser.parse(itemDescription);
    return repository.find(description).stream()
        .collect(toMap(Item::name, item -> item));
  }
}
