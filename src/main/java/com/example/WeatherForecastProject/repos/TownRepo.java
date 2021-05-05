package com.example.WeatherForecastProject.repos;

import com.example.WeatherForecastProject.domain.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepo extends JpaRepository<Town, Long> {
    Town findByName(String name);
    boolean existsByName(String name);
}
