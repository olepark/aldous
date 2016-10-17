package org.dcn.aldous.crawler.services.rest;

import com.codahale.metrics.health.HealthCheck;

public class CrawlerHealthCheck extends HealthCheck {

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
