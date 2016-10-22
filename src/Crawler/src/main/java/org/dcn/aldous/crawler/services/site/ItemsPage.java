package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;

import java.util.List;

public interface ItemsPage {

  List<Item> extractItems();
}
