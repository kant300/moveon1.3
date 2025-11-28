package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.EmergencyWaterService;

@RestController
@RequiredArgsConstructor
public class EmergencyWaterController {

    private final EmergencyWaterService emergencyWaterService;

    @GetMapping("/api/emergencywater")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(emergencyWaterService.getEmergencyWaterList());
    }
}
