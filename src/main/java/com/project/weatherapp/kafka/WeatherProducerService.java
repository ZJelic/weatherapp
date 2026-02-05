package com.project.weatherapp.kafka;

import com.project.weatherapp.dto.WeatherUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherProducerService {

    private final KafkaTemplate<String, WeatherUpdateMessage> kafkaTemplate;

    public void sendWeatherUpdate(WeatherUpdateMessage message) {
        log.info("Sending weather update to Kafka: {}", message);
        kafkaTemplate.send(KafkaConfig.WEATHER_TOPIC, message);
    }
}
