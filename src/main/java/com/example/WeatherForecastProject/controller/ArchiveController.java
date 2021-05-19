
package com.example.WeatherForecastProject.controller;

import com.example.WeatherForecastProject.domain.Forecast;
import com.example.WeatherForecastProject.domain.Town;
import com.example.WeatherForecastProject.repos.ForecastRepo;
import com.example.WeatherForecastProject.repos.TownRepo;
import jdk.nashorn.internal.parser.JSONParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.math3.util.Precision;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ArchiveController {
    @Autowired
    private ForecastRepo forecastRepo;
    @Autowired
    private TownRepo townRepo;

    @GetMapping("/archive")
    public String archive(Map<String, Object> model) {

        Iterable<Forecast> forecasts = forecastRepo.findAll();
        Iterable<Town> towns = townRepo.findAll();

        model.put("forecasts", forecasts);
        model.put("towns", towns);

        return "archive";
    }

    @PostMapping("archive")
    public String search(@RequestParam String townInput, @RequestParam String dateInput, Map<String, Object> model) throws ParseException, IOException, JSONException {
        List<Forecast> _forecasts = forecastRepo.findByTown(townRepo.findByName(townInput));
        System.out.println(_forecasts.size());

        List<Forecast> forecasts = null;
        String[] dates = dateInput.split("-");
        String date = dates[2] + "-" + dates[1] + "-" + dates[0];
        System.out.println(date);

        _forecasts.forEach(forecast -> {
            System.out.println(forecast.getDate().substring(0, 10));
                    if (forecast.getDate().substring(0, 10) == date) {
                        forecasts.add(forecast);
                    }
                });

        model.put("towns", townRepo.findByName(townInput));
        model.put("forecasts", forecasts);
        return "archive";
    }
}