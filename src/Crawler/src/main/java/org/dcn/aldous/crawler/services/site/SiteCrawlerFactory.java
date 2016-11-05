package org.dcn.aldous.crawler.services.site;

import java.util.Optional;

public class SiteCrawlerFactory {

  public Optional<SiteCrawler> supply(String url) {
    if (url.contains("ulmart")) {
      return Optional.of(new UlmartCrawler());
    } else {
      return Optional.empty();
    }
  }

}
