package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class AmbulanceDto {
    private String province;              // 시·도
    private String region;              // 지역(시군구)
    private String address;              // 상세 주소
    private String companyName;            // 병원/기관/업체명
    private String special;              // 특수구급차 수
    private String general;              // 일반구급차 수
    private String contact;            // 대표 전화번호
    private String department;            // 담당 부서(과)
    private String team;            // 담당 팀명
    private String officerContact;       // 담당자 연락처
}
