package web.model.dto.safety;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data@NoArgsConstructor@AllArgsConstructor
public class EmergencyWaterResponse {

    private Header header;
    private int numOfRows;
    private int pageNo;
    private int totalCount;

    private List<Map<String, Object>> body;

    // 내부 Header DTO
    @Data
    public static class Header {
        private String resultMsg;
        private String resultCode;
        private String errorMsg;
    }

}
