//package web.service.safety;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//import web.model.dto.safety.SexCrimeDto;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class SexCrimeService {
//
//    private final List<SexCrimeDto> crimeList = new ArrayList<>();
//
//    @PostConstruct
//    public void loadCsvData() {
//
//        String basePath = System.getProperty("user.dir");
//        String filePath = basePath +  "/build/resources/main/static/data/sexcrime.csv";
//
//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
//
//            String line;
//            boolean isHeader = true;
//
//            while ((line = br.readLine()) != null) {
//
//                // 첫 줄(헤더) 건너뛰기
//                if (isHeader) {
//                    isHeader = false;
//                    continue;
//                }
//
//                String[] d = line.split(",", -1);
//
//                // 최소 21개 컬럼이 있는지 확인
//                if (d.length < 21) continue;
//
//                // 공백은 null 처리
//                for (int i = 0; i < d.length; i++) {
//                    d[i] = d[i].trim().isEmpty() ? null : d[i].trim();
//                }
//
//                // ============================
//                // CSV 컬럼 기준 매핑 (0-index)
//                // 18 → chgBfrCtpvNm
//                // 19 → chgBfrSggNm
//                // 20 → chgBfrUmdNm
//                // ============================
//
//                SexCrimeDto dto = new SexCrimeDto(
//                        d[18],   // chgBfrCtpvNm (시도)
//                        d[19],   // chgBfrSggNm (시군구)
//                        d[20]    // chgBfrUmdNm (읍면동)
//                );
//
//                crimeList.add(dto);
//            }
//
//            System.out.println("✅ SexCrime CSV 로드 완료: " + crimeList.size() + "건");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("❌ SexCrime CSV 로드 중 오류!");
//        }
//    }
//
//
//    public List<SexCrimeDto> getAllSexCrime() {
//        return crimeList;
//    }
//
//}
