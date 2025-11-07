package web.controller.transport;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.transport.ParkingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transport")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping("/parking")
    public List<Map<String, Object>> getParkingData() {
        return parkingService.getParkingData();
    }
}
