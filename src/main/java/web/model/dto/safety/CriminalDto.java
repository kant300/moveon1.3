package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Data
public class CriminalDto {
    private int cNo;    // 성범죄자 번호
    private String cName;   // 성범죄자 이름
    private String cAddress;    // 성범죄자 실제거주지
    private String cAddress2;    // 성범죄자 등본주소
    private double latitude;   // 위도
    private double longitude;  // 경도
}
