package org.dcn.aldous.crawler;

import org.dcn.aldous.crawler.services.site.UlmartCrawler;
import org.junit.Test;

import java.util.ArrayList;

import static org.jsoup.helper.Validate.fail;

/**
 * Created by alexey on 29.10.16.
 */
public class UlmartCrawlerTest {

    @Test
    public void crawlSiteTestWrongUrl(){
        UlmartCrawler crawler = new UlmartCrawler();
        ArrayList <String> urlList = new ArrayList<>();
        urlList.add("insanePieceOfShit.com");
        try{
            crawler.crawlSite(urlList);
        } catch (Throwable e){
            if(e.getClass()!=IllegalArgumentException.class)
                fail("Not expected exception");
        }
        System.out.println("Everything is ok");
    }

    @Test
    public void crawlSiteTestReturnList(){
        UlmartCrawler crawler = new UlmartCrawler();
        ArrayList <String> urlList = new ArrayList<>();
        urlList.add("https://www.ulmart.ru/catalog/server_mb?sort=5&viewType=2&rec=true");
        urlList.add("https://www.ulmart.ru/catalog/99862?sort=5&viewType=2&rec=true");
        urlList.add("https://www.ulmart.ru/catalog/15021252?pageNum=1&pageSize=30&sort=5&viewType=2&rec=true");
        try{
            crawler.crawlSite(urlList);
            if(crawler.getItemsList().isEmpty() && urlList.size()!=0)
                fail("Crawler got nothing from the site");
            else
                System.out.println(crawler.getItemsList());
        } catch (Throwable e){
            fail("Not expected exception");
        }

    }
}
