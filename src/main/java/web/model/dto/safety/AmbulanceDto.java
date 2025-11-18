package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class AmbulanceDto {
    private String 시도;              // 시·도
    private String 지역;              // 지역(시군구)
    private String 주소;              // 상세 주소
    private String 업체명;            // 병원/기관/업체명
    private String 특수;              // 특수구급차 수
    private String 일반;              // 일반구급차 수
    private String 연락처;            // 대표 전화번호
    private String 담당과;            // 담당 부서(과)
    private String 담당팀;            // 담당 팀명
    private String 담당자연락처;       // 담당자 연락처
}
