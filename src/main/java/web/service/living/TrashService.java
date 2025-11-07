package web.service.living;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TrashService {
    public Map<String, String> getTrashData(String tCity, String tGu) {
        Map<String, String> data = new LinkedHashMap<>();
        try {
            File file = new File("src/main/resources/static/data/12_04_04_E_생활쓰레기배출정보_인천.xlsx");
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // 시도명, 시군구명 체크
            for (int i=0; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell cityCell = row.getCell(1);
                Cell guCell = row.getCell(2);

                if (cityCell.getStringCellValue().equals(tCity) && guCell.getStringCellValue().equals(tGu)) {
                    data.put("배출장소", row.getCell(6).getStringCellValue());
                    data.put("생활쓰레기배출방법", row.getCell(7).getStringCellValue());
                    data.put("음식물쓰레기배출방법", row.getCell(8).getStringCellValue());
                    data.put("재활용품배출방법", row.getCell(9).getStringCellValue());
                    data.put("일시적다량폐기물배출방법", row.getCell(10).getStringCellValue());
                    data.put("일시적다량폐기물배출장소", row.getCell(11).getStringCellValue());
                    data.put("생활쓰레기배출요일", row.getCell(12).getStringCellValue());
                    data.put("음식물쓰레기배출요일", row.getCell(13).getStringCellValue());
                    data.put("재활용품배출요일", row.getCell(14).getStringCellValue());
                    data.put("생활쓰레기배출시작시각", row.getCell(15).getStringCellValue());
                    data.put("생활쓰레기배출종료시각", row.getCell(16).getStringCellValue());
                    data.put("음식물쓰레기배출시작시각", row.getCell(17).getStringCellValue());
                    data.put("음식물쓰레기배출종료시각", row.getCell(18).getStringCellValue());
                    data.put("재활용품배출시작시각", row.getCell(19).getStringCellValue());
                    data.put("재활용품배출종료시각", row.getCell(20).getStringCellValue());
                    data.put("일시적다량폐기물배출시작시각", row.getCell(21).getStringCellValue());
                    data.put("일시적다량폐기물배출종료시각", row.getCell(22).getStringCellValue());
                    data.put("미수거일", row.getCell(23).getStringCellValue());
                    data.put("관리부서명", row.getCell(24).getStringCellValue());
                    data.put("관리부서전화번호", row.getCell(25).getStringCellValue());
                }
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






















