package org.dcn.aldous.database;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class ItemsSearcher {

  private final DescriptionParser descriptionParser;

  private final ItemsDAO items;

  public List<Item> find(String description) {
    DescriptionMatcher matcher = new DescriptionMatcher(descriptionParser.parse(description));
    return items.getMatchingItems(description)
        .map(item -> Pair.of(item, matcher.rate(item)))
        .filter(p -> p.getValue() > 0)
        .toList()
        .map(pairs -> pairs.stream()
            .sorted(Comparator.comparing(Pair::getValue))
            .map(Pair::getKey)
            .collect(Collectors.toList()))
        .toBlocking().first();
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
