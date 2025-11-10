package web.controller.jwtcontroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.service.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/jwt/member")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    // 토큰 생성
    @PostMapping("/create")
    public ResponseEntity<?> createToken(@RequestBody Map<String, String> payload) {
        String mid = payload.get("mid");
        String mrole = payload.get("mrole");
        String result = jwtService.createToken(mid, mrole);
        return ResponseEntity.ok(result);
    }


    // 토큰 검증
    @GetMapping("/read")
    public ResponseEntity< ? > checkToken(@RequestParam String token){
        boolean result = jwtService.checkToken(token);
        return ResponseEntity.ok(result);
    }

    // 토큰 값 추출
    @GetMapping("/get")
    public ResponseEntity<?> tokenget(@RequestParam String token) {
        String mid = jwtService.getMid(token);
        String mrole = jwtService.getMrole(token);
        return ResponseEntity.ok(Map.of("mid", mid, "mrole", mrole));
    }

}
