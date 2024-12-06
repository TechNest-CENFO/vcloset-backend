package com.example.vcloset.rest.weather;


import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.weather.WeatherData;
import com.example.vcloset.logic.service.category.CategoryService;
import com.example.vcloset.logic.service.weather.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;

@RequestMapping("/weather")
@RestController
public class WeatherRestController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city,  HttpServletRequest request) {


        return  new GlobalResponseHandler().handleResponse(
                "Clima generado con éxito",
                weatherService.getWeather(city),
                HttpStatus.OK,
                request);
    }

    // Metodo para obtener por latitud y longitud
    @GetMapping("/{lat}/{lon}")
    public ResponseEntity<?> getWeatherLatAndLon(@PathVariable String lat,
                                                 @PathVariable String lon,
                                                 HttpServletRequest request
                                                 ) {

        return  new GlobalResponseHandler().handleResponse(
                "Clima generado con éxito",
                weatherService.getWeatherBylatAndLon(lat, lon),
                HttpStatus.OK,
                request);
    }

}
