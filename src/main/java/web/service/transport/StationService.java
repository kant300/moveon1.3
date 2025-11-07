package web.service.transport;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StationService {
    // --------------------------- 엘리베이터/에스컬레이터 데이터 관리 --------------------------------------
    // 승강기 파일을 읽어서 데이터 가져오기 (csv)
    public List<Map<String, String>> getCsvLiftData() {
        try {
            // 데이터 파일 경로
            String path = "src/main/resources/static/data/인천교통공사_엘리베이터_에스컬레이터 설치현황_20250630.csv";
            FileReader fileReader = new FileReader(path, Charset.forName("EUC-KR"));
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();
            List<Map<String, String>> csvList = new ArrayList<>();

            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                Map<String, String> item = new LinkedHashMap<>();

                // CSV 데이터에서 뒷부분의 '역' 제거
                row[1] = row[1].replace("역", "");
                // 위도, 경도 값 체크 (값이 없으면 스킵)
                if (row[7].isEmpty() || row[8].isEmpty()) continue;

                // 확인용 코드
                // System.out.printf("역사 : %s, 장비종류 : %s, 호기 : %s, 위도 : %s, 경도 : %s\n",
                //         row[1], row[2], row[3], row[7], row[8]);

                // 얻은 데이터로 리스트 만들기
                item.put("역사", row[1]);
                item.put("장비", row[2].equals("ES")?"에스컬레이터":"엘리베이터");
                item.put("호기", row[3]);
                item.put("위도", row[7]);
                item.put("경도", row[8]);
                item.put("상태", "운행중");
                csvList.add(item);
            }
            csvReader.close();
            return csvList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 승강기 고장현황 사이트 스크래핑 (web), 또는 당일 저장된 자료 활용 (csv)
    public List<Map<String, String>> getWebLiftData() {
        List<Map<String, String>> webList = new ArrayList<>();

        try {
            // 비교를 위한 현재 시간과 파일 위치
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String today = date.format(formatter);

            String path = "src/main/resources/static/data/승강설비_운영고장현황_" + today + ".csv";
            File file = new File(path);

            if (file.exists()) {
                // CSV 파일 읽기
                FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(fileReader);
                List<String[]> csvData = csvReader.readAll();

                for (int i=1; i<csvData.size(); i++) {
                    String[] row = csvData.get(i);
                    Map<String, String> item = new LinkedHashMap<>();

                    // 확인용 코드
                    // System.out.printf("역사 : %s, 장비 : %s, 호기 : %s\n",
                    //         row[0], row[1], row[2]);

                    // 얻은 데이터로 리스트 만들기
                    item.put("역사", row[0]);
                    item.put("장비", row[1]);
                    item.put("호기", row[2]);
                    webList.add(item);
                }
                csvReader.close();
            } else {
                // 웹사이트 크롤링 시 새 창을 띄우지 않도록 설정
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new", "--disable-gpu");
                WebDriver driver = new ChromeDriver(options);

                // 고장현황 URL 주소
                driver.get("https://www.ictr.or.kr/main/railway/guidance/elevator.jsp");
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

                // <td> 태그의 값들을 가져옴
                List<WebElement> webData = driver.findElements(By.tagName("td"));
                List<String> line = new ArrayList<>();

                // 가져온 값들을 리스트에 정리하여 추가
                for (WebElement element : webData) {
                    line.add(element.getText().trim());
                }

                for (int i=0; i<line.size(); i+=6) {
                    Map<String, String> item = new LinkedHashMap<>();
                    item.put("역사", line.get(i+1));
                    item.put("장비", line.get(i+2));
                    item.put("호기", line.get(i+3));
                    webList.add(item);
                }

                // 디버깅용 코드 (문학경기장, 엘리베이터, 3호기를 고장현황 리스트에 추가)
                Map<String, String> item = new LinkedHashMap<>();
                item.put("역사", "문학경기장");
                item.put("장비", "엘리베이터");
                item.put("호기", "3");
                webList.add(item);

                // csv 파일 작성
                CSVWriter csvWriter = new CSVWriter(new FileWriter(path));
                String[] header = {"역사", "장비", "호기"};
                csvWriter.writeNext(header);

                for (Map<String, String> result : webList) {
                    String[] data = {result.get("역사"), result.get("장비"), result.get("호기")};
                    csvWriter.writeNext(data);
                }
                csvWriter.close();
                driver.quit();

                // for (Map<String, String> resultData : webList) {
                //     System.out.println(resultData);
                // }
            }
            return webList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 승강기 상태 체크 (운행중, 수리중) 후 최종 데이터 반환
    public List<Map<String, String>> getLiftData() {
        List<Map<String, String>> csvList = getCsvLiftData(); // CSV 파일 리스트
        List<Map<String, String>> webList = getWebLiftData(); // 웹 스크래핑 데이터 리스트 (Selenium)

        for (int i=0; i<csvList.size(); i++) {
            Map<String, String> csv = csvList.get(i);
            String c1 = csv.get("역사");
            String c2 = csv.get("장비");
            String c3 = csv.get("호기");
            for (int j=0; j<webList.size(); j++) {
                Map<String, String> web = webList.get(j);
                String w1 = web.get("역사");
                String w2 = web.get("장비");
                String w3 = web.get("호기");
                // 역사, 장비, 호기를 비교하여 모두 동일할 시 (고장현황 정보와 일치할 시) '수리중'으로 변경
                if (c1.equals(w1) && c2.equals(w2) && c3.equals(w3)) {
                    csv.put("상태", "수리중");
                }
            }
        }
        // 최종 데이터 예시:
        // [{역사=문학경기장, 장비=엘리베이터, 호기=3, 위도=37.4348231, 경도=126.6969204, 상태=수리중},
        // {역사=선학, 장비=엘리베이터, 호기=3, 위도=37.4270381, 경도=126.6986070, 상태=운행중}]
        // System.out.println(csvList);
        return csvList;
    }

    // --------------------------- 지하철 배차 정보 데이터 관리 --------------------------------------
    // 지하철 역사 위치 데이터 가져오기 (csv)
    public List<Map<String, String>> getStationLocationData() {
        try {
            // 데이터 파일 경로
            String path = "src/main/resources/static/data/서울시_역사마스터_정보.csv";
            FileReader fileReader = new FileReader(path, Charset.forName("EUC-KR"));
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();
            List<Map<String, String>> csvList = new ArrayList<>();

            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                Map<String, String> item = new LinkedHashMap<>();

                // 확인용 코드
                // System.out.printf("역사_ID : %s, 역사명 : %s, 호선 : %s, 위도 : %s, 경도 : %s\n",
                //         row[0], row[1], row[2], row[3], row[4]);

                // 현재는 인천 1호선의 데이터만 가져오도록 설정
                if (row[2].equals("인천1호선")) {
                    // 얻은 데이터로 리스트 만들기
                    item.put("역사명", row[1]);
                    item.put("위도", row[3]);
                    item.put("경도", row[4]);
                    csvList.add(item);
                }
            }
            csvReader.close();

            // 최종 데이터 예시:
            // [{역사명=송도달빛축제공원, 위도=37.407143, 경도=126.62597},
            // {역사명=국제업무지구, 위도=37.399907, 경도=126.630347}]
            // System.out.println(csvList);
            return csvList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 지하철 배차 정보 데이터 가져오기 (xls)
    public List<List<LocalTime>> getScheduleData(String station_name) {
        List<List<LocalTime>> data = new ArrayList<>();
        try {
            // 현재 시각 (시:분), 요일 구하기
            LocalDateTime now = LocalDateTime.now();
            DayOfWeek week = now.getDayOfWeek();
            int weekNum = week.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime time_first = null;
            LocalTime time_second = null;

            File file = new File("src/main/resources/static/data/열차운행_시간표.xls");
            FileInputStream fis = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            // 요일에 따라 loop 구분하기
            if (weekNum >= 1 && weekNum <= 5) {
                // 평일
                for (int index=0; index<=1; index++) {
                    HSSFSheet sheet = workbook.getSheetAt(index);
                    HSSFRow row = sheet.getRow(0);
                    for (int i=0; i<row.getLastCellNum(); i++) {
                        HSSFCell cell = row.getCell(i);
                        // 역 이름이 일치하는 셀 찾기
                        if (cell.getStringCellValue().equals(station_name)) {
                            List<LocalTime> times = new ArrayList<>();
                            for (int j=1; j<sheet.getLastRowNum(); j++) {
                                row = sheet.getRow(j);

                                // 이전 역이 없을 시 중단
                                if (i < 1) continue;

                                // 도착 시간을 구하기 위해 현재 역의 이전 역 시간을 가져오기
                                cell = row.getCell(i-1);

                                // 값이 없는 셀은 건너뛰기
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    // 현재 시간 이후의 시간만 List 로 담기
                                    LocalTime time = LocalTime.parse(cell.getStringCellValue(), formatter);
                                    if (time.isAfter(LocalTime.from(now))) {
                                        times.add(time);
                                    }
                                }
                            }
                            if (times.size() >= 2) {
                                time_first = times.get(0);
                                time_second = times.get(1);
                            }
                            break;
                        }
                    }
                    List<LocalTime> item = new ArrayList<>();
                    item.add(time_first);
                    item.add(time_second);
                    data.add(item);
                }
            } else if (weekNum >= 6 && weekNum <= 7) {
                // 주말
                for (int index=2; index<=3; index++) {
                    HSSFSheet sheet = workbook.getSheetAt(index);
                    HSSFRow row = sheet.getRow(0);
                    for (int i=0; i<row.getLastCellNum(); i++) {
                        HSSFCell cell = row.getCell(i);
                        // 역 이름이 일치하는 셀 찾기
                        if (cell.getStringCellValue().equals(station_name)) {
                            List<LocalTime> times = new ArrayList<>();
                            for (int j=1; j<sheet.getLastRowNum(); j++) {
                                row = sheet.getRow(j);
                                // 도착 시간을 구하기 위해 현재 역의 이전 역 시간을 가져오기
                                cell = row.getCell(i-1);

                                // 값이 없는 셀은 건너뛰기
                                if (cell == null || cell.getStringCellValue().isEmpty()) continue;

                                // 현재 시간 이후의 시간만 List 로 담기
                                LocalTime time = LocalTime.parse(cell.getStringCellValue(), formatter);
                                if (time.isAfter(LocalTime.from(now))) {
                                    times.add(time);
                                }
                            }
                            time_first = times.get(0);
                            time_second = times.get(1);
                            break;
                        }
                    }
                    List<LocalTime> item = new ArrayList<>();
                    item.add(time_first);
                    item.add(time_second);
                    data.add(item);
                }
            }
            workbook.close();
            fis.close();

            // 최종 데이터 예시:
            // [10:47, 10:56]
            // System.out.println(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 주유소 위치 데이터 가져오기 (csv)
    public List<Map<String, Object>> getGasStationData() {
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            // 데이터 파일 경로
            String path = "src/main/resources/static/data/인천광역시 연수구_주유소 현황_20250428.csv";
            FileReader fileReader = new FileReader(path, Charset.forName("EUC-KR"));
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                Map<String, Object> item = new LinkedHashMap<>();

                // 확인용 코드
                // System.out.printf("업소명 : %s, 소재지 : %s, 위도 : %s, 경도 : %s, 전화번호 : %s\n",
                //         row[0], row[1], row[2], row[3], row[4]);

                // 얻은 데이터로 리스트 만들기
                item.put("업소명", row[0]);
                item.put("소재지", row[1]);
                item.put("위도", row[2]);
                item.put("경도", row[3]);
                item.put("전화번호", row[4]);
                data.add(item);
            }
            csvReader.close();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}