package web.model.dto.community;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter@ToString
public class ChattingDto {
    private int cno; // 채팅번호
    private int mno; // 회원번호
    private int bno; // 글번호
    private int host_mno; // 방장
    private String mname; // 회원명
    private String mmessage; // 메세지(내용)
    private String cdate; // 채팅방 기록(날짜)
    private int btotal;
    private int bcount;
}
