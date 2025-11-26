package web.controller.safety;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.service.safety.CctvInfoService;
import web.service.safety.GeoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cctv")
@RequiredArgsConstructor
public class CctvInfoController {

    private final CctvInfoService cctvInfoService;
    private final GeoService geoService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCctv( @RequestParam double lat, @RequestParam double lng ) {


        // 1) 좌표 → 행정구역 (시/구/동)
        GeoService.RegionInfo region = geoService.reverseGeo(lat, lng);

        String sido = region.getSido();         // 예: "인천"
        String sigungu = region.getSigungu();   // 예: "미추홀"
        String dong = region.getDong();         // 예: "주안동"

        return ResponseEntity.ok( cctvInfoService.getAllCctv( sigungu ) );
    }
    // ⭐️ (추가) 동별 CCTV 대수 API
    @GetMapping("/count-by-dong")
    public ResponseEntity<?> getCctvCountByDong(
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        // 1) 좌표 → 행정구역 (시/구/동)
        GeoService.RegionInfo region = geoService.reverseGeo(lat, lng);

        String sido = region.getSido();
        String sigungu = region.getSigungu();
        String dong = region.getDong();

        // 2) 행정구역이 유효한지 확인
        if (sido == null || sigungu == null || dong == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "유효한 행정구역 정보를 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // 3) 해당 동의 CCTV 대수 계산
        int count = cctvInfoService.getCctvCountByRegion(region);

        // 4) 결과 반환 (Flutter 홈 화면용)
        Map<String, Object> response = new HashMap<>();
        response.put("sido", sido);
        response.put("sigungu", sigungu);
        response.put("dong", dong);
        response.put("cctv_count", count); // 해당 동의 CCTV 총 대수

        return ResponseEntity.ok(response);
    }

}
