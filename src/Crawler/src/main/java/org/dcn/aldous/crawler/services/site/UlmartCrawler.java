package org.dcn.aldous.crawler.services.site;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dcn.aldous.database.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Created by alexey on 29.10.16.
 */
@Slf4j
public class UlmartCrawler implements SiteCrawler {

  private static final String ULMART = "https://www.ulmart.ru";

  private List<Item> itemList = new ArrayList<>();

  @Override
  public void extractAndConsume(Consumer<Item> itemConsumer) {
    crawlSite(newArrayList(
        "https://www.ulmart.ru/catalog/server_mb?sort=5&viewType=2&rec=true",
        "https://www.ulmart.ru/catalog/99862?sort=5&viewType=2&rec=true",
        "https://www.ulmart.ru/catalog/15021252?pageNum=1&pageSize=30&sort=5&viewType=2&rec=true"));
    itemList.forEach(itemConsumer);
    itemList.clear();
  }

  @Override
  public String status() {
    return String.format("%s items extracted from %s", itemList.size(), ULMART);
  }

  public List<Item> getItemsList() {
    return this.itemList;
  }

  @SneakyThrows(IOException.class)
  public void crawlSite(List<String> catalogueUrls) {
    for (String catalogueUrl : catalogueUrls) {
      crawlCatalogue(catalogueUrl);
    }
  }

  private void crawlCatalogue(String url) throws IOException {
    Document doc = Jsoup.connect(url + "&pageSize=1000").get();
    List<Element> elementsList = doc.select("[href~=/goods/?]");
    for (int i = 0; i < elementsList.size() / 2; i++) {
      Item item = parseItemPage(this.ULMART + elementsList.get(i * 2).attr("href"));
      this.itemList.add(item);
      log.debug("Added item {}", item);
    }
  }

  private String getElementAttribute(String name, String data) {
    String attribute = data.substring(data.indexOf(name) + name.length());
    attribute = attribute.substring(0, attribute.indexOf('\''));
    return attribute;
  }

  private Item parseItemPage(String url) throws IOException {
    Document doc;
    List<String> propertiesComplete = new ArrayList<>();

    doc = Jsoup.connect(url).get();

    List<Element> propertiesName = doc.getElementsByClass("b-product-list__val");
    List<Element> propertiesValue = doc.getElementsByClass("b-product-list__prop");

    Element description = doc.getElementsByClass("b-details__description").first();

    List<Element> dataLayer = doc.select("script");
    String data = dataLayer.toString();
    String name = getElementAttribute("'productName': '", data);
    String vendorName = getElementAttribute("'vendorName': '", data);
    String price = getElementAttribute("'productPrice': '", data);


    for (int i = 0; i < propertiesName.size(); i++) {
      propertiesComplete.add(propertiesName.get(i).text() + ":" + propertiesValue.get(i).text());
    }
    propertiesComplete.add("Price:" + price);
    propertiesComplete.add("Description:" + description.text());

    return new Item("default", vendorName, name, url, new ArrayList<>(), propertiesComplete);
  }
}
