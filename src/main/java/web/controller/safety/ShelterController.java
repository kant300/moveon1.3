package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.ShelterService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/safety")
@RequiredArgsConstructor
public class ShelterController {
    private final ShelterService shelterService;

    @GetMapping("/shelter")
    public List<Map<String, Object>> getShelterData() {
        return shelterService.getShelterData();
    }
}
