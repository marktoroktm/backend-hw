package com.aldisued.iot.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MeasurementServiceAdditionalTests {
  @Test
  public void measurementLookupReturnsNullWhenNoValuesMatch() {
    SensorReadingRepository readings = mock(SensorReadingRepository.class);
    MeasurementService service = new MeasurementService(readings);
    LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0);
    LocalDateTime to = LocalDateTime.of(2020, 12, 31, 23, 59);
    when(readings.findValuesByTypeAndTimeRange(SensorType.TEMPERATURE, from, to))
        .thenReturn(List.of());

    assertNull(service.getMeasurementValuesBySensorType(SensorType.TEMPERATURE, from, to));
  }

  @Test
  public void measurementLookupReturnsMatchingValues() {
    SensorReadingRepository readings = mock(SensorReadingRepository.class);
    MeasurementService service = new MeasurementService(readings);
    LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0);
    LocalDateTime to = LocalDateTime.of(2020, 12, 31, 23, 59);
    List<Double> values = List.of(15.8, 16.1);
    when(readings.findValuesByTypeAndTimeRange(SensorType.TEMPERATURE, from, to))
        .thenReturn(values);

    assertSame(values, service.getMeasurementValuesBySensorType(SensorType.TEMPERATURE, from, to));
  }
}