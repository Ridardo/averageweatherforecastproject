package com.example.WeatherForecastProject.repos;

import com.example.WeatherForecastProject.domain.Forecast;
import com.example.WeatherForecastProject.domain.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForecastRepo extends JpaRepository<Forecast, Long> {

    List<Forecast> findByTown(Town town);

    List<Forecast> findByDate(String date);

}
