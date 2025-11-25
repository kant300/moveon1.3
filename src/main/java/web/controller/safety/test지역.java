//package web.controller.safety;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import web.service.safety.GeoService;
//import web.service.safety.SexCrimeService;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/sexcrime")
//public class SexCrimeController {
//
//    private final SexCrimeService sexCrimeService;
//    private final GeoService geoService; // 좌표 → 행정구역 변환 서비스 (카카오 API)
//
//    @GetMapping("/near")
//    public Map<String, Object> getCrimeCounts(@RequestParam double lat, @RequestParam double lng) {
//
//        // 1) 좌표 → 행정구역
//        GeoService.RegionInfo region = geoService.reverseGeo(lat, lng);
//
//        // 2) 카운트 계산
//        Map<String, Long> counts = sexCrimeService.countByRegion(
//                region.getSido(),
//                region.getSigungu(),
//                region.getDong()
//        );
//
//        // 3) 응답
//        Map<String, Object> result = new HashMap<>();
//        result.put("region", region);
//        result.put("counts", counts);
//        result.put("source", "여성가족부 성범죄자 알림e 공개자료");
//
//        return result;
//    }
//}
