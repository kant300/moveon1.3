package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.model.dto.safety.SexCrimeDto;
import web.service.safety.GeoService;
import web.service.safety.SexCrimeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sexcrime")
public class SexCrimeController {

    private final SexCrimeService sexCrimeService;
    private final GeoService geoService; // 좌표 → 행정구역 변환 서비스 (카카오 API)

    @GetMapping("/near")
    public Map<String, Object> getCrimeCounts(@RequestParam double lat, @RequestParam double lng) {

        // 1) 좌표 → 행정구역
        GeoService.RegionInfo region = geoService.reverseGeo(lat, lng);

        // 2) 카운트 계산
        Map<String, Long> counts = sexCrimeService.countByRegion(
                region.getSido(),
                region.getSigungu(),
                region.getDong()
        );

        // 3) 응답
        Map<String, Object> result = new HashMap<>();
        result.put("region", region);
        result.put("counts", counts);
        result.put("source", "여성가족부 성범죄자 알림e 공개자료");

        return result;
    }
    // ⭐ [새로운 기능 추가: 지역 필터링]
    // 요청 예시: /api/sexcrime/filter?sido=인천광역시&sigungu=부평구&dong=부평동
    @GetMapping("/filter")
    public ResponseEntity<?> getFilteredCrimes(
            @RequestParam(required = true) String sido,
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false) String dong
    ) {
        System.out.println("sido = " + sido + ", sigungu = " + sigungu + ", dong = " + dong);
        // 1. 서비스에서 필터링된 데이터 목록을 가져옵니다.
        Map<String,Integer> filteredList = sexCrimeService.filterByRegion(sido, sigungu, dong);

        // 2. 결과를 반환합니다. 데이터가 없으면 204 No Content를 반환할 수도 있지만,
        // 여기서는 빈 리스트를 반환하고 200 OK를 유지합니다.
        return ResponseEntity.ok(filteredList);

    }
}
