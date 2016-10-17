package org.dcn.aldous.query.services.query;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ItemsRepository {

  private final Set<Item> items;

  public ItemsRepository(Set<Item> items) {
    this.items = items;
  }

  public List<Item> find(Description description) {
    DescriptionMatcher matcher = new DescriptionMatcher(description);
    return items.stream()
        .map(item -> Pair.of(item, matcher.rate(item)))
        .filter(p -> p.getValue() > 0)
        .sorted(Comparator.comparingInt(Pair::getValue))
        .map(Pair::getKey)
        .collect(toList());
  }

  private class DescriptionMatcher {

    private final Description description;

    private final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

    public DescriptionMatcher(Description description) {
      this.description = description;
    }

    public int rate(Item item) {
      int forName = compareString(item.name(), description.name());
      int forVendor = compareString(item.vendor(), description.vendor());
      int forProperties = compareLists(item.properties(), description.properties());
      int forTags = compareLists(item.tags(), description.tags());
      return forName + forVendor + forProperties + forTags;
    }

    private int compareLists(List<String> itemList, List<String> descList) {
      return descList.stream()
          .mapToInt(property -> itemList.stream().mapToInt(dProp -> compareString(property, dProp))
              .max().orElse(-1 * descList.size()))
          .sum();
    }

    private int compareString(String si, String sd) {
      if (sd.isEmpty()) {
        return 0;
      }
      return (si.length() + sd.length()) / 2 - levenshteinDistance.apply(si, sd);
    }
  }
}
