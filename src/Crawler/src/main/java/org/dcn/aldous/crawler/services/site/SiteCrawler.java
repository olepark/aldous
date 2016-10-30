package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface SiteCrawler{
  void crawlSite(List <String> catalogueUrls)throws IOException;
  List <Item> getItemsList();
  List <Item> itemList = new ArrayList();
}
