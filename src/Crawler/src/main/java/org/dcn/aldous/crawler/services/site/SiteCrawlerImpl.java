package org.dcn.aldous.crawler.services.site;

import org.dcn.aldous.database.Item;
import org.jsoup.HttpStatusException;
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
    private class TreeNode{
        public TreeNode(String catalogueUrl, String catalogueName) {
            this.parent = null;
            this.children = null;
            this.catalogueUrl = catalogueUrl;
            this.catalogueName = catalogueName;
        }

        public TreeNode parent;
        public List<TreeNode> children;
        public String catalogueUrl;
        public String catalogueName;
    }


    @Override
    public void crawlSite()throws IOException {

    }

    @Override
    public void crawlCatalogue(String url, String catalogueName)throws IOException {
        Document doc = Jsoup.connect(url + "&pageSize=1000").get();
        List <Element> elementsList = doc.select("[href~=/goods/?]");

        for(int i=0;i<elementsList.size()/2;i++){
            itemList.add(parseItemPage(siteUrl+elementsList.get(i*2).attr("href").toString()));
            //System.out.println(itemList.get(i));
        }

        //System.out.println("В базе данных "+itemList.size()+" записей.");

    }

    @Override
    public Item parseItemPage(String url) throws IOException{
        Document doc;
        List <String> propertiesComplete= new ArrayList();
        List <String> itemUrl = new ArrayList<>();

        /*connect to the item page*/
        doc = Jsoup.connect(url).get();

        /*get properties names and values*/
        List <Element> propertiesName = doc.getElementsByClass("b-product-list__val");
        List <Element> propertiesValue = doc.getElementsByClass("b-product-list__prop");

        /*get description*/
        Element description = doc.getElementsByClass("b-details__description").first();


        List <Element> dataLayer = doc.select("script");
        String data = dataLayer.toString();
        String temp;

        /*get name of the item*/
        temp = "'productName': '";
        String name = data.substring(data.indexOf(temp)+temp.length());
        name = name.substring(0, name.indexOf('\''));

        /*get the vendor name*/
        temp = "'vendorName': '";
        String vendorName = data.substring(data.indexOf(temp)+temp.length());
        vendorName = vendorName.substring(0, vendorName.indexOf('\''));

        /*get the price*/
        temp = "'productPrice': '";
        String price = data.substring(data.indexOf(temp)+temp.length());
        price = price.substring(0, price.indexOf('\''));


        for(int i=0;i<propertiesName.size();i++){
            propertiesComplete.add(propertiesName.get(i).text()+":"+propertiesValue.get(i).text());
        }
        propertiesComplete.add("Price:"+price);
        propertiesComplete.add("Description:"+description.text());
        itemUrl.add(url);

        //System.out.println(new Item("default", vendorName, name, itemUrl, null, propertiesComplete));
        return new Item("default", vendorName, name, itemUrl, null, propertiesComplete);
    }

    @Override
    public void buildSiteGraph(int k) throws IOException{
        Document doc;
        /* полный рак мозга
        for(int i=k; i<99999999; i++){
            try {
                doc = Jsoup.connect(siteUrl+"/catalog/"+i).get();
                System.out.println("rabotaet: "+siteUrl+"/catalog/"+i);
            } catch (HttpStatusException e){
                System.out.println("ne rabotaet: "+siteUrl+"/catalog/"+i);
                buildSiteGraph(i+1);
            }
        }*/

    }

    private final String siteUrl = "https://www.ulmart.ru";
    private TreeNode root = new TreeNode("https://www.ulmart.ru/", "Начальная страница");
    private List <Item> itemList = new ArrayList();
}
