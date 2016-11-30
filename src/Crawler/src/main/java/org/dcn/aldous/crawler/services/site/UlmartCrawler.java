package org.dcn.aldous.crawler.services.site;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.dcn.aldous.database.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Created by alexey on 29.10.16.
 */
@Slf4j
public class UlmartCrawler implements SiteCrawler {

  private static final String ULMART = "https://www.ulmart.ru";

  private final Subscription subscription = Subscription.create();

  private Set<String> catalogueUrls;

  @Override
  public String status() {
    return String.format("%s items extracted from %s", 42, ULMART);
  }

  @Override
  public Subscription subscription() {
    return subscription;
  }

  @Override
  public void crawlSite() {
    for (String catalogueUrl : catalogueUrls) {
      try {
        crawlCatalogue(catalogueUrl);
      } catch (IOException exp) {
        log.debug("This link doesn't exist: {}", catalogueUrl);
      }
    }
  }

  private void crawlCatalogue(String url) throws IOException {
    Document doc = Jsoup.connect(url + "&pageSize=10000").get();
    List<Element> elementsList = doc.select("[href~=/goods/?]");
    for (int i = 0; i < elementsList.size() / 2; i++) {
      Item item = parseItemPage(this.ULMART + elementsList.get(i * 2).attr("href"));
      subscription.broadcast(item);
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
    propertiesComplete.add("Description:" + description.text());
    Item item = new Item("default", vendorName, name, url, price, new ArrayList<>(), propertiesComplete);
    log.debug("Extracted new item: {}", item);
    return item;
  }

  private UlmartCrawler(Set<String> urls) {
    this.catalogueUrls = urls;
  }

  static public UlmartCrawler getUlmartCrawler() {
    //hardcoded urls
    Set<String> urls = Sets.newHashSet("https://www.ulmart.ru/catalog/15007481?sort=5&viewType=1&rec=true",
        "https://www.ulmart.ru/catalog/996333333333333333333325?sort=5&viewType=1&rec=true",
        "http://mxp.ulmart.ru/catalog/mxp_home?sort=5&viewType=1&rec=true",
        "https://www.ulmart.ru/catalog/15010148?sort=5&viewType=1&rec=true");
    return new UlmartCrawler(urls);
  }
}
