package org.dcn.aldous.database;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemsSearcher {

  private final ItemsDAO items;

  public List<Item> find(String description) {
    DescriptionRater matcher = new DescriptionRater(description);
    return items.getMatchingItems(description)
        .map(item -> Pair.of(item, matcher.rate(item)))
        .toList()
        .map(pairs -> pairs.stream()
            .sorted(Comparator.comparing(Pair::getValue))
            .map(Pair::getKey)
            .collect(Collectors.toList()))
        .toBlocking().first();
  }

  private class DescriptionRater {

    private final String description;

    private final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

    private DescriptionRater(String description) {
      this.description = description;
    }


    public int rate(Item item) {
      int forName = compareString(item.name());
      int forVendor = compareString(item.vendor());
      int forProperties = compareList(item.properties());
      int forTags = compareList(item.tags());
      return forName + forVendor + forProperties + forTags;
    }

    private int compareList(List<String> itemList) {
      return itemList.stream()
          .mapToInt(this::compareString)
          .max()
          .orElse(-1 * description.length());
    }

    private int compareString(String si) {
      return si.length() - levenshteinDistance.apply(si, description);
    }
  }
}
