//package web.service;
//
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import org.springframework.beans.factory.annotation.Value;
//
//@Service
//public class WeatherService {
//
//    // application.properties에서 API 키와 URL을 주입받습니다.
//    @Value("${kma.api.key}")
//    private String authKey;
//
//    @Value("${kma.api.url}")
//    private String apiUrl;
//
//    @Value("${kma.api.auth-key-param}")
//    private String authKeyParam;
//
//    /**
//     * WGS84 위도/경도(lat, lon)를 기상청 격자 좌표(nx, ny)로 변환합니다.
//     * 기상청 API는 GPS 위경도가 아닌 자체 격자 좌표를 사용해야 합니다.
//     */
//    public KmaGrid convertToKmaGrid(double lat, double lon)  {
//        // 기상청에서 사용하는 격자 좌표계 상수
//        final double RE = 6371.00877; // 지구 반경(km)
//        final double GRID = 5.0; // 격자 간격(km)
//        final double SLAT1 = 30.0; // 표준 위도 1
//        final double SLAT2 = 60.0; // 표준 위도 2
//        final double OLAT = 38.0; // 기준 위도
//        final double OLON = 126.0; // 기준 경도
//        final double XO = 43.0; // 원점 X 격자 좌표
//        final double YO = 136.0; // 원점 Y 격자 좌표
//
//        // 각도를 라디안으로 변환하는 함수
//        double DEGRAD = Math.PI / 180.0;
//
//        // 경도/위도 설정 (라디안)
//        double slat1 = SLAT1 * DEGRAD;
//        double slat2 = SLAT2 * DEGRAD;
//        double olat = OLAT * DEGRAD;
//        double olon = OLON * DEGRAD;
//        double latRad = lat * DEGRAD;
//        double lonRad = lon * DEGRAD;
//
//        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
//        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
//        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
//        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
//        double ro = Math.pow(Math.tan(Math.PI * 0.25 + olat * 0.5), sn);
//        ro = RE * sf / ro;
//
//        double ra = Math.pow(Math.tan(Math.PI * 0.25 + latRad * 0.5), sn);
//        ra = RE * sf / ra;
//
//        double theta = lonRad - olon;
//        if (theta > Math.PI) theta -= 2.0 * Math.PI;
//        if (theta < -Math.PI) theta += 2.0 * Math.PI;
//        theta *= sn;
//
//        double x = (ra * Math.sin(theta)) / GRID;
//        double y = (ro - ra * Math.cos(theta)) / GRID;
//
//        int nx = (int) Math.floor(x + XO + 0.5);
//        int ny = (int) Math.floor(y + YO + 0.5);
//
//        return new KmaGrid(nx, ny);
//    }
//
//    // 격자 좌표를 저장하는 내부 클래스
//    private static class KmaGrid {
//        final int nx;
//        final int ny;
//
//        KmaGrid(int nx, int ny) {
//            this.nx = nx;
//            this.ny = ny;
//        }
//    }
//
//    /**
//     * 기상청 API를 활용하여 데이터 가져오기
//     * @param lat 위도 (WGS84)
//     * @param lon 경도 (WGS84)
//     * @return 기상청에서 받은 JSON 문자열
//     */
//
//    public String getWeatherData(double lat, double lon) {
//        try {
//            // 1. 위경도 -> KMA 격자 좌표 변환
//            KmaGrid grid = convertToKmaGrid(lat, lon);
//            int nx = grid.nx;
//            int ny = grid.ny;
//
//            // 2. Base Time 및 Base Date 설정
//            LocalDateTime now = LocalDateTime.now();
//            // 기상청은 매 시 30분 이후에 데이터가 갱신되므로, 안전하게 1시간 전 시간을 기준으로 합니다.
//            LocalDateTime baseDateTime = now.minusHours(1);
//
//            // 발표일자 (base_date, ex: 20251225)
//            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
//            String baseDate = baseDateTime.format(dateFormat);
//
//            // 발표시각 (base_time, ex: 1200, 0600) - 정시(00분) 기준
//            // HHMM 패턴을 사용하며, 분은 00으로 고정합니다.
//            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH00");
//            String baseTime = baseDateTime.format(timeFormat);
//
//            // 3. API URL 구성
//            StringBuilder urlBuilder = new StringBuilder(apiUrl);
//
//
//            urlBuilder.append("?" + URLEncoder.encode(authKeyParam,"UTF-8") + "=" + authKey); // 인증키
//            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지번호
//            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); // 한 페이지 결과 수
//            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 요청자료형식(XML/JSON) Default: XML
//
//            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 발표일자
//            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 발표시각
//
//            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + nx); // 예보지점의 X 좌표값 (격자 좌표)
//            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + ny); // 예보지점의 Y 좌표값 (격자 좌표)
//
//            URL url = new URL(urlBuilder.toString());
//
//            // 4. API 요청 및 응답 처리
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("KMA API Response code: " + responseCode);
//
//            BufferedReader rd;
//            if(responseCode >= 200 && responseCode <= 300) {
//                // 성공 (200 OK)
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                // 에러 발생
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                // 실제 서비스에서는 에러 코드를 분석하여 더 자세한 예외를 던지는 것이 좋습니다.
//                System.err.println("API Error: " + responseCode);
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//
//            rd.close();
//            conn.disconnect();
//            return sb.toString();
//
//        } catch (Exception e) {
//            // API 호출 실패 시 구체적인 RuntimeException을 던집니다.
//            throw new RuntimeException("기상청 API 호출 중 오류가 발생했습니다.", e);
//        }
//    }
//}