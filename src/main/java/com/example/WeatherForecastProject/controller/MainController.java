
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

<<<<<<< Updated upstream
=======
        Integer successAmount = 0;

        Map<String, Double> weatherApi;
        if (weatherApi(town) != null) {
            successAmount += 1;
            weatherApi = weatherApi(town);
            degrees += weatherApi.get("Degrees");
            wind += weatherApi.get("Wind");
            humidity += weatherApi.get("Humidity");
            pressure += weatherApi.get("Pressure");
        }

        Map<String, Double> openWeatherApi;
        if (openWeatherApi(town) != null) {
            successAmount += 1;
            openWeatherApi = openWeatherApi(town);
            degrees += openWeatherApi.get("Degrees");
            wind += openWeatherApi.get("Wind");
            humidity += openWeatherApi.get("Humidity");
            pressure += openWeatherApi.get("Pressure");
        }

        Map<String, Double> yandexParse;
        if (yandexParse(town) != null) {
            successAmount += 1;
            yandexParse = yandexParse(town);
            degrees += yandexParse.get("Degrees");
            wind += yandexParse.get("Wind");
            humidity += yandexParse.get("Humidity");
            pressure += yandexParse.get("Pressure");
        }

        if (successAmount == 0)
            return "redirect:/main";

        degrees = Precision.round((degrees / successAmount), 2);
        wind = Precision.round((wind / successAmount), 2);
        humidity = Precision.round((humidity / successAmount), 2);
        pressure = Precision.round((pressure / successAmount), 2);

        Map<String, Double> weatherApiFuture;
        if (weatherApiFuture(town) != null){
            weatherApiFuture = weatherApiFuture(town);
            future += weatherApiFuture.get("Degrees").toString() + "℃, ветер ";
            future += weatherApiFuture.get("Wind").toString() + "м/с, влажность ";
            future += weatherApiFuture.get("Humidity").toString() + "%, давление ";
            future += weatherApiFuture.get("Pressure").toString() + " мм.р.с.";
        }

        System.out.println(future);

        if (townRepo.existsByName(townInput))
            townRepo.findByName(townInput).setFuture(future);
        else{
            townRepo.save(new Town(townInput));
            townRepo.findByName(townInput).setFuture(future);
        }

        Forecast forecast = new Forecast(townRepo.findByName(townInput), degrees, date, wind, humidity, pressure);
        forecastRepo.save(forecast);

        Iterable<Forecast> forecasts = forecastRepo.findAll();
        Iterable<Town> towns = townRepo.findAll();

        model.put("forecasts", forecasts);
        model.put("towns", towns);
        return "redirect:/main";
    }

    public Map<String, Double> weatherApi(String town){
        Map<String, Double> forecast = new HashMap<String, Double>();

        Double degrees;
        Double wind;
        Double humidity;
        Double pressure;


        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.weatherapi.com/v1/current.json?key=d29d08b3fd2f4219a4272219212005&q=" +
                            town +
                            "&aqi=no")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();
            JSONObject obj = new JSONObject(jsonData);

            degrees = Double.parseDouble(obj.getJSONObject("current").getString("temp_c"));
            wind = Double.parseDouble(obj.getJSONObject("current").getString("wind_kph"));
            humidity = Double.parseDouble(obj.getJSONObject("current").getString("humidity"));
            pressure = Precision.round((Double.parseDouble(obj.getJSONObject("current").getString("pressure_mb")) * 0.750062), 2);

            forecast.put("Degrees", degrees);
            forecast.put("Wind", wind);
            forecast.put("Humidity", humidity);
            forecast.put("Pressure", pressure);
        }
        catch (Exception e) {
            return null;
        }

        return forecast;
    }

    public Map<String, Double> openWeatherApi(String town){
        Map<String, Double> forecast = new HashMap<String, Double>();

        Double degrees;
        Double wind;
        Double humidity;
        Double pressure;


>>>>>>> Stashed changes
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