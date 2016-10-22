package org.dcn.aldous.crawler.services.site;

import java.util.Iterator;

public interface SiteCrawler extends Iterator<ItemsPage> {

  String currentPage();
}
