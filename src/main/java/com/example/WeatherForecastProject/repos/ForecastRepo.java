package com.example.WeatherForecastProject.repos;

import com.example.WeatherForecastProject.domain.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForecastRepo extends JpaRepository<Forecast, Long> {

}
