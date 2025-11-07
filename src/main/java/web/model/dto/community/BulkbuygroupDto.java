package web.model.dto.community;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter@ToString
public class BulkbuygroupDto {
    private int bno; // 글번호
    private int mno; // 회원번호
    private int host_mno; // 방장
    private String btitle; // 제목
    private String bcontent; // 내용
    private String bdate; // 채팅방 생성 날짜
    private int btotal; // 인원(총)
    private int bcount; // 현재인원
    private String maddress1;
    private String maddress2;
    private String maddress3;
    private int read_only;
    private String bimg;
}
