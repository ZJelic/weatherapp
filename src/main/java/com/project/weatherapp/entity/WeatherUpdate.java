package com.project.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_update")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(nullable = false)
    private double temperature;

    @Column(name = "wind_speed", nullable = false)
    private double windSpeed;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
