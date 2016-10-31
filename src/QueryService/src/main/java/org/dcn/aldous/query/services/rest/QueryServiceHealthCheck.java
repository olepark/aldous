package org.dcn.aldous.query.services.rest;

import com.codahale.metrics.health.HealthCheck;

public class QueryServiceHealthCheck extends HealthCheck {

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
