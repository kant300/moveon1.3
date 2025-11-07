package web.service.WheelchairCharger;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import web.model.dto.WheelchairCharger.WheelchairChargerDto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class WheelchairChargerService {

    private final List<WheelchairChargerDto> chargerList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {
        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "/build/resources/main/static/data/인천광역시_인천데이터포털_전동휠체어급속충전기 정보_20250114.csv";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), Charset.forName("EUC-KR")))) {

            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { // 첫 줄(헤더)은 건너뜀
                    isHeader = false;
                    continue;
                }
                String[] d = line.split(",", -1); // 빈 칸도 인식하도록 -1 옵션
                if (d.length < 20) continue;

                WheelchairChargerDto wc = new WheelchairChargerDto(
                        d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11],
                        d[12], d[13], d[14], d[15], d[16], d[17], d[18], d[19]
                );
                chargerList.add(wc);
            }

            System.out.println("✅ CSV 로드 완료: " + chargerList.size() + "건");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<WheelchairChargerDto> getChargerList(){
        return chargerList;
    }
}