package org.dcn.aldous.app.services.rest;

import com.codahale.metrics.health.HealthCheck;

public class ManagerHealthCheck extends HealthCheck {

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
