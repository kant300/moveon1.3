package web.service.safety;

import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
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


    public List<CctvInfo> getAllCctv(String sigungu) {

        List<CctvInfo> cctvList = new ArrayList<>();

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

                // (1) trim + 빈칸 → null
                for (int i = 0; i < d.length; i++) {
                    d[i] = (d[i] == null || d[i].trim().isEmpty()) ? null : d[i].trim();
                }

                // (2) 설치목적 = 교통단속 제거
                if ("교통단속".equals(d[4])) continue;

//                // (4) 설치연월 2017 이상
//                String 설치연월 = d[9];
//                if (설치연월 == null) continue;
//
//                try {
//                    int year = Integer.parseInt(설치연월.substring(0, 4));
//                    if (year < 2017) continue;
//                } catch (Exception e) {
//                    continue;
//                }

                // -----------------------------------------------------
                // (5) sigungu 필터링 (도로명 OR 지번 주소 중 하나라도 포함되면 OK)
                // -----------------------------------------------------
                if (sigungu != null && !sigungu.trim().isEmpty()) {

                    String roadAddr = d[2];   // 소재지도로명주소
                    String jibunAddr = d[3];  // 소재지지번주소

                    boolean match = false;

                    if (roadAddr != null && roadAddr.contains(sigungu)) match = true;
                    if (jibunAddr != null && jibunAddr.contains(sigungu)) match = true;

                    // 둘 다 없으면 제외
                    if (!match) continue;
                }
                // -----------------------------------------------------

                // DTO 생성
                CctvInfo cctv = new CctvInfo(
                        d[0],  // 번호
                        d[1],  // 관리기관명
                        d[2],  // 도로명주소
                        d[3],  // 지번주소
                        d[4],  // 설치목적
                        d[5],  // 대수
                        d[6],  // 화소수
                        d[7],  // 방면정보
                        d[8],  // 보관일수
                        d[9],  // 설치연월
                        d[10], // 연락처
                        d[11], // 위도
                        d[12]  // 경도
                );

                cctvList.add(cctv);
            }

            System.out.println("CCTV CSV 필터링 + 로드 완료: " + cctvList.size() + "건");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CCTV CSV 로드 중 오류 발생!");
        }

        return cctvList;
    }

}