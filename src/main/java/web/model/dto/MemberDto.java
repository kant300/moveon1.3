package web.model.dto;

import lombok.Data;

@Data
public class MemberDto {
    private int mno;               // 회원번호
    private String mid;            // 아이디
    private String mpwd;           // 비밀번호
    private String mname;          // 닉네임
    private String mphone;         // 휴대번호
    private String memail;         // 이메일
    private String maddress1;      // 주소(시)
    private String maddress2;      // 주소(구)
    private String maddress3;      // 주소(동)
    private String mdate;          // 날짜(생성)
    private String mdateup;        // 날짜(수정)
    private String newPwd;         // 새 비밀번호
    private String newPwdConfirm;   // 새 비밀번호 확인

}
