package com.project.weatherapp.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherConsumerService {

    @KafkaListener(topics = KafkaConfig.WEATHER_TOPIC, groupId = "weather-group")
    public void consume(String message) {
        log.info("Received weather update from Kafka: {}", message);
        // You could parse it and save to DB, etc.
    }
}
