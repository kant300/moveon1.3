package web.controller.transport;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.service.transport.StationService;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transport")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @GetMapping("/lift")
    public List<Map<String, String>> getLiftData() {
        return stationService.getLiftData();
    }

    @GetMapping("/location")
    public List<Map<String, String>> getStationLocationData() {
        return stationService.getStationLocationData();
    }

    @GetMapping("/schedule")
    public List<List<LocalTime>> getScheduleData(@RequestParam String station_name) {
        return stationService.getScheduleData(station_name);
    }

    @GetMapping("/gas")
    public List<Map<String, Object>> getGasStationData() {
        return stationService.getGasStationData();
    }
}
