package com.project.weatherapp.kafka;

import com.project.weatherapp.dto.WeatherUpdateMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String WEATHER_TOPIC = "weather-updates";

    // PRODUCER
    @Bean
    public ProducerFactory<String, WeatherUpdateMessage> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, WeatherUpdateMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // CONSUMER
    @Bean
    public ConsumerFactory<String, WeatherUpdateMessage> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "weather-group");
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonDeserializer.class);
        config.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES,
                "com.project.weatherapp.dto");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(WeatherUpdateMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WeatherUpdateMessage>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, WeatherUpdateMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public NewTopic weatherTopic() {
        return new NewTopic(WEATHER_TOPIC, 1, (short) 1);
    }
}
