package org.dcn.aldous.crawler.services.site;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SiteCrawlerFactory {

  private final Map<String, Supplier<SiteCrawler>> crawlers;

  public Optional<SiteCrawler> supply(String key) {
    return Optional.ofNullable(crawlers.get(key)).map(Supplier::get);
  }

  public List<SiteCrawler> supplyAll() {
    return crawlers.values().stream().map(Supplier::get).collect(Collectors.toList());
  }
}
