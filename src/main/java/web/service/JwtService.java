package web.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // [1] 개발자가 비밀키 정의 , 32 글자 이상의 문자열로 구성
    private String secret = "123456789123456789123456789123456789";
    // [2] 개발자가 정의한 비밀키를 이용한 sha-256 알고리즘 적용
    private Key secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    // [3] 토큰 함수
    // [3-1] 토큰 생성 : 회원로그인정보 전용( 아이디, 권한 )
    public String createToken(String mid ) {
        String token = Jwts.builder() // 토큰객체 생성 빌더 함수 시작
                .claim("mid", mid) // mid 라는 key의 로그인성공한 회원아이디 저장
                .setIssuedAt(new Date()) // 발급 날짜/시간 , 현재 시스템(pc) 날짜/시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // HS256 서명 알고리즘 적용
                .compact(); // 토큰 객체 생성 빌더 함수 종료
        System.out.println("token = " + token); // 확인
        return token;
    }
    // [3-2] 토큰 검증
    public boolean checkToken( String token ){
        try {  Jwts.parser()
                .setSigningKey(secretKey) // 서명 검증시 필요한 비밀키 대입
                .build()
                .parseClaimsJws(token); // 검증할 토큰 대입 하여 검증 실행
            return true; // 예외가 발생하지 않으면 검증 성공
        }catch ( JwtException e ){
            return false; // 예외가 발생하면 검증 실패 : 유효기간이 지난 토큰이거나 존재하지 않은 토큰
        }
    }
    // [3-3] 토큰 클레임(값)들 추출
    public Claims getClaims( String token ){
        return Jwts.parser()
                .setSigningKey( secretKey)
                .build()
                .parseClaimsJws( token )
                .getBody(); // 검증에 성공한 토큰의 (저장된데이터)클레임들 반환
    }
    // [3-4] 클레임들의 특정 값 추출 getter 함수
    public String getMid( String token ){
        return getClaims( token ).get( "mid" , String.class );
    }

} // class end
