package web.model.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.dto.MemberDto;

import static java.time.LocalTime.now;

@Mapper     // 빈 등록
@Repository
public interface MemberMapper {
    // 1. 회원가입
    @Insert("insert into members( mid, mpwd, mname, mphone, memail, maddress1, maddress2, maddress3 ) " +
            "values ( #{mid}, #{mpwd}, #{mname}, #{mphone}, #{memail}, #{maddress1}, #{maddress2}, #{maddress3} )")
    @Options( useGeneratedKeys = true , keyProperty = "mno" )
    public int signup(MemberDto memberDto);

    // 2 : 패스워드를 제외한 아이디로 계정 정보 조회(아이디 중복조회)
    @Select("select count(*) from members where mid = #{mid} ")
   int checkMid( String mid );

    // 3. 로그인용 계정조회, 회원정보 호출
    @Select("select * from members where mid = #{mid}")
    MemberDto myInfo(String mid);

    // 4. 이메일+휴대폰으로 아이디 찾기
    @Select("select mid from members where memail = #{memail} and mphone = #{mphone}")
    String findId(MemberDto dto);

    // 5. 비밀번호찾기 (이메일인증후 비밀번호 재설정)
    @Update("update members set mpwd = #{mpwd}, mdateup = now() where mid = #{mid}")
    int findPwd(MemberDto dto);

    // 5-1. 비밀번호 찾기용 회원 존재 여부 확인
    @Select("SELECT COUNT(*) FROM members WHERE mid = #{mid} AND memail = #{memail}")
    int existsByMidAndEmail(MemberDto dto);

    // 6. 회원정보 수정
    @Update("update members set mname = #{mname}, memail = #{memail}, mphone=#{mphone}, " +
            "maddress1 = #{maddress1}, maddress2 = #{maddress2}, maddress3 = #{maddress3}, mpwd = #{mpwd} " +
            "where mid = #{mid}")
    int updateInfo(MemberDto dto);

    // 비밀번호 변경
    @Update("UPDATE member SET mpwd = #{encodedPwd}, mdateup = now() WHERE mid = #{mid}")
    int updatePassword(@Param("mid") String mid, @Param("newPwd") String newPwd);

    @Select("SELECT * FROM member WHERE mid = #{mid}")
    MemberDto getMemberById(String mid);

    // 7. 회원탈퇴
    @Delete("delete from members where mid = #{mid}")
    int signout(String mid);


}
