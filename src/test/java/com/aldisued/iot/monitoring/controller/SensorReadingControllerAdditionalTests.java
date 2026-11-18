package com.aldisued.iot.monitoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

public class SensorReadingControllerAdditionalTests extends IntegrationTestBase {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private SensorReadingRepository readingRepository;

  @Test
  @Transactional
  public void knownSensorReturnsReading() throws Exception {
    Sensor sensor = sensorRepository.saveAndFlush(
        new Sensor("reading-test-" + UUID.randomUUID(), SensorType.TEMPERATURE));
    SensorReadingDto reading = new SensorReadingDto(
        sensor.getId(), 23.45, LocalDateTime.of(2025, 6, 1, 12, 0));

    mockMvc.perform(post("/sensor-readings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reading)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensor.id").value(sensor.getId().toString()))
        .andExpect(jsonPath("$.value").value(23.45));
  }

  @Test
  public void missingSensorReturnsExactMessageAndDoesNotSave() throws Exception {
    UUID missingId = UUID.randomUUID();
    assertFalse(sensorRepository.existsById(missingId));
    long before = readingRepository.count();
    SensorReadingDto reading = new SensorReadingDto(
        missingId, 23.45, LocalDateTime.of(2025, 6, 1, 12, 0));

    mockMvc.perform(post("/sensor-readings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reading)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(content().string("Sensor not found: " + missingId));
    assertEquals(before, readingRepository.count());
  }
}