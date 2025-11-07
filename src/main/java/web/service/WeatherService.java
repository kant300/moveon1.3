package web.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    // 기상청 API를 활용하여 데이터 가져오기
    public String getWeatherData(int lat, int lon) {
        try {
            // 현재 시간 구하기
            LocalDateTime now = LocalDateTime.now();
            // 현재 날짜 (ex : 20251225)
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 현재 시간, 시 단위 (ex : 1200, 0600)
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(now.getHour()>=10?"HH00":"0H00");

            // 기상청 API 주소
            StringBuilder urlBuilder = new StringBuilder("https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("authKey","UTF-8") + "=yYgQKKE3SxeIECihN0sXRQ"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(now.format(dateFormat), "UTF-8")); /*‘21년 6월 28일 발표*/
            // fcstTime (필요한 시간)을 구하기 위해 현재 시간에서 1시간을 뺀 시간을 매개변수로 전달
            // 30분마다 최신화되기에 원하는 정보를 얻기 위함임. 예시로 16시 30분에 요청하면 17시 이후 정보만 출력되기에 15시에 요청하도록 하여 16시 이후 정보를 얻는 것
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(now.minusHours(1).format(timeFormat), "UTF-8")); /*06시 발표(정시단위) */
            // 예보지점의 x, y 좌표값은 JS 의 Geolocation 을 통해 얻은 위도, 경도 좌표값을 대입해야 함 (현재 위치에 따른 기상 정보 요청)
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + lat); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + lon); /*예보지점의 Y 좌표값*/
            URL url = new URL(urlBuilder.toString());

            // API 요청
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
