package web.model.dto.CCTV;

import lombok.*;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
public class CctvInfo {

    private String 번호;                   //일련번호
    private String 관리기관명;              //관리기관명
    private String 소재지도로명주소;         // 도로명주소
    private String 소재지지번주소;          // 지번주소
    private String 설치목적구분;            // 방범, 시설물 보호등
    private String 카메라대수;              // 카메라 개수
    private String 카메라화소수;            // 예 : 200만화소
    private String 촬영장면정보;            // 예 : 남쪽방향 , 양방향 등
    private String 보관일수;                //예: 30일
    private String 설치연월;                //설치된 연월(YYYY-MM)
    private String 관리기관전화번호;         //문의 전화
    private String WGS84위도;              //위도
    private String WGS84경도;              //경도
}
