package web.model.dto.safety;

import lombok.Data;

@Data
public class EmergencyWaterDto {

    private String id;       // 시설관리번호
    private String city;     // 시도명
    private String district; // 시군구명
    private String roadName; // 도로명
    private String emd;      // 읍면동명

    private double x; // XMAP_CRTS
    private double y; // YMAP_CRTS

    private double lat;       // WGS84 위도
    private double lon;       // WGS84 경도

}
