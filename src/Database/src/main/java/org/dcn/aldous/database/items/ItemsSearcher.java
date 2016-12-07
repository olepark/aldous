package org.dcn.aldous.database.items;

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
            .sorted(Comparator.comparingDouble(Pair::getValue))
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


    public double rate(Item item) {
      double forName = compareString(item.name());
      double forVendor = compareString(item.vendor());
//      int forProperties = compareList(item.properties());
      double forTags = compareList(item.tags());
      return forName + forVendor + forTags;
    }

    private double compareList(List<String> itemList) {
      return itemList.stream()
          .mapToDouble(this::compareString)
          .max()
          .orElse(-1 * description.length());
    }

    private double compareString(String si) {
      return (si.length() - levenshteinDistance.apply(si, description)) * 1.0 / si.length();
    }
  }
}
