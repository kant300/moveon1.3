package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.ToiletService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/safety")
@RequiredArgsConstructor
public class ToiletController {
    private final ToiletService toiletService;

    @GetMapping("/toilet")
    public List<Map<String, Object>> getToiletData() {
        return toiletService.getToiletData();
    }
}
