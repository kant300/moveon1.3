package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class CctvInfo {
    private String 번호;                // 일련번호
    private String 관리기관명;           // 관리 기관명
    private String 소재지도로명주소;      // 도로명 주소
    private String 소재지지번주소;       // 지번 주소
    private String 설치목적구분;          // 방범, 시설물 보호 등
    private String 카메라대수;           // 카메라 개수
    private String 카메라화소수;         // 예: 200만 화소
    private String 촬영방면정보;         // 예: 남쪽 방향, 양방향 등
    private String 보관일수;            // 예: 30일
    private String 설치연월;            // 설치된 연월 (YYYY-MM)
    private String 관리기관전화번호;      // 문의 전화
    private String 위도;           // 위도
    private String 경도;           // 경도
}
