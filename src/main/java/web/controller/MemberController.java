package web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.JwtService;
import web.service.MemberService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/api/member")  // 공통URL 정의
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestHeader(value = "Guest-token" , required = false ) String guesttoken , @RequestBody MemberDto memberDto) {
        boolean result = memberService.signup(memberDto , guesttoken);
        return ResponseEntity.ok(result);
    }

    // 2-2. 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        MemberDto result = memberService.login(memberDto);
        if (result != null) {
            String token = jwtService.createToken(result.getMid(), "LoginMno");

            Map<String, Object> map = new HashMap<>();
            map.put("status", "Login");
            map.put("token", token);
            map.put("member", result);
            return ResponseEntity.ok(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status", "UnLogin");
        map.put("message", "로그인 실패요");
        return ResponseEntity.status(403).body(map);
    }

    // 3. 현재 로그인된 정보 호출 ( + 마이페이지 )
    @GetMapping("/info")
    public ResponseEntity<?> myInfo(@RequestHeader("Authorization") String tokens) {
        // 토큰s가 비어있거나 / 형식이 잘못된 경우
        if (tokens == null || !tokens.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("정보 호출 실패");
        }
        // token 에 값 받아오면 문자열 만큼 뒷자리 자름 7자리
        String token = tokens.substring("Bearer ".length());
        String mid = jwtService.getMid(token); // token mid 값 추출
        MemberDto member = memberService.getMemberById(mid); // 추출한 mid member 조회
        return ResponseEntity.ok(member);
    }

    // 즐겨찾기 수정
    @PutMapping("/wishlist")
    public ResponseEntity<?> updateWishlist(@RequestHeader("Authorization") String tokens, @RequestBody Map<String, String> map) {
        if (tokens == null || !tokens.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰 오류");
        }

        String token = tokens.substring("Bearer ".length());
        String mid = jwtService.getMid(token);

        String wishlist = map.get("wishlist");

        boolean result = memberService.updateWishlist(mid, wishlist);

        return ResponseEntity.ok(Map.of("success", result));
    }


    // 4. 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokens) {
        // 토큰이 비어있거나 형식이 잘못된 경우
        if (tokens == null || !tokens.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(Map.of("LogOutFail", "로그아웃 실패 "));
        }
        // 토큰 7자리 이후 자름
        String token = tokens.substring("Bearer ".length());
        // 맞으면 성공
        return ResponseEntity.ok(Map.of("Logout", "로그아웃성공 "));
    }

    // 5. 아이디찾기
    @GetMapping("/findid") // http://localhost:8080/api/member/findid?memail=ygun123@test.com&mphone="ygun123@test.com
    public ResponseEntity<?> findId(@RequestParam String memail, @RequestParam String mphone) {
        MemberDto dto = new MemberDto();
        dto.setMemail(memail);
        dto.setMphone(mphone);
        System.out.println("MemberController.findId");
        String result = memberService.findId(dto);

        if (result != null && !result.isEmpty()) {
            // 성공시 ID반환
            Map<String, String> response = new HashMap<>();
            response.put("mid", result);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "일치하는 회원정보가 없습니다.");
            return ResponseEntity.status(404).body(error);
        }
    }

    // 6. 비밀번호찾기/재설정
    @PutMapping("/findpwd") // http://localhost:8080/api/member/findpwd  //
    public ResponseEntity<?> findPwd(@RequestBody MemberDto dto) {
        Map<String, Object> response = new HashMap<>();
        boolean result = memberService.findPwd(dto);
        if (result == true) {
            response.put("success", true);
            response.put("message", "비밀번호가 변경되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "비밀번호 변경 실패");
        }
        return ResponseEntity.ok(response);
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
            return ResponseEntity.ok(response);
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
            return ResponseEntity.ok(response);
        }
    }

    // 7. 회원정보수정
    @PutMapping("/update")
    public ResponseEntity<?> updateInfo(@RequestBody MemberDto dto) {
        boolean result = memberService.updateInfo(dto);
        return ResponseEntity.ok(result);
    }

    // 7-1 비밀번호 변경
    @PutMapping("/updatePwd")
    public ResponseEntity<?> updatePwd(@RequestBody MemberDto dto, @RequestHeader("Authorization") String tokens) {
        Map<String, Object> maps = new HashMap<>();
        if (tokens == null || !tokens.startsWith("Bearer ")) {
            return ResponseEntity.ok("비밀번호 변경 실패");
        }
        String token = tokens.substring("Bearer ".length());
        String mid = jwtService.getMid(token);

        // DB의 회원정보 가져오기
        MemberDto dbMember = memberService.getMemberById(mid);
        if (dbMember == null) {
            maps.put("success", false);
            maps.put("message", "회원정보 불러오기 실패");
            return ResponseEntity.ok(maps);
        }
        // 기존 비밀번호 일치여부확인(암호화 비교)
        if (!passwordEncoder.matches(dto.getMpwd(), dbMember.getMpwd())) {
            maps.put("success", false);
            maps.put("message", "기존비밀번호 일치 X");
            return ResponseEntity.ok(maps);
        }
        // 새 비밀번호 암호화 후 DB 업데이트
        String encodedNewPwd = passwordEncoder.encode(dto.getNewPwd());
        boolean result = memberService.updatePassword(mid, encodedNewPwd);
        if (result) {
            maps.put("success", true);
            maps.put("message", "비밀번호 변경 성공");
        } else {
            maps.put("success", false);
            maps.put("message", "비밀번호 변경 실패");
        }
        return ResponseEntity.ok(maps);
    }


    // 8. 회원탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<?> signout(@RequestHeader("Authorization") String tokens) {
        if (tokens == null || !tokens.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body("회원탈퇴 실패");
        }
        String token = tokens.substring("Bearer ".length());
        String mid = jwtService.getMid(token);
        boolean result = memberService.signout(mid);
        return ResponseEntity.ok(result);
    }
}// class e
