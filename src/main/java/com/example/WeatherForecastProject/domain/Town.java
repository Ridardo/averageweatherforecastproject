package com.example.WeatherForecastProject.domain;

import org.springframework.boot.context.properties.bind.Name;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(targetEntity = Forecast.class,mappedBy = "town", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Forecast> forecasts;

    private String future;

    public String getFuture() {
        return future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public void addForecast(Forecast forecast)
    {
        this.forecasts.add(forecast);
    }

    public Town() {
    }

    public Town(String name) {
        this.name = name;
    }

}