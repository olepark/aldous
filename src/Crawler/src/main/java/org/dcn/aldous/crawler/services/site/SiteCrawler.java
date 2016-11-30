package org.dcn.aldous.crawler.services.site;

public interface SiteCrawler {

  String status();

  Subscription subscription();

  void crawlSite();
}
