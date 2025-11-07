package web.service.safety;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ToiletService {
    public List<Map<String, Object>> getToiletData() {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            File file = new File("src/main/resources/static/data/12_04_01_E_공중화장실정보.xlsx");
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, Object> item = new LinkedHashMap<>();

                item.put("화장실명", row.getCell(3).getStringCellValue());
                item.put("소재지도로명주소", row.getCell(4).getStringCellValue());
                item.put("관리기관명", row.getCell(15).getStringCellValue());
                item.put("전화번호", row.getCell(16).getStringCellValue());
                item.put("개방시간상세", row.getCell(18).getStringCellValue());
                item.put("위도", row.getCell(20).getStringCellValue());
                item.put("경도", row.getCell(21).getStringCellValue());
                data.add(item);
            }
            workbook.close();
            fis.close();

            // 최종 데이터 예시:
            // {배출장소=집(가게) 앞, 생활쓰레기배출방법=일반 종량제 봉투로 배출(음식물, 재활용 제외), ...}
            // System.out.println(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
