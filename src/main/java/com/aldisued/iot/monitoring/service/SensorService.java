package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.DuplicateSensorNameException;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public Sensor saveSensor(SensorDto sensor) {
    if (sensorRepository.existsByName(sensor.name())) {
      throw new DuplicateSensorNameException(sensor.name());
    }
    return sensorRepository.save(new Sensor(sensor.name(), sensor.type()));
  }
}
