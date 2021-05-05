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
}
