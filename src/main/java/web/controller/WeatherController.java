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
    // 위도와 경도는 소수점을 가질 수 있으므로 int 대신 double 타입을 사용
    public String getData(@RequestParam double lat, @RequestParam double lon) {
        String result =  weatherService.getWeatherData(lat, lon);
        // System.out.println("result = " + result);
        return result;
    }
}
