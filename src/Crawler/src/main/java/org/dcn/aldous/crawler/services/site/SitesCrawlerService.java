package org.dcn.aldous.crawler.services.site;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.ItemsDAO;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class SitesCrawlerService {

  private final SiteCrawlerFactory crawlerFactory;

  private final ItemsDAO itemsDAO;

  private final Executor executor;

  private final Map<Integer, SiteCrawler> crawlerJobs;

  private final AtomicInteger idGenerator;

  public void crawlAll() {
    crawlerFactory.supplyAll().stream()
        .forEach(this::runJob);
  }

  public CrawlerResponse startCrawling(String siteUrl) {
    Optional<SiteCrawler> crawler = crawlerFactory.supply(siteUrl);
    if (!crawler.isPresent()) {
      return new CrawlerResponse("Site not covered: " + siteUrl, -1);
    } else {
      int id = runJob(crawler.get());
      return new CrawlerResponse("Started crawling " + siteUrl, id);
    }
  }

  private int runJob(SiteCrawler crawler) {
    runAsync(crawler);
    int id = idGenerator.getAndIncrement();
    crawlerJobs.put(id, crawler);
    return id;
  }

  public CrawlerResponse info(Integer id) {
    if (crawlerJobs.containsKey(id)) {
      String status = crawlerJobs.get(id).status();
      return new CrawlerResponse(status, id);
    }
    return new CrawlerResponse("Crawler with that id not found", id);
  }

  private void runAsync(SiteCrawler crawler) {
    executor.execute(() -> {
      Subscription subscription = crawler.subscription();
      subscription.addSubscriber(itemsDAO::addItem);
      crawler.crawlSite();
    });
  }

  public Map<Integer, String> jobs() {
    return Maps.transformValues(crawlerJobs, Object::toString);
  }
}
