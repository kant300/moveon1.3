package web.service.safety;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import web.model.dto.safety.CctvInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CctvInfoService {
    private final List<CctvInfo> cctvList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {

        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "/build/resources/main/static/data/인천광역시_CCTV.csv";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] d = line.split(",", -1);
                if (d.length < 13) continue;

                // -----------------------
                // 1) 비어있는 값 → NULL 처리
                // -----------------------
                for (int i = 0; i < d.length; i++) {
                    d[i] = d[i].trim().isEmpty() ? null : d[i].trim();
                }

                // -------------------------------
                // 2) 설치목적구분 = 교통단속 -> 제외
                // -------------------------------
                String 목적 = d[4];
                if (목적 != null && 목적.equals("교통단속")) {
                    continue;
                }

                // --------------------------------------
                // 3) 카메라화소수 200 이상 아닌 경우 제외
                //    null → 제외
                // --------------------------------------
                String 화소 = d[6];
                if (화소 == null) {
                    continue;
                }

                try {
                    // 화소 값이 "200만", "200만 화소" 등 일 수도 있어서 숫자만 추출
                    String numStr = 화소.replaceAll("[^0-9]", "");
                    if (numStr.isEmpty()) continue;

                    int 화소수 = Integer.parseInt(numStr);
                    if (화소수 < 200) continue; // 200 미만 제거
                } catch (Exception e) {
                    continue; // 숫자 변환 안 되면 제거
                }

                // ----------------------------------
                // 4) 설치 연월 2017년부터 아닌경우 제외
                // ----------------------------------
                String 설치연월 = d[9];
                if (설치연월 == null) continue;

                // 형태: YYYY-MM 또는 YYYYMM 또는 YYYY
                try {
                    String yearStr = 설치연월.substring(0, 4);
                    int year = Integer.parseInt(yearStr);

                    if (year < 2017) continue;
                } catch (Exception e) {
                    continue;
                }

                // -----------------------
                // 최종 DTO 생성
                // -----------------------
                CctvInfo cctv = new CctvInfo(
                        d[0],  // 번호
                        d[1],  // 관리기관명
                        d[2],  // 소재지도로명주소
                        d[3],  // 소재지지번주소
                        d[4],  // 설치목적구분
                        d[5],  // 카메라대수
                        d[6],  // 카메라화소수
                        d[7],  // 촬영방면정보
                        d[8],  // 보관일수
                        d[9],  // 설치연월
                        d[10], // 관리기관전화번호
                        d[11], // WGS84위도
                        d[12]  // WGS84경도
                );

                cctvList.add(cctv);
            }

            System.out.println("CCTV CSV 필터링 + 로드 완료: " + cctvList.size() + "건");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CCTV CSV 로드 중 오류 발생!");
        }
    }

    public List<CctvInfo> getAllCctv() {
        return cctvList;
    }
}