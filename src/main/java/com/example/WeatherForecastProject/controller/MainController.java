
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
        String tempTownInput = townInput.substring(0, 1).toUpperCase() + townInput.substring(1);
        townInput = tempTownInput;
        String town = "";
        Double degreesApi;
        Double windApi;
        Double humidityApi;
        Double pressureApi;

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
            windApi = Double.parseDouble(obj.getJSONObject("wind").getString("speed"));
            humidityApi = Double.parseDouble(obj.getJSONObject("main").getString("humidity"));
            pressureApi = Precision.round((Double.parseDouble(obj.getJSONObject("main").getString("pressure")) * 0.750062), 2);
            town = obj.getString("name");

            System.out.println(town + ": температура " + degreesApi + " ветер " + windApi + " влажность " + humidityApi + " давление " + pressureApi * 0.750062);
        }
        catch (Exception e) {
            System.out.println(e + " at 84");
            return "redirect:/main";
        }

        String value = "";


        try{
            if (!townRepo.existsByName(townInput))
                townRepo.save(new Town(townInput));
            Document doc = Jsoup.connect("https://yandex.ru/pogoda/" + town).timeout(0).get();

            Elements e = doc.select(" div.temp.fact__temp.fact__temp_size_s");
            value = e.first().text().replace("Текущая температура", "");
            Double degrees = Double.parseDouble(value);
            Double outputDegrees = Precision.round(((degreesApi + degrees) / 2), 2);
            Forecast forecast = new Forecast(townRepo.findByName(townInput), outputDegrees, date);

            String _wind = doc.select("div.term.term_orient_v.fact__wind-speed").text();

            if (_wind.equals("Штиль"))
                _wind = "0 м/с";


            String[] splittedWindInput = _wind.split(" ");
            Double outputWindTemp = Precision.round(
                    ((Double.parseDouble(splittedWindInput[0].replace(',','.')) + windApi) / 2), 2);
            //System.out.println(Double.parseDouble(splittedWindInput[0].replace(',','.')));

            if (splittedWindInput.length == 3)
            {
                String direction = splittedWindInput[2];
                _wind = direction + " " + outputWindTemp.toString();
            }
            else
                _wind = outputWindTemp.toString();

            forecast.setWind(_wind);

            String _humidity = doc.select("div.term.term_orient_v.fact__humidity").text().replaceAll("%", "");
            Double outputHumidity = Precision.round(((Double.parseDouble(_humidity) + humidityApi) / 2), 2);
            forecast.setHumidity(outputHumidity);

            String _pressure = doc.select("div.term.term_orient_v.fact__pressure").text();
            Double outputPressure = Precision.round(((Double.parseDouble(_pressure.split(" ")[0]) + pressureApi) / 2), 2);
            forecast.setPressure(outputPressure);

            forecastRepo.save(forecast);
        }
        catch (Exception e) {
            if (!townRepo.existsByName(townInput))
                townRepo.save(new Town(townInput));
            Forecast forecast = new Forecast(townRepo.findByName(townInput), degreesApi, date, windApi.toString() + " м/c", humidityApi, pressureApi);
            forecastRepo.save(forecast);
            System.out.println(e + " somewhere lmao");
            return "redirect:/main";
        }

        Iterable<Forecast> forecasts = forecastRepo.findAll();
        Iterable<Town> towns = townRepo.findAll();

        model.put("forecasts", forecasts);
        model.put("towns", towns);
        return "redirect:/main";
    }
}