package web.service.transport;

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
public class ParkingService {
    public List<Map<String, Object>> getParkingData() {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            File file = new File("src/main/resources/static/data/오픈데이터_공영주차장_정보.xlsx");
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, Object> item = new LinkedHashMap<>();

                item.put("name", row.getCell(2).getStringCellValue());
                item.put("long", row.getCell(3).getNumericCellValue());
                item.put("lat", row.getCell(4).getNumericCellValue());
                data.add(item);
            }
            workbook.close();
            fis.close();

            // 최종 데이터 예시:
            // [{name=용현1,4동제1노상주차장 경인남길30번길 100-8, long=126.7052992, lat=37.45613149}, ...}]
            // System.out.println(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
