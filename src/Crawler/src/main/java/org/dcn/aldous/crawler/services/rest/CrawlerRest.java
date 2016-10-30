package org.dcn.aldous.crawler.services.rest;

import com.google.inject.Inject;
import org.dcn.aldous.crawler.services.site.CrawlerResponse;
import org.dcn.aldous.crawler.services.site.SitesCrawlerService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("api/1.0/crawler")
@Produces(MediaType.APPLICATION_JSON)
public class CrawlerRest {

  private final SitesCrawlerService crawlerService;

  @Inject
  public CrawlerRest(SitesCrawlerService crawlerService) {
    this.crawlerService = crawlerService;
  }

  @GET
  @Path("crawl")
  public CrawlerResponse crawl(@QueryParam("site") String siteUrl) {
    try {
      return crawlerService.startCrawling(siteUrl);
    } catch (RuntimeException ex) {
      return new CrawlerResponse(ex.getMessage(), -1);
    }
  }

  @GET
  @Path("info")
  public CrawlerResponse info(@QueryParam("jobId") Integer id) {
    return crawlerService.info(id);
  }

}
