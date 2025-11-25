package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.CctvInfoService;

@RestController
@RequestMapping("/api/cctv")
@RequiredArgsConstructor
public class CctvInfoController {

    private final CctvInfoService cctvInfoService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCctv() {
        return ResponseEntity.ok(cctvInfoService.getAllCctv());
    }
}
