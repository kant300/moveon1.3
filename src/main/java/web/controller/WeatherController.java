package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.service.WeatherService;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@CrossOrigin( origins = "*")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("")
    public String getData(@RequestParam int lat, @RequestParam int lon) {
        String result =  weatherService.getWeatherData(lat, lon);
        // System.out.println("result = " + result);
        return result;
    }
}
