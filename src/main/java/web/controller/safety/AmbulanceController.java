package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.AmbulanceService;


@RestController
@RequestMapping("/api/ambulance")
@RequiredArgsConstructor
public class AmbulanceController {
    private final AmbulanceService ambulanceService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllAmbulances(){
        return ResponseEntity.ok(ambulanceService.getAllAmbulances());
    }
}
