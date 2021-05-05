
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
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private ForecastRepo forecastRepo;
    @Autowired
    private TownRepo townRepo;

    @GetMapping("/")
    public String greeting(){
        return "greeting";
    }
    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        //forecastRepo.deleteAll();
        //townRepo.deleteAll();
        // проверка? проверка ветки?
        Iterable<Forecast> forecasts = forecastRepo.findAll();
        Iterable<Town> towns = townRepo.findAll();

        model.put("forecasts", forecasts);
        model.put("towns", towns);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String townInput, Map<String, Object> model) throws ParseException, IOException, JSONException {
        if (!townRepo.existsByName(townInput))
            townRepo.save(new Town(townInput));

        String town = "";
        Double degreesApi;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(calendar.getTime());

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.openweathermap.org/data/2.5/weather?q=" +
                            townInput +
                            "&units=metric&appid=2d81d63e0f7c7b1ecbcba144d563fbc4")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();
            JSONObject obj = new JSONObject(jsonData);
            degreesApi = Double.parseDouble(obj.getJSONObject("main").getString("temp"));
            town = obj.getString("name");
            //System.out.print(degreesApi + " " + town);
        }
        catch (Exception e) {
            return "redirect:/main";
        }

        String value = "";

        try{
            Document doc = Jsoup.connect("https://yandex.ru/pogoda/" + town).timeout(0).get();
            Elements e = doc.select(" div.temp.fact__temp.fact__temp_size_s");
            value = e.first().text().replace("Текущая температура", "");

            Double degrees = Double.parseDouble(value);
            Double outputDegrees = Precision.round(((degreesApi + degrees) / 2), 2);

            Forecast forecast = new Forecast(townRepo.findByName(townInput), outputDegrees, date);
            forecastRepo.save(forecast);
        }
        catch (Exception e) {
            Forecast forecast = new Forecast(townRepo.findByName(townInput), degreesApi, date);
            forecastRepo.save(forecast);
            //return "redirect:/main";
        }
        Iterable<Forecast> forecasts = forecastRepo.findAll();
        Iterable<Town> towns = townRepo.findAll();

        model.put("forecasts", forecasts);
        model.put("towns", towns);
        return "redirect:/main";
    }
}