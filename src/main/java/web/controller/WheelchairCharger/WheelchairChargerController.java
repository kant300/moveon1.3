package web.controller.WheelchairCharger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.WheelchairCharger.WheelchairChargerService;

@RestController
@RequestMapping("/api/chargers")
@RequiredArgsConstructor

public class WheelchairChargerController {

    private final WheelchairChargerService chargerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllChargers(){
        return ResponseEntity.ok( chargerService.getChargerList());
    }
}
