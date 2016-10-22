package org.dcn.aldous.crawler.services.site;

import lombok.AllArgsConstructor;
import org.dcn.aldous.database.ItemsDAO;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class SitesCrawlerService {

  private final SiteCrawlerFactory crawlerFactory;

  private final ItemsDAO itemsDAO;

  private final ExecutorService executorService;

  private final Map<Integer, SiteCrawler> crawlerJobs;

  private final AtomicInteger idGenerator;

  public CrawlerResponse startCrawling(String siteUrl) {
    Optional<SiteCrawler> crawlerOptional = crawlerFactory.supply(siteUrl);
    if (!crawlerOptional.isPresent()) {
      return new CrawlerResponse("Site not covered: " + siteUrl, -1);
    } else {
      SiteCrawler crawler = crawlerOptional.get();
      runAsync(crawler);
      int id = idGenerator.getAndIncrement();
      crawlerJobs.put(id, crawler);
      return new CrawlerResponse("Started crawling " + siteUrl, id);
    }
  }

  public CrawlerResponse info(Integer id) {
    if (crawlerJobs.containsKey(id)) {
      String currentPage = crawlerJobs.get(id).currentPage();
      return new CrawlerResponse("Current page: " + currentPage, id);
    }
    return new CrawlerResponse("Crawler with that id not found", id);
  }

  private void runAsync(SiteCrawler crawler) {
    executorService.execute(() -> {
      crawler.forEachRemaining(page -> {
        page.extractItems().forEach(itemsDAO::addItem);
      });
    });
  }
}
