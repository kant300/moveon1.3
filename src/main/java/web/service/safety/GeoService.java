package web.service.safety;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {
    private final String KAKAO_REST_API_KEY = "d9225b3aea1025f0b502b5c3547b13db";

    public RegionInfo reverseGeo(double lat, double lng) {

        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="
                + lng + "&y=" + lat;

        RestTemplate rest = new RestTemplate();

        var headers = new org.springframework.http.HttpHeaders();
        headers.add("Authorization", "KakaoAK " + KAKAO_REST_API_KEY);

        var entity = new org.springframework.http.HttpEntity<>(headers);

        var response = rest.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );

        // ★ 간단한 JSON 파싱 (실서비스에서는 DTO 매핑 권장)
        String body = response.getBody();

        String sido = extract(body, "\"region_1depth_name\":\"", "\"");
        String sigungu = extract(body, "\"region_2depth_name\":\"", "\"");
        String dong = extract(body, "\"region_3depth_name\":\"", "\"");

        return new RegionInfo(sido, sigungu, dong);
    }

    // 문자열에서 값 추출하는 단순 파서
    private String extract(String text, String start, String end) {
        int s = text.indexOf(start);
        if (s == -1) return null;
        s += start.length();
        int e = text.indexOf(end, s);
        if (e == -1) return null;
        return text.substring(s, e);
    }

    @Data
    public static class RegionInfo {
        private final String sido;
        private final String sigungu;
        private final String dong;
    }
}
