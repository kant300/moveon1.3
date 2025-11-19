package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class SexCrimeDto {

    private String chgBfrCtpvNm;    // 시도
    private String chgBfrSggNm; // 시군구
    private String chgBfrUmdNm; // 읍면동
}
