package com.project.weatherapp.kafka;

import com.project.weatherapp.dto.WeatherUpdateMessage;
import com.project.weatherapp.entity.WeatherUpdate;
import com.project.weatherapp.repository.WeatherUpdateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherConsumerService {

    private final WeatherUpdateRepository repository;

    @KafkaListener(
            topics = KafkaConfig.WEATHER_TOPIC,
            groupId = "weather-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(WeatherUpdateMessage message) {

        log.info("Consumed weather update: {}", message);

        WeatherUpdate entity = WeatherUpdate.builder()
                .stationId(message.stationId())
                .temperature(message.temperature())
                .windSpeed(message.windSpeed())
                .timestamp(message.timestamp())
                .build();

        repository.save(entity);

        log.info("Weather update persisted for station {}", message.stationId());
    }
}
