package com.aldisued.iot.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MeasurementCalculatorAdditionalTests {
  private final MeasurementCalculatorService calculator = new MeasurementCalculatorService();

  @ParameterizedTest(name = "deviation = {0}")
  @NullSource
  @ValueSource(doubles = {-0.1, 1.1, Double.NaN})
  public void invalidDeviationIsIncludedInMessage(Double value) {
    IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
        () -> calculator.filterByAverageDeviation(List.of(), value));
    assertEquals(String.format("Deviation must be between %.1f and %.1f: %s", 0.0, 1.0, value),
        error.getMessage());
  }
}