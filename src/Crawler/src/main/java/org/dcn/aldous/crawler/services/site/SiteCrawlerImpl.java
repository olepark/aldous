package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;


/**
 * Created by alexey on 29.10.16.
 */
public class SiteCrawlerImpl implements SiteCrawler {
    @Override
    public List <Item> getItemsList(){
        return this.itemList;
    }

    @Override
    public void crawlSite(List <String> catalogueUrls)throws IOException {
        for(int i=0; i<catalogueUrls.size();i++){
            crawlCatalogue(catalogueUrls.get(i));
        }
    }

    private void crawlCatalogue(String url)throws IOException {
        Document doc = Jsoup.connect(url + "&pageSize=1000").get();
        List <Element> elementsList = doc.select("[href~=/goods/?]");
        for(int i=0; i<elementsList.size()/2; i++){
            this.itemList.add(parseItemPage(this.siteUrl+elementsList.get(i*2).attr("href").toString()));
            System.out.println(this.itemList.get(i));
        }
    }

    private String getElementAttribute(String name, String data) {
        String attribute = data.substring(data.indexOf(name)+name.length());
        attribute = attribute.substring(0, attribute.indexOf('\''));
        return attribute;
    }

    private Item parseItemPage(String url) throws IOException {
        Document doc;
        List<String> propertiesComplete = new ArrayList();
        List<String> itemUrl = new ArrayList<>();

        /*connect to the item page*/
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
        itemUrl.add(url);

        return new Item("default", vendorName, name, itemUrl, null, propertiesComplete);
    }

    private final String siteUrl = "https://www.ulmart.ru";
    private List <Item> itemList = new ArrayList();
}
