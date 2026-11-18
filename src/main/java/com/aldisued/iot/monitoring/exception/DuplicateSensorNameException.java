package com.aldisued.iot.monitoring.exception;

public class DuplicateSensorNameException extends RuntimeException {
  public DuplicateSensorNameException(String name) {
    super(String.format("Sensor name already exists: %s", name));
  }
}