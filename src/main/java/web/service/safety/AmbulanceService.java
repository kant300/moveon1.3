package web.service.safety;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import web.model.dto.safety.AmbulanceDto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmbulanceService {

    private final List<AmbulanceDto> ambulanceList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {

        try {
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/build/resources/main/static/data/ambulance.csv"; // 업로드한 CSV 경로

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {

                    // -----------------------
                    // 1) CSV 헤더 스킵
                    // -----------------------
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    // -----------------------
                    // 2) 컬럼 분리
                    // -----------------------
                    // 비어있는 값 유지 위해 limit=-1
                    String[] d = line.split(",", -1);

                    // 컬럼 개수 검사 (10개)
                    if (d.length < 10) continue;

                    // -----------------------
                    // 3) trim + 빈 값 NULL 처리
                    // -----------------------
                    for (int i = 0; i < d.length; i++) {
                        d[i] = d[i].trim().isEmpty() ? null : d[i].trim();
                    }

                    // -----------------------
                    // 4) 최종 DTO 생성
                    // -----------------------
                    AmbulanceDto dto = new AmbulanceDto(
                            d[0],  // 시도
                            d[1],  // 지역
                            d[2],  // 주소
                            d[3],  // 업체명
                            d[4],  // 특수
                            d[5],  // 일반
                            d[6],  // 연락처
                            d[7],  // 담당과
                            d[8],  // 담당팀
                            d[9]   // 담당자연락처
                    );

                    ambulanceList.add(dto);
                }

                System.out.println("✅ Ambulance CSV 로드 완료: " + ambulanceList.size() + "건");

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Ambulance CSV 로드 중 오류 발생!");
        }
    }

    public List<AmbulanceDto> getAllAmbulances() {
        return ambulanceList;
    }
}
