package web.model.dto.guest;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GuestDto {
    private int fno; // pk
    private int mno; // 회원
    private String guestKey; // token guest
    private String gaddress1; // 게스트 주소 시
    private String gaddress2; // 게스트 주소 구
    private String gaddress3; // 게스트 주소 동
    private String wishlist; // 관심 내역(즐겨찾기)
    private LocalDateTime createdDate; // 생성일
    private LocalDateTime updatedDate; // 수정일
}
