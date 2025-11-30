DROP TABLE IF EXISTS weather_station;

CREATE TABLE weather_station (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL,
                                 location VARCHAR(100),
                                 latitude DOUBLE NOT NULL,
                                 longitude DOUBLE NOT NULL,
                                 elevation DOUBLE
);

-- CREATE TABLE weather_measurement (
--                                      id SERIAL PRIMARY KEY,
--                                      station_id INT NOT NULL,
--                                      temperature NUMERIC,
--                                      humidity NUMERIC,
--                                      measured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                                      FOREIGN KEY (station_id) REFERENCES weather_station(id)
-- );
--
-- CREATE TABLE weather_alert (
--                                id SERIAL PRIMARY KEY,
--                                station_id INT NOT NULL,
--                                alert_type VARCHAR(50),
--                                description TEXT,
--                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                                FOREIGN KEY (station_id) REFERENCES weather_station(id)
-- );
