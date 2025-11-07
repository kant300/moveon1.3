package web.model.dto.living;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor
@Data
public class TrashDto {
    private int pNo;        // 페이지네이션 번호 // 생략가능 디폴트 값 1 설정
    private int tNo;        // 쓰레기 배출 정보 번호 // 생략가능, 자동번호 설정
    private String tCity;   // 배출지역시
    private String tGu;     // 배출지역구
    private String tInfo;   // 배출 정보
    private String tDay;    // 정보 등록일 // 생략가능, 디폴트 값 NOW()로 설정
};




