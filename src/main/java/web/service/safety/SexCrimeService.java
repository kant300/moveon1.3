package web.service.safety;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import web.model.dto.safety.SexCrimeDto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SexCrimeService {

    private final List<SexCrimeDto> crimeList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {

        String basePath = System.getProperty("user.dir");
        String filePath = basePath +  "/build/resources/main/static/data/sexcrime.csv";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {

                // 첫 줄(헤더) 건너뛰기
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] d = line.split(",", -1);

                // 최소 21개 컬럼이 있는지 확인
                if (d.length < 21) continue;

                // 공백은 null 처리
                for (int i = 0; i < d.length; i++) {
                    d[i] = d[i].trim().isEmpty() ? null : d[i].trim();
                }

                // ============================
                // CSV 컬럼 기준 매핑 (0-index)
                // 18 → chgBfrCtpvNm
                // 19 → chgBfrSggNm
                // 20 → chgBfrUmdNm
                // ============================

                SexCrimeDto dto = new SexCrimeDto(
                        d[18],   // chgBfrCtpvNm (시도)
                        d[19],   // chgBfrSggNm (시군구)
                        d[20]    // chgBfrUmdNm (읍면동)
                );

                // ⭐ 추가: 특정 지역 데이터 확인 (예: 인천 부평구)
                if ("인천광역시".equals(d[18]) && "부평구".equals(d[19])) {
                    System.out.println("인천 부평구 로드 데이터: " + d[20]);
                }
                // CSV 컬럼 기준 매핑된 값 d[18] (시도)와 d[19] (시군구) 출력
                if ("인천광역시".equals(d[18]) && "부평구".equals(d[19])) {
                    System.out.println("로드된 시도/시군구/읍면동 값: " + d[18] + "/" + d[19] + "/" + d[20]);
                }

                crimeList.add(dto);
            }

            System.out.println("✅ SexCrime CSV 로드 완료: " + crimeList.size() + "건");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ SexCrime CSV 로드 중 오류!");
        }
    }


    /**
     * 시도 명칭을 표준화합니다 (예: "인천광역시" -> "인천").
     * @param regionName 정규화할 지역 이름
     * @return 표준화된 지역 이름
     */
    private String normalizeRegionName(String regionName) {
        if (regionName == null || regionName.isEmpty()) {
            return "";
        }
        String name = regionName.trim();
        name = name.replace("특별시", "");
        name = name.replace("광역시", "");
        name = name.replace("특별자치시", ""); // 세종특별자치시 대비
        name = name.replace("특별자치도", ""); // 제주특별자치도 대비
        name = name.replace("도", "");

        // 최종적으로 공백을 제거하고 반환 (예: "경 기" -> "경기")
        return name.replaceAll("\\s+", "");
    }


    // 행정구역별 카운트
    public Map<String, Long> countByRegion(String sido, String sigungu, String dong) {
        // DONG 입력 값 확인 로그 (countByRegion 시작 부분)
        System.out.println(">>> 요청 지역: SIDO=" + sido + ", SIGUNGU=" + sigungu + ", DONG=" + dong);

        // 1단계: 입력 값을 먼저 정규화합니다.
        // 예를 들어, sido가 "인천"인 경우, trimmedSido는 "인천"이 됩니다.
        String inputSido = normalizeRegionName(sido); // "인천광역시" 요청 시 -> "인천"
        String inputSigungu = (sigungu != null) ? sigungu.trim() : null; // 시군구는 접미사 제거 불필요
        String inputDong = (dong != null) ? dong.trim() : null; // 읍면동은 접미사 제거 불필요

        // 1. 시도 카운트
        long sidoCount = 0;
        if (!inputSido.isEmpty()) {
            sidoCount = crimeList.stream()
                    .filter(c -> {
                        String csvSido = normalizeRegionName(c.getChgBfrCtpvNm()); // CSV 데이터도 정규화 (예: "인천광역시" -> "인천")
                        return csvSido.equals(inputSido);
                    })
                    .count();
        }

        // 2. 시군구 카운트
        long sigunguCount = 0;
        if (!inputSido.isEmpty() && inputSigungu != null && !inputSigungu.isEmpty()) {
            sigunguCount = crimeList.stream()
                    .filter(c -> {
                        String csvSido = normalizeRegionName(c.getChgBfrCtpvNm());
                        // CSV 데이터의 시군구는 null이 아니면서 입력 시군구와 정확히 일치해야 합니다.
                        return csvSido.equals(inputSido)
                                && c.getChgBfrSggNm() != null
                                && c.getChgBfrSggNm().trim().equals(inputSigungu);
                    })
                    .count();
        }

        // 3. 읍면동 카운트
        long dongCount = 0;
        if (!inputSido.isEmpty() && inputSigungu != null && !inputSigungu.isEmpty() && inputDong != null && !inputDong.isEmpty()) {
            dongCount = crimeList.stream()
                    .filter(c -> {
                        String csvSido = normalizeRegionName(c.getChgBfrCtpvNm());
                        String csvSgg = (c.getChgBfrSggNm() != null) ? c.getChgBfrSggNm().trim() : "";
                        String csvUmd = (c.getChgBfrUmdNm() != null) ? c.getChgBfrUmdNm().trim() : "";

                        // 1. 시도, 시군구 일치 확인
                        boolean isRegionMatch = csvSido.equals(inputSido)
                                && csvSgg.equals(inputSigungu);

                        // ⭐ 2. 읍면동 필터링: startsWith()으로 변경
                        // 입력된 DONG 값으로 시작하는지 확인 ("부평동" 입력 시 "부평1동", "부평2동" 포함)
                        boolean isDongMatch = csvUmd.startsWith(inputDong);

                        return isRegionMatch && isDongMatch;
                    })
                    .count();
        }

        Map<String, Long> result = new HashMap<>();
        result.put("sidoCount", sidoCount);
        result.put("sigunguCount", sigunguCount);
        result.put("dongCount", dongCount);

        return result;
    }
    // ⭐ [수정된 기능: 지역별 필터링된 리스트 반환 - 읍면동 추가]
    /**
     * 시도, 시군구, 읍면동에 따라 성범죄자 데이터를 필터링하여 반환합니다.
     * @param sido 요청 시도명
     * @param sigungu 요청 시군구명 (nullable)
     * @param dong 요청 읍면동명 (nullable)
     * @return 필터링된 SexCrimeDto 리스트
     */

    public Map<String, Integer> filterByRegion(String sido, String sigungu, String dong) {

        // 1. 입력값 정규화
        final String inputSido   = normalizeRegionName(sido);
        final String inputSigungu = (sigungu != null) ? sigungu.trim() : null;
        final String inputDong    = (dong != null) ? dong.trim() : null;

        int sidoCount = 0;
        int sigunguCount = 0;
        int dongCount = 0;

        for (SexCrimeDto c : crimeList) {

            String csvSido   = normalizeRegionName(c.getChgBfrCtpvNm());
            String csvSigungu = (c.getChgBfrSggNm() != null) ? c.getChgBfrSggNm().trim() : "";
            String csvDong    = (c.getChgBfrUmdNm() != null) ? c.getChgBfrUmdNm().trim() : "";

            // -----------------------
            // 1) 시도 카운트
            // -----------------------
            if (csvSido.equals(inputSido)) {
                sidoCount++;

                // -----------------------
                // 2) sigungu 입력되면 구군 카운트
                // -----------------------
                if (inputSigungu != null && inputSigungu.equals(csvSigungu)) {
                    sigunguCount++;

                    // -----------------------
                    // 3) dong 입력되면 동 카운트 (startsWith)
                    // -----------------------
                    if (inputDong != null && csvDong.startsWith(inputDong)) {
                        dongCount++;
                    }
                }
            }
        }

        // 결과 리턴
        Map<String, Integer> result = new HashMap<>();
        result.put("sido", sidoCount);
        result.put("sigungu", sigunguCount);
        result.put("dong", dongCount);

        return result;
    }

}
