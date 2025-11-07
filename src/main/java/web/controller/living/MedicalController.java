package web.controller.living;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.living.MedicalService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/living")
@RequiredArgsConstructor
public class MedicalController {
    private final MedicalService medicalService;

    @GetMapping("/medical")
    public List<Map<String, Object>> getMedicalData() {
        return medicalService.getMedicalData();
    }
}
