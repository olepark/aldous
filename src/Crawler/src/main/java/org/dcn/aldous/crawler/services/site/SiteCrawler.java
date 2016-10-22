package org.dcn.aldous.crawler.services.site;

import java.util.Iterator;

public class SiteCrawler implements Iterator<ItemsPage> {

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public ItemsPage next() {
    return null;
  }

  public String currentPage() {
    return null;
  }
}
