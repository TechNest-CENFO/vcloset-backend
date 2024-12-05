package com.example.vcloset.rest.recommendations;

import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.weather.WeatherData;
import com.example.vcloset.logic.service.outfit.OutfitService;
import com.example.vcloset.logic.service.weather.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/recommendation")
@RestController
public class RecommendationsRestController {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private OutfitService outfitService;

    @GetMapping("/weekly/{lat}/{lon}")
    public ResponseEntity<?> getWeeklyOutfit(@PathVariable String lat,
                                             @PathVariable String lon,
                                             HttpServletRequest request) {
        WeatherData weatherData = weatherService.getWeatherBylatAndLon(lat, lon);



        return  new GlobalResponseHandler().handleResponse(
                "Clima generado con éxito",
                weatherData,
                HttpStatus.OK,
                request);
    }

}
