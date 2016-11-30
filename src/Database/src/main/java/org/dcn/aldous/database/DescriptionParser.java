package org.dcn.aldous.database;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toMap;

public class DescriptionParser {

  private static final String LIST_DELIMITER = ";";

  public Description parse(String itemDescription) {
    Map<ItemProperty, String> properties = newArrayList(itemDescription.split(" ")).stream()
        .map(word -> Pair.of(tag(word), word))
        .collect(toMap(Pair::getKey, Pair::getValue,
            (w1, w2) -> w1 + LIST_DELIMITER + w2, () -> Maps.newEnumMap(ItemProperty.class)));
    return new Description(itemDescription,
        properties.getOrDefault(ItemProperty.VENDOR, ""),
        properties.getOrDefault(ItemProperty.NAME, ""),
        asList(properties.getOrDefault(ItemProperty.TAGS, "")),
        asList(properties.getOrDefault(ItemProperty.PROPERTIES, "")));
  }

  private List<String> asList(String s) {
    if (s.isEmpty()) {
      return newArrayList();
    }
    return newArrayList(s.split(LIST_DELIMITER));
  }

  private ItemProperty tag(String word) { //TODO replace with NER
    if (word.matches("[A-Z][a-z]*.")) {
      return ItemProperty.VENDOR;
    } else if (word.matches("[A-Z]*.")) {
      return ItemProperty.NAME;
    } else if (word.matches("[a-z]*.")) {
      return ItemProperty.TAGS;
    } else if (word.matches(".[0-9]*.")) {
      return ItemProperty.PROPERTIES;
    } else {
      return ItemProperty.NA;
    }
  }

  private enum  ItemProperty {
    VENDOR, NAME, TAGS, PROPERTIES, NA
  }
}
