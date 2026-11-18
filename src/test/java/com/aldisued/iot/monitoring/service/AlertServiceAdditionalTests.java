package com.aldisued.iot.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class AlertServiceAdditionalTests {
  @Mock
  private KafkaTemplate<String, AlertDto> kafkaTemplate;

  @Test
  public void missingSensorDoesNotSaveOrPublish() throws Exception {
    try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
      AlertRepository alerts = mock(AlertRepository.class);
      SensorRepository sensors = mock(SensorRepository.class);
      AlertService service = new AlertService(alerts, sensors, kafkaTemplate);
      UUID missingId = UUID.randomUUID();
      when(sensors.findById(missingId)).thenReturn(Optional.empty());
      AlertDto alert = new AlertDto(missingId, "test", LocalDateTime.of(2025, 6, 1, 12, 0));

      SensorNotFoundException error = assertThrows(SensorNotFoundException.class,
          () -> service.saveAlert(alert));
      assertEquals("Sensor not found: " + missingId, error.getMessage());
      verifyNoInteractions(alerts, kafkaTemplate);
    }
  }
}