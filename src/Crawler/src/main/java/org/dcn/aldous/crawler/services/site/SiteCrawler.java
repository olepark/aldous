package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;

public interface SiteCrawler{
  void crawlSite()throws IOException;
  void crawlCatalogue(String url, String catalogueName)throws IOException;
  Item parseItemPage(String url)throws IOException;
  void buildSiteGraph(int k)throws IOException;
}
