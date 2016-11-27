package org.dcn.aldous.crawler.services.site;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dcn.aldous.database.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by alexey on 29.10.16.
 */
@Slf4j
public class MVideoCrawler implements SiteCrawler {

  private static final String MVIDEO = "https://www.mvideo.ru";

  private Set<String> catalogueUrls;

  private Consumer<Item> itemConsumer;

  @Override
  public String status() {
    return String.format("%s items extracted from %s", 42, MVIDEO);
  }

  @Override
  @SneakyThrows
  public void extractAndConsume(Consumer<Item> itemConsumer) {
    this.itemConsumer = itemConsumer;
    crawlSite();
  }

  public void crawlSite() {
    for (String catalogueUrl : catalogueUrls) {
      try {
        crawlCatalogue(catalogueUrl);
      } catch (IOException exp) {
        log.debug("This link doesn't exist: {}", catalogueUrl + exp);
      }
    }
  }

  private void crawlCatalogue(String url) throws IOException {
    Document doc;
    int k = 1;
    while (true) {
      doc = Jsoup.connect(url + "/page=" + k).get();
      List<Element> elementsList = doc.getElementsByClass("product-tile-picture-holder");
      if (elementsList.isEmpty())
        break;
      for (int i = 0; i < elementsList.size(); i++) {
        parseItemPage(this.MVIDEO + elementsList.get(i).select("[href~=/products/?]").attr("href"));
      }
      k++;
    }
  }

  private String getElementAttribute(String name, String data) {
    String attribute = data.substring(data.indexOf(name) + name.length());
    attribute = attribute.substring(0, attribute.indexOf(','));
    return attribute;
  }

  private void parseItemPage(String url) throws IOException {
    Document doc;
    List<String> propertiesComplete = new ArrayList<>();

    doc = Jsoup.connect(url).get();
    List<Element> scriptElements = doc.select("script");
    String scriptText = scriptElements.toString();
    String params = scriptText.substring(scriptText.indexOf("\"params\": {") + 15);
    params = params.substring(0, params.indexOf('}'));

    int i = params.indexOf('"');
    while (i < params.length() && params.indexOf('"', i) != -1) {
      propertiesComplete.add(params.substring(params.indexOf('"', i), params.indexOf(",", i)).replace('"', '\0'));
      i = params.indexOf(",", i) + 1;
    }
    propertiesComplete.add("Price: " + getElementAttribute("\"price\": ", scriptText));
    propertiesComplete.add("Description: " + doc.getElementsByClass("pds-top-description").text());
    String vendorName = getElementAttribute("\'productVendorName\': ", scriptText).replace('\'', '\0');
    String name = getElementAttribute("\'productName\': ", scriptText).replace('\'', '\0');

    Item temp = new Item("default", vendorName, name, url, new ArrayList<>(), propertiesComplete);
    log.debug("Extracted new item: {}", temp);
    itemConsumer.accept(temp);
  }

  private MVideoCrawler(Set<String> urls) {
    this.catalogueUrls = urls;
  }

  static public MVideoCrawler getMVideoCrawler() {
    //hardcoded urls
    Set<String> urls = Sets.newHashSet("http://www.mvideo.ru/noutbuki-planshety-komputery/noutbuki-118/f/category=igrovye-noutbuki-3607",
        "http://www.mvideo.ru/krupnaya-kuhonnaya-tehnika/elektricheskie-plity-111",
        "http://www.mvideo.ru/telefony",
        "http://www.mvideo.ru/hi-fi-tehnika/akustika-hi-fi-192");
    return new MVideoCrawler(urls);
  }
}