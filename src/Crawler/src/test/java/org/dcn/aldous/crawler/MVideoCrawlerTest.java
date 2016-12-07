package org.dcn.aldous.crawler;

import org.dcn.aldous.crawler.services.site.MVideoCrawler;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


/**
 * Created by alexey on 29.10.16.
 */
public class MVideoCrawlerTest {
    @Test
    public void crawlSiteTestWrongUrl(){
        MVideoCrawler crawler = MVideoCrawler.getMVideoCrawler();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(crawler::crawlSite);
    }
}
