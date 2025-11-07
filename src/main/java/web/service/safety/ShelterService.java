package web.service.safety;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class ShelterService {
    public List<Map<String, Object>> getShelterData() {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            File file = new File("src/main/resources/static/data/12_04_12_E_민방위대피시설정보.xlsx");
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, Object> item = new LinkedHashMap<>();

                // 상태가 사용 중지가 아닌 데이터만 확인
                if (!row.getCell(4).getStringCellValue().equals("사용중지")) {
                    item.put("시설명", row.getCell(5).getStringCellValue());
                    item.put("위도", row.getCell(22).getStringCellValue());
                    item.put("경도", row.getCell(23).getStringCellValue());
                    data.add(item);
                }
            }
            workbook.close();
            fis.close();

            // 최종 데이터 예시:
            // [{시설명=용현엑슬루타워 지하주차장(1층~3층), 위도=37.45794676983926, 경도=126.63756377041193}, ...]
            // System.out.println(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
