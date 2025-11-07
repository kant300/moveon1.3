package web.service.living;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedicalService {
    public List<Map<String, Object>> getMedicalData() {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            // 데이터 파일 경로
            String path = "src/main/resources/static/data/인천광역시_시설 정보 현황_2021-01-25.csv";
            FileReader fileReader = new FileReader(path, Charset.forName("EUC-KR"));
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                Map<String, Object> item = new LinkedHashMap<>();

                // 확인용 코드
                // System.out.printf("분야 : %s, 유형 : %s, 필드코드 : %s, 타입코드 : %s, 분류코드 : %s,
                //         시설명: %s, 주소 : %s, 전화번호 : %s, 경도 : %s, 위도 : %s\n",
                //         row[1], row[2], row[3], row[4], row[5],
                //         row[8], row[11], row[12], row[13], row[14]);

                // 필드 코드가 6인 것만 가져오기 (복지·건강)
                if (Integer.parseInt(row[3]) == 6) {
                    // 얻은 데이터로 리스트 만들기
                    item.put("유형", row[2]);
                    item.put("시설명", row[8]);
                    item.put("주소", row[11]);
                    item.put("전화번호", row[12]);
                    item.put("경도", row[13]);
                    item.put("위도", row[14]);
                    data.add(item);
                }
            }
            csvReader.close();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
