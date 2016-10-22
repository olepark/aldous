package org.dcn.aldous.crawler.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.dcn.aldous.crawler.services.site.SiteCrawlerFactory;
import org.dcn.aldous.crawler.services.site.SitesCrawlerService;
import org.dcn.aldous.database.ItemsDAO;
import org.dcn.aldous.providers.ConfigProvider;
import org.dcn.aldous.providers.ItemsDAOProvider;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Maps.newHashMap;

public class CrawlerGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ConfigProvider.class);
    bind(ItemsDAO.class).toProvider(ItemsDAOProvider.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  SitesCrawlerService sitesCrawlerService(ItemsDAO itemsDAO) {
    QueuedThreadPool threadPool = new QueuedThreadPool();
    return new SitesCrawlerService(new SiteCrawlerFactory(), itemsDAO, threadPool, newHashMap(), new AtomicInteger());
  }
}
