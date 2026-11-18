package com.aldisued.iot.monitoring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/sql/task-5-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class AlertRepositoryAdditionalTests extends IntegrationTestBase {
  @Autowired
  private AlertRepository alertRepository;

  @Test
  public void findsAlertByLongId() {
    assertEquals("Temperature alert 1", alertRepository.findById(1L).orElseThrow().getMessage());
    assertFalse(alertRepository.findById(999999L).isPresent());
  }
}