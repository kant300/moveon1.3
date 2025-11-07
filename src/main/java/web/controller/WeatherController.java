package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.service.WeatherService;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("")
    public String getData(@RequestParam int lat, @RequestParam int lon) {
        String result =  weatherService.getWeatherData(lat, lon);
        // System.out.println("result = " + result);
        return result;
    }
}
