package org.dcn.aldous.crawler;

import org.dcn.aldous.crawler.services.site.UlmartCrawler;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> crawler.crawlSite(urlList));
    }

    @Test
    public void crawlSiteTestReturnList() {
        UlmartCrawler crawler = new UlmartCrawler();
        crawler.crawlSite(newArrayList(
            "https://www.ulmart.ru/catalog/server_mb?sort=5&viewType=2&rec=true",
            "https://www.ulmart.ru/catalog/99862?sort=5&viewType=2&rec=true",
            "https://www.ulmart.ru/catalog/15021252?pageNum=1&pageSize=30&sort=5&viewType=2&rec=true"));
        assertThat(crawler.getItemsList()).isNotEmpty();
    }
}
