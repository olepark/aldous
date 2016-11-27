package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;

import java.util.function.Consumer;

public interface SiteCrawler {

  String status();
  void extractAndConsume(Consumer <Item> itemConsumer);
}
