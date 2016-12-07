package org.dcn.aldous.crawler.services.site;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.dcn.aldous.database.items.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by alexey on 29.10.16.
 */
@Slf4j
public class MVideoCrawler implements SiteCrawler {

  private static final String MVIDEO = "https://www.mvideo.ru";

  private Set<String> catalogueUrls;

  private final Subscription subscription = Subscription.create();

  private long numberOfExtractredItems;

  @Override
  public String status() {
    return String.format("%s items extracted from %s", numberOfExtractredItems, MVIDEO);
  }

  @Override
  public Subscription subscription() {
    return subscription;
  }

  @Override
  public void crawlSite() {
    this.numberOfExtractredItems = 0;
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
        Item item = parseItemPage(this.MVIDEO + elementsList.get(i).select("[href~=/products/?]").attr("href"));
        subscription.broadcast(item);
      }
      k++;
    }
  }

  private String getElementAttribute(String name, String data) {
    String attribute = data.substring(data.indexOf(name) + name.length());
    attribute = attribute.substring(0, attribute.indexOf(','));
    return attribute;
  }

  private Item parseItemPage(String url) throws IOException {
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
    String price = getElementAttribute("\"price\": ", scriptText);
    propertiesComplete.add("Description: " + doc.getElementsByClass("pds-top-description").text());
    String vendorName = getElementAttribute("\'productVendorName\': ", scriptText).replace('\'', '\0');
    String name = getElementAttribute("\'productName\': ", scriptText).replace('\'', '\0');

    Item temp = new Item(null, vendorName, name, url, price, new ArrayList<>(), propertiesComplete);
    log.debug("Extracted new item: {}", temp);
    this.numberOfExtractredItems++;
    return temp;
  }

  private MVideoCrawler(Set<String> urls) {
    this.catalogueUrls = urls;
    this.numberOfExtractredItems = 0;
  }

  static public MVideoCrawler getMVideoCrawler() {
    //hardcoded urls
    Set<String> urls = Sets.newHashSet(
            "http://www.mvideo.ru/noutbuki-planshety-komputery/noutbuki-118/f/category=igrovye-noutbuki-3607",
            "http://www.mvideo.ru/krupnaya-kuhonnaya-tehnika/elektricheskie-plity-111",
            "http://www.mvideo.ru/hi-fi-tehnika/akustika-hi-fi-192",
            "http://www.mvideo.ru/smartfony-i-svyaz/smartfony-205",
            "http://www.mvideo.ru/televizory-i-cifrovoe-tv/televizory-65",
            "http://www.mvideo.ru/audiotehnika/muzykalnye-centry-68",
            "http://www.mvideo.ru/naushniki/naushniki-3967",
            "http://www.mvideo.ru/portativnoe-audio/ipod-166",
            "http://www.mvideo.ru/portativnoe-audio/mp3-pleery-72",
            "http://www.mvideo.ru/noutbuki-planshety-komputery/noutbuki-118",
            "http://www.mvideo.ru/komputernaya-tehnika/sistemnye-bloki-80",
            "http://www.mvideo.ru/noutbuki-planshety-komputery/planshety-195",
            "http://www.mvideo.ru/fotoapparaty/fotoapparaty-sistemnye-196",
            "http://www.mvideo.ru/fotoapparaty/zerkalnye-fotoapparaty-169",
            "http://www.mvideo.ru/videokamery/ekshn-kamery-2288",
            "http://www.mvideo.ru/kuhonnaya-tehnika/elektrochainiki-96",
            "http://www.mvideo.ru/kuhonnaya-tehnika/multivarki-180",
            "http://www.mvideo.ru/kuhonnaya-tehnika/miksery-i-blendery-98",
            "http://www.mvideo.ru/kuhonnaya-tehnika/myasorubki-104",
            "http://www.mvideo.ru/prigotovlenie-kofe/kofemashiny-155",
            "http://www.mvideo.ru/prigotovlenie-kofe/kofevarki-157",
            "http://www.mvideo.ru/malaya-bytovaya-tehnika/utugi-92/f/category=parogenerator-s-boilerom-736",
            "http://www.mvideo.ru/malaya-bytovaya-tehnika/utugi-92",
            "http://www.mvideo.ru/malaya-bytovaya-tehnika/shveinye-mashiny-134",
            "http://www.mvideo.ru/komputernye-aksessuary/routery-i-setevoe-oborudovanie-186",
            "http://www.mvideo.ru/komputernye-aksessuary/sumki-dlya-noutbukov-216",
            "http://www.mvideo.ru/komputernye-aksessuary/myshi-183",
            "http://www.mvideo.ru/komputernye-aksessuary/komputernye-kolonki-182"
    );
    return new MVideoCrawler(urls);
  }
}