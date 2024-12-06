package com.example.vcloset.logic.service.weather;

import com.example.vcloset.logic.entity.weather.WeatherApiResponse;
import com.example.vcloset.logic.entity.weather.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private String apiKey = "f1e6ab5a6d49b69ef817f7c486f6b6f0";


    private final RestTemplate restTemplate = new RestTemplate();


    // Este metodo obtiene el clima de la ciudad desde la API externa
    public WeatherData getWeather(String city) {
        // Construir la URL para la solicitud
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=es",
                city, apiKey);

        // Hacer la solicitud y obtener la respuesta en formato JSON
        WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

        // Mapear la respuesta a WeatherData (puedes ajustar según lo necesites)
        WeatherData weatherData = new WeatherData();
        weatherData.setLon(response.getCoord().getLon());
        weatherData.setLat(response.getCoord().getLat());
        weatherData.setFeels_like(response.getMain().getFeels_like());
        weatherData.setName(response.getName());
        weatherData.setId(response.getWeather().get(0).getId());
        weatherData.setMain(response.getWeather().get(0).getMain());
        weatherData.setDescription(response.getWeather().get(0).getDescription());

        return weatherData;
    }


    public WeatherData getWeatherBylatAndLon(String lat, String lon) {
        // Construir la URL para la solicitud
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric&lang=es",
                lat, lon,apiKey);

        // Hacer la solicitud y obtener la respuesta en formato JSON
        WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

        // Mapear la respuesta a WeatherData (puedes ajustar según lo necesites)
        WeatherData weatherData = new WeatherData();
        weatherData.setLon(response.getCoord().getLon());
        weatherData.setLat(response.getCoord().getLat());
        weatherData.setFeels_like(response.getMain().getFeels_like());
        weatherData.setName(response.getName());
        weatherData.setId(response.getWeather().get(0).getId());
        weatherData.setMain(response.getWeather().get(0).getMain());
        weatherData.setDescription(response.getWeather().get(0).getDescription());

        return weatherData;
    }
}
