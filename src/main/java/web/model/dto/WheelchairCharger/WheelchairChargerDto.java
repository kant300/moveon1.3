package web.model.dto.WheelchairCharger;
import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class WheelchairChargerDto {

    private String 시설명;
    private String 시도명;
    private String 시군구명;
    private String 시군구코드;
    private String 소재지도로명주소;
    private String 소재지지번주소;
    private String 위도;
    private String 경도;
    private String 설치장소명;
    private String 평일운영시작시각;
    private String 평일운영종료시각;
    private String 토요일운영시작시각;
    private String 토요일운영종료시각;
    private String 공휴일운영시작시각;
    private String 공휴일운영종료시각;
    private String 동시사용가능대수;
    private String 공기주입가능여부;
    private String 휴대전화충전기가능여부;
    private String 관리기관명;
    private String 데이터기준일자;
}