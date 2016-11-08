package org.dcn.aldous.crawler.services.site;

import java.util.Optional;

public class SiteCrawlerFactory {

  public Optional<SiteCrawler> supply(String url) {
    if (url.contains("ulmart")) {
      return Optional.of(UlmartCrawler.getUlmartCrawler());
    } else if (url.contains("mvideo")) {
      return Optional.of(MVideoCrawler.getMVideoCrawler());
    } else {
      return Optional.empty();
    }
  }

}
