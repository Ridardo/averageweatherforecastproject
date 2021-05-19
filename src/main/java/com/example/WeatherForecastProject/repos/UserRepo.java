package com.example.WeatherForecastProject.repos;

import com.example.WeatherForecastProject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    long deleteByUsername(String username);
}
