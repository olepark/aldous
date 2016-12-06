package org.dcn.aldous.crawler.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.dcn.aldous.crawler.services.site.SiteCrawlerFactory;
import org.dcn.aldous.crawler.services.site.SitesCrawlerService;
import org.dcn.aldous.database.ItemsDAO;
import org.dcn.aldous.providers.ConfigProvider;
import org.dcn.aldous.database.providers.ItemsDAOProvider;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Maps.newHashMap;

public class CrawlerGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ConfigProvider.class);
    bind(ItemsDAO.class).toProvider(ItemsDAOProvider.class).in(Singleton.class);
    bind(SiteCrawlerFactory.class).toProvider(SiteCrawlerFactoryProvider.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  SitesCrawlerService sitesCrawlerService(ItemsDAO itemsDAO, SiteCrawlerFactory factory) {
    ExecutorThreadPool threadPool = new ExecutorThreadPool(4, 100, 10, TimeUnit.SECONDS);
    return new SitesCrawlerService(factory, itemsDAO, threadPool, newHashMap(), new AtomicInteger());
  }
}
