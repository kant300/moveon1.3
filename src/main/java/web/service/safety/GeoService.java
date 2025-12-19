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
        System.out.println("body = " + body);

        // 1. 지번 주소 블록 전체를 먼저 추출합니다.
        String addressBlock = extract(body, "\"address\":{", "}}]}");

        // 2. 시도, 시군구는 도로명 주소(road_address)에서 가져와도 무방하거나,
        //    addressBlock에서 가져옵니다. 여기서는 addressBlock에서 추출합니다.
        String sido = extract(addressBlock, "\"region_1depth_name\":\"", "\"");
        String sigungu = extract(addressBlock, "\"region_2depth_name\":\"", "\"");

        // 3. 읍면동(dong)을 addressBlock 내에서 추출합니다.
        String dong = extract(addressBlock, "\"region_3depth_name\":\"", "\"");

        System.out.println("sido = " + sido);
        System.out.println("sigungu = " + sigungu);
        System.out.println("dong = " + dong);


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
