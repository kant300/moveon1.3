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
import java.util.stream.Collectors;

@Service
public class CctvInfoService {

    // ⭐️ (추가) 전체 CCTV 데이터를 저장할 메모리 영역 (싱글톤 서비스 시작 시 1회 로드)
    private List<CctvInfo> allCctvList = new ArrayList<>();

    // ⭐️ (수정) getAllCctv 로직을 @PostConstruct로 이동하여 서버 시작 시 데이터 로드
    @PostConstruct
    public void loadAllCctv() {
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

                // (3) 위도/경도 데이터가 없는 경우 필터링
                if (d[11] == null || d[12] == null) continue;

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
//                if (sigungu != null && !sigungu.trim().isEmpty()) {
//
//                    String roadAddr = d[2];   // 소재지도로명주소
//                    String jibunAddr = d[3];  // 소재지지번주소
//
//                    boolean match = false;
//
//                    if (roadAddr != null && roadAddr.contains(sigungu)) match = true;
//                    if (jibunAddr != null && jibunAddr.contains(sigungu)) match = true;
//
//                    // 둘 다 없으면 제외
//                    if (!match) continue;
//                }
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

                allCctvList.add(cctv);
            }

            System.out.println("CCTV CSV 필터링 + 로드 완료: " + allCctvList.size() + "건");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CCTV CSV 로드 중 오류 발생!");
        }

        //return cctvList;
    }

    /**
     * 특정 시군구에 해당하는 CCTV 목록 반환 (기존 로직)
     * @param sigungu 필터링할 시군구 이름 (예: 미추홀)
     * @return 필터링된 CCTV 목록
     */
    public List<CctvInfo> getAllCctv(String sigungu) {
        if (sigungu == null || sigungu.trim().isEmpty()) {
            // 시군구 필터링 조건이 없으면 전체 목록 반환
            return allCctvList;
        }
        // 메모리에 로드된 전체 목록에서 sigungu 필터링
        return allCctvList.stream()
                .filter(cctv -> {
                    String roadAddr = cctv.get소재지도로명주소();
                    String jibunAddr = cctv.get소재지지번주소();

                    boolean roadMatch = roadAddr != null && roadAddr.contains(sigungu);
                    boolean jibunMatch = jibunAddr != null && jibunAddr.contains(sigungu);

                    return roadMatch || jibunMatch;
                })
                .collect(Collectors.toList());
    }
    // ⭐️ (추가) 특정 동에 설치된 CCTV 대수 계산
    /**
     * 특정 동(시/군/구/동)에 설치된 CCTV 대수 반환
     * @param region 검색할 행정구역 정보 (sigungu와 dong 포함)
     * @return 해당 동의 CCTV 총 대수
     */
    public int getCctvCountByRegion(GeoService.RegionInfo region) {
        if (region.getDong() == null || region.getSigungu() == null) return 0;

        String sigungu = region.getSigungu().trim(); // 예: 미추홀
        String dong = region.getDong().trim();       // 예: 주안동

        int totalCount = 0;

        for (CctvInfo cctv : allCctvList) {
            // 주소에서 시/군/구 필터링 (동일 시/군/구만 대상)
            boolean sigunguMatch = (cctv.get소재지도로명주소() != null && cctv.get소재지도로명주소().contains(sigungu)) ||
                    (cctv.get소재지지번주소() != null && cctv.get소재지지번주소().contains(sigungu));

            if (!sigunguMatch) continue;

            // 주소에서 동(洞) 필터링
            boolean dongMatch = (cctv.get소재지도로명주소() != null && cctv.get소재지도로명주소().contains(dong)) ||
                    (cctv.get소재지지번주소() != null && cctv.get소재지지번주소().contains(dong));

            if (dongMatch) {
                // CCTV 대수를 합산 (카메라대수 필드는 String이므로 Integer로 변환)
                try {
                    int cameras = Integer.parseInt(cctv.get카메라대수());
                    totalCount += cameras;
                } catch (NumberFormatException e) {
                    // 숫자 변환 실패 시 1대로 간주하거나 무시 (여기서는 무시)
                    // System.err.println("카메라 대수 변환 오류: " + cctv.get카메라대수());
                }
            }
        }
        return totalCount;
    }



}