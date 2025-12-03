package web.service.safety;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.proj4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import web.model.dto.safety.EmergencyWaterDto;
import web.model.dto.safety.EmergencyWaterResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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


                double x = Double.parseDouble(item.get("XMAP_CRTS").toString());
                double y = Double.parseDouble(item.get("YMAP_CRTS").toString());

                dto.setX(x);
                dto.setY(y);

                // ✅ 정확한 좌표 변환: EPSG:5179 → WGS84
                double[] latLng = convert5179ToWgs84(x, y);
                dto.set위도(latLng[0]);   // 위도
                dto.set경도(latLng[1]);   // 경도

                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException("비상급수시설 API 호출 실패: ", e);
        }
    }

    // ✅ EPSG:5179 → WGS84 정확 변환
    public static double[] convert5179ToWgs84(double x, double y) {

        CRSFactory crsFactory = new CRSFactory();

        CoordinateReferenceSystem srcCrs = crsFactory.createFromName("EPSG:5179");
        CoordinateReferenceSystem dstCrs = crsFactory.createFromName("EPSG:4326");

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCrs, dstCrs);

        ProjCoordinate srcCoord = new ProjCoordinate(x, y);
        ProjCoordinate dstCoord = new ProjCoordinate();

        transform.transform(srcCoord, dstCoord);

        double lat = dstCoord.y;
        double lon = dstCoord.x;

        return new double[]{lat, lon};
    }
}
