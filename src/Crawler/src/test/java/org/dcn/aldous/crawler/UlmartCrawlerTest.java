package org.dcn.aldous.crawler;

import org.dcn.aldous.crawler.services.site.UlmartCrawler;
import org.dcn.aldous.database.Item;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Created by alexey on 29.10.16.
 */
public class UlmartCrawlerTest {
    @Test
    public void crawlSiteTestWrongUrl(){
        UlmartCrawler crawler = UlmartCrawler.getUlmartCrawler();
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(crawler::crawlSite);
    }
}
