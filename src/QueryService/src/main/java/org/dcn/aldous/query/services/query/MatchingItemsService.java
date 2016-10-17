package org.dcn.aldous.query.services.query;

import com.google.inject.Inject;

import java.util.List;

public class MatchingItemsService {

  private final DescriptionParser descriptionParser;

  private final ItemsRepository repository;

  @Inject
  public MatchingItemsService(DescriptionParser descriptionParser, ItemsRepository repository) {
    this.descriptionParser = descriptionParser;
    this.repository = repository;
  }

  public List<Item> getMatchingItems(String itemDescription) {
    Description description = descriptionParser.parse(itemDescription);
    return repository.find(description);
  }
}
