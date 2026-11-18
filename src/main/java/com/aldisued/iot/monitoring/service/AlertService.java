package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public Alert saveAlert(AlertDto alertDto) {
    Sensor sensor = sensorRepository.findById(alertDto.sensorId())
        .orElseThrow(() -> new SensorNotFoundException(alertDto.sensorId()));
    Alert saved = alertRepository.save(
        new Alert(alertDto.message(), alertDto.timestamp(), sensor));
    kafkaTemplate.send("alerts", alertDto);
    return saved;
  }

  public AlertDto findLastAlertBySensorId(UUID sensorId) {
    Alert alert = alertRepository.findFirstBySensor_IdOrderByTimestampDesc(sensorId)
        .orElse(null);
    if (alert == null) {
      return null;
    }
    return new AlertDto(sensorId, alert.getMessage(), alert.getTimestamp());
  }
}
