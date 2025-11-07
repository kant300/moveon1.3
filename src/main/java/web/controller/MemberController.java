package web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.JwtService;
import web.service.MemberService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/api/member")  // 공통URL 정의
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity< ? > signup( @RequestBody MemberDto memberDto ){
        boolean result = memberService.signup( memberDto );
        return ResponseEntity.ok( result );
    }

    // 2-2. 로그인(+쿠키 : 클라이언트 브라우저 의 임시 저장소 , 세션:서버 / 쿠키:클라이언트  ) + 토큰
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto , HttpServletResponse response ){
        MemberDto result = memberService.login( memberDto );

        if( result != null ){
            // ******* 쿠키에 저장하는 회원정보를 토큰으로 저장하기 *********
            Cookie cookie = new Cookie( "loginMember", jwtService.createToken( result.getMid() ) );

            // 클라이언트 에서 해당 쿠키를 노출(탈취) 방지 = 주로 민감한정보는 httpOnly 설정한다.
            cookie.setHttpOnly( true ); // .setHttpOnly( true ) : 무조건 http 에서만 사용. 즉] JS로 접근 불가능
            cookie.setSecure( false ); //  HTTP 암호화 : 단 https(true) 에서만 가능 하므로 테스트 단계에서는 false 한다.
            // + 설정
            cookie.setPath("/"); // 쿠키에 접근할수 있는 경로
            cookie.setMaxAge( 60 * 60 ); // 쿠키의 유효기간(초) , 1시간
            response.addCookie( cookie ); // 생성한 쿠키를 클라이언트에게 반환한다.
        }
        return ResponseEntity.ok( result );
    }

    // 3. 현재 로그인된 정보 호출 ( + 마이페이지 )
    @GetMapping("/info")
    public ResponseEntity<?> myInfo( HttpServletRequest request ){ // 쿠키 활용한 로그인상태를 확인
        // 3-1 : 현재 클라이언트(브라우저) 저장된 모든 쿠키 가져오기
        Cookie[] cookies = request.getCookies();
        // 3-2 : 반복문 이용한 특정한 쿠키명 찾기
        if( cookies != null ){ // 만약에 쿠키들이 존재하면
            for( Cookie c : cookies ){ // 하나씩 쿠키들을 반복하여
                if( c.getName().equals( "loginMember") ){ // "loginMember" 쿠키명과 같다면
                    // ******* 쿠키의 저장된 토큰 반환 하기 *********
                    String token = c.getValue();// 쿠키의 저장된 토큰 반환
                    boolean checked = jwtService.checkToken( token ); // 토큰 검증
                    if( checked ) {// 만약에 토큰이 유효하면
                        String mid =jwtService.getMid( token ); // 토큰의 저장된 클레임(회원아이디) 추출하기
                        // 3-3 : 서비스를 호출하여 내정보 조회
                        MemberDto result = memberService.myInfo( mid );
                        return ResponseEntity.ok( result ); // 로그인 상태로 회원정보 조회
                    }//
                    // 만약에 토큰이 유효하지 않으면
                    return ResponseEntity.ok( null ); // 토큰 검증 실패
                }
            }
        }
        return ResponseEntity.ok( null ); // 비로그인 상태 // 쿠키가 없다.
    }

    // 4. 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout( HttpServletResponse response ){
        // 4-1 : 삭제할 쿠키명을 null 값으로 변경한다.
        Cookie cookie = new Cookie( "loginMember" , null );
        cookie.setHttpOnly( true );
        cookie.setSecure( false );
        cookie.setPath("/");
        cookie.setMaxAge( 0 ); // 즉시 삭제 하라는 뜻 : 0초
        response.addCookie( cookie ); // 동일한 쿠키명으로 null 저장하면 기존 쿠키명 사라진다.

        return ResponseEntity.ok( true );
    }

    // 5. 아이디찾기
    @GetMapping("/findid") // http://localhost:8080/api/member/findid?memail=ygun123@test.com&mphone="ygun123@test.com
    public ResponseEntity<?> findId(@RequestParam String memail, @RequestParam String mphone){
        MemberDto dto = new MemberDto();
        dto.setMemail(memail);
        dto.setMphone(mphone);
        System.out.println("MemberController.findId");
        String result = memberService.findId(dto);

        if(result !=null && !result.isEmpty()){
            // 성공시 ID반환
            Map<String, String> response = new HashMap<>();
            response.put("mid", result);
            return ResponseEntity.ok(response);
        }else {
            Map<String, String> error = new HashMap<>();
            error.put("message" , "일치하는 회원정보가 없습니다.");
            return ResponseEntity.status(404).body(error);
        }
    }

    // 6. 비밀번호찾기/재설정
    @PutMapping("/findpwd") // http://localhost:8080/api/member/findpwd  //
    public ResponseEntity<?> findPwd(@RequestBody MemberDto dto){
        boolean result = memberService.findPwd(dto);
        return ResponseEntity.ok(result);
    }


    // 6-1. 비밀번호 찾기용 인증 요청 (이메일 인증 전송)
    @PostMapping("/requestPwdAuth")
    public ResponseEntity<?> requestPwdAuth(@RequestBody MemberDto dto) {
        boolean result = memberService.sendPwdAuthEmail(dto);
        Map<String, Object> response = new HashMap<>();

        if (result) {
            response.put("success", true);
            response.put("message", "인증번호가 이메일로 전송되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "회원 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(400).body(response);
        }
    }

    // 6-2. 인증번호 확인
    @PostMapping("/verifyPwdCode")
    public ResponseEntity<?> verifyPwdCode(@RequestBody Map<String, String> payload) {
        String mid = payload.get("mid");
        String code = payload.get("verifyCode");

        boolean verified = memberService.verifyPwdCode(mid, code);
        Map<String, Object> response = new HashMap<>();

        if (verified) {
            response.put("success", true);
            response.put("message", "인증 완료");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "인증번호가 일치하지 않습니다.");
            return ResponseEntity.status(400).body(response);
        }
    }

    // 7. 회원정보수정
    @PutMapping("/update")
    public ResponseEntity<?> updateInfo(@RequestBody MemberDto dto){
        boolean result = memberService.updateInfo(dto);
        return ResponseEntity.ok(result);
    }
    // 7-1 비밀번호 변경
    @PutMapping("/updatePwd")
    public boolean updatePwd(@RequestBody MemberDto dto, HttpSession session ){
        MemberDto loginMember = (MemberDto) session.getAttribute("loginMember");
        if( loginMember == null ) return false; // 로그인 안된 경우

        // DB의 회원정보 가져오기
        MemberDto dbMember = memberService.getMemberById(loginMember.getMid());
        if( dbMember == null ) return false;

        // 기존 비밀번호 일치여부확인(암호화 비교)
        if(!passwordEncoder.matches(dto.getMpwd(), dbMember.getMpwd())){
            return false;
        }

        // 새 비밀번호 암호화 후 DB 업데이트
        String encodedNewPwd = passwordEncoder.encode(dto.getNewPwd());
        boolean result = memberService.updatePassword(loginMember.getMid(), encodedNewPwd);

        // 세션 갱신
        if (result){
            dbMember.setMpwd(dto.getNewPwd());
            session.setAttribute("loginMember" , dbMember);
        }
        return result;
    }


    // 8. 회원탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request){
        MemberDto loginMember = memberService.myInfo(request);
        if(loginMember == null) return ResponseEntity.ok(false);

        boolean result = memberService.signout(loginMember.getMid());
        return ResponseEntity.ok(result);
    }
}// class e
