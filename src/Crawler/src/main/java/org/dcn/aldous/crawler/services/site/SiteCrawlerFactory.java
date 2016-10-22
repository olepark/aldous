package org.dcn.aldous.crawler.services.site;

import java.util.Optional;

public class SiteCrawlerFactory {

  public Optional<SiteCrawler> supply(String url) {
    return Optional.empty(); //TODO implement logic for getting crawler for specific site
  }

}
