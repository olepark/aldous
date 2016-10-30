package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;

import java.util.List;

public interface SiteCrawler {

  CrawlerResponse status();

  List<Item> extractItems();
}
