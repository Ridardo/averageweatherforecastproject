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

    private Double wind;

    @ManyToOne
    @JoinColumn(name="town_id")
    private Town town;

    @ManyToOne
    @JoinColumn(name="usr_id")
    private User user;

    private String date;

    public User getUser() {
         return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

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
        this.wind = -1.00;
        this.humidity = -1.00;
        this.pressure = -1.00;
    }

    public Forecast(Town town, Double degrees, String date, Double wind, Double humidity, Double pressure){
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

    public Double GetWind() {return wind;}

    public void setWind(Double wind)
    {
        this.wind = wind;
    }
}
