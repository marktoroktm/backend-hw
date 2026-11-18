package com.aldisued.iot.monitoring.controller;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.exception.DuplicateSensorNameException;
import com.aldisued.iot.monitoring.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensors")
public class SensorController {

  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @PostMapping
  public ResponseEntity<?> saveSensor(@Valid @RequestBody SensorDto sensorDto) {
    try {
      return ResponseEntity.ok(sensorService.saveSensor(sensorDto));
    } catch (DuplicateSensorNameException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .contentType(MediaType.TEXT_PLAIN)
          .body(ex.getMessage());
    }
  }
}