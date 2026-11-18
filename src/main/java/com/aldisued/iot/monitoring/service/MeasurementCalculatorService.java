package com.aldisued.iot.monitoring.service;

import java.util.ArrayList;
import java.util.List;

public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    double minimumDeviation = 0.0;
    double maximumDeviation = 1.0;
    if (deviation == null || Double.isNaN(deviation) || deviation < minimumDeviation || deviation > maximumDeviation) {
      throw new IllegalArgumentException(
          String.format("Deviation must be between %.1f and %.1f: %s", minimumDeviation, maximumDeviation, deviation));
    }
    if (values.isEmpty()) {
      return List.of();
    }
    double sum = 0.0;
    for (Double value : values) {
      sum += value;
    }
    double average = sum / values.size();
    double allowed = Math.abs(average) * deviation;
    List<Double> result = new ArrayList<>();
    for (Double value : values) {
      if (Math.abs(value - average) <= allowed) {
        result.add(value);
      }
    }
    return result;
  }

}
