package com.example.WeatherForecastProject.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class Forecast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Double degrees;

    private Double pressure;

    private Double humidity;

    private String wind;

    @ManyToOne
    @JoinColumn(name="town_id")
    private Town town;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Forecast() {
    }

    public Forecast(Town town, Double degrees, String date){
        this.town = town;
        this.degrees = degrees;
        this.date = date;
        this.wind = "No info on wind";
        this.humidity = -1.00;
        this.pressure = -1.00;
    }

    public Forecast(Town town, Double degrees, String date, String wind, Double humidity, Double pressure){
        this.town = town;
        this.degrees = degrees;
        this.date = date;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public Double getDegrees() {
        return degrees;
    }

    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public String GetWind() {return wind;}

    public void setWind(String wind)
    {
        this.wind = wind;
    }

}
