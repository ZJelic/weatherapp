package com.project.weatherapp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendWeatherUpdate(String message) {
        log.info("Sending weather update to Kafka: {}", message);
        kafkaTemplate.send(KafkaConfig.WEATHER_TOPIC, message);
    }
}
