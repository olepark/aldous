package org.dcn.aldous.crawler.services.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.inject.Inject;
import lombok.Data;
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
      crawlerService.startCrawling(siteUrl);
      return new CrawlerResponse("Crawling started", 1);
    } catch (RuntimeException ex) {
      return new CrawlerResponse(ex.getMessage(), -1);
    }
  }

  @Data
  @JsonAutoDetect
  private class CrawlerResponse {

    private final String response;

    private final Integer jobId;

  }
}
