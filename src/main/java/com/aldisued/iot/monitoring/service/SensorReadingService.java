package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

  private final AlertRepository alertRepository;
  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository, AlertRepository alertRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
    this.alertRepository = alertRepository;
  }

  @Transactional
  public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
    Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
        .orElseThrow(() -> new SensorNotFoundException(sensorReadingDto.sensorId()));
    SensorReading reading = sensorReadingRepository.save(new SensorReading(
        sensorReadingDto.value(), sensorReadingDto.timestamp(), sensor));

    String message = validateRange(sensorReadingDto, sensor);
    if (message != null) {
      alertRepository.save(new Alert(message, sensorReadingDto.timestamp(), sensor));
    }
    return reading;
  }

  private static @Nullable String validateRange(SensorReadingDto sensorReadingDto, Sensor sensor) {
    double value = sensorReadingDto.value();
    SensorType type = sensor.getType();
    String message = null;
    if (type == SensorType.TEMPERATURE && (value < -20.0 || value > 40.0)) {
      message = String.format(Locale.ROOT, "Temperature out of range: %s", value);
    } else if (type == SensorType.HUMIDITY && value > 90.0) {
      message = String.format(Locale.ROOT, "Humidity too high: %s", value);
    } else if (type == SensorType.ATMOSPHERIC_PRESSURE
        && (value < 950.0 || value > 1050.0)) {
      message = String.format(Locale.ROOT, "Pressure out of range: %s", value);
    }
    return message;
  }

}
