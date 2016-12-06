package org.dcn.aldous.crawler.dependencies;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Provider;
import org.dcn.aldous.crawler.services.site.MVideoCrawler;
import org.dcn.aldous.crawler.services.site.SiteCrawler;
import org.dcn.aldous.crawler.services.site.SiteCrawlerFactory;
import org.dcn.aldous.crawler.services.site.UlmartCrawler;

import java.util.function.Supplier;

public class SiteCrawlerFactoryProvider implements Provider<SiteCrawlerFactory> {

  @Override
  public SiteCrawlerFactory get() {
    ImmutableMap<String, Supplier<SiteCrawler>> crawlers = ImmutableMap
        .<String, Supplier<SiteCrawler>>builder()
        .put("ulmart", UlmartCrawler::getUlmartCrawler)
        .put("mvideo", MVideoCrawler::getMVideoCrawler)
        .build();
    return new SiteCrawlerFactory(crawlers);
  }
}
