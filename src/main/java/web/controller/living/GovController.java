package web.controller.living;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.living.GovService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/living")
@RequiredArgsConstructor
public class GovController {
    private final GovService govService;

    @GetMapping("/gov")
    public List<Map<String, Object>> getGovernmentOfficeData() {
        return govService.getGovernmentOfficeData();
    }
}
