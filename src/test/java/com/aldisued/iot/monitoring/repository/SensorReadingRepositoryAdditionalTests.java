package com.aldisued.iot.monitoring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/sql/task-7-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class SensorReadingRepositoryAdditionalTests extends IntegrationTestBase {
  @Autowired
  private SensorReadingRepository readingRepository;

  @Test
  public void findsReadingByLongId() {
    assertEquals(15.0, readingRepository.findById(1L).orElseThrow().getValue().doubleValue());
    assertFalse(readingRepository.findById(999999L).isPresent());
  }
}