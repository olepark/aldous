package org.dcn.aldous.crawler;

import com.google.common.io.Resources;
import org.dcn.aldous.crawler.services.site.SiteCrawlerImpl;
/**
 * Created by alexey on 29.10.16.
 */
public class SiteCrawlerImplTest {
    public static void main(String[] args) throws Exception {
        SiteCrawlerImpl crawlerEpt = new SiteCrawlerImpl();
        //crawlerEpt.parseItemPage("https://www.ulmart.ru/goods/3995231");
        crawlerEpt.crawlCatalogue("https://www.ulmart.ru/catalog/mobiles?sort=3&viewType=2&rec=true", "testDefault");
        //crawlerEpt.buildSiteGraph(0);
    }
}
