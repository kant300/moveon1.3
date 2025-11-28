package web.service.safety;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.model.dto.safety.EmergencyWaterDto;
import web.model.dto.safety.EmergencyWaterResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmergencyWaterService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String serviceKey = "4N0FEL4FE484E52L";

    private final String baseUrl = "https://www.safetydata.go.kr/V2/api/DSSP-IF-00098";

    public List<EmergencyWaterDto> getEmergencyWaterList(){
        try{
            // ⭐ URL 생성
            String url = baseUrl
                    + "?serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8)
                    + "&pageNo=1&numOfRows=1000";

            // API 호출
            String responseStr = restTemplate.getForObject(url, String.class);

            // 전체 Response를 Wrapper로 매핑
            EmergencyWaterResponse response = objectMapper.readValue(responseStr, EmergencyWaterResponse.class);

            List<EmergencyWaterDto> list = new ArrayList<>();

            for( Map<String, Object> item : response.getBody() ){
                EmergencyWaterDto dto = new EmergencyWaterDto();

                dto.setId((String) item.get("FCLT_MNG_NO"));
                dto.setCity((String) item.get("CTPV_NM"));
                dto.setDistrict((String) item.get("SGG_NM"));
                dto.setRoadName((String) item.get("ROAD_NM"));
                dto.setEmd((String) item.get("EMD_NM"));
                dto.setX(Double.parseDouble(item.get("XMAP_CRTS").toString()));
                dto.setY(Double.parseDouble(item.get("YMAP_CRTS").toString()));

                // 좌표 변환: EPSG:5179 → WGS84
                double[] latLng = convert5179ToWgs84(dto.getX(), dto.getY());
                dto.setLat(latLng[0]);
                dto.setLon(latLng[1]);

                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException("비상급수시설 API 호출 실패: " + e.getMessage(), e);
        }
    }
    // 좌표 변환 함수
    public static double[] convert5179ToWgs84(double x, double y) {
        // 실제 EPSG:5179 → WGS84 변환 로직 넣기
        // 테스트용은 그대로 반환
        return new double[]{y / 100000.0, x / 100000.0}; // 예시
    }
}
