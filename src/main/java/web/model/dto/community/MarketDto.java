package web.model.dto.community;

import lombok.Data;

@Data
public class MarketDto {
    private int mno; // 회원
    private int kno; // 방번호
    private int kprice; // 가격
    private String ktitle; // 제목
    private String kcontent; // 내용
    private String kcategory; // 카테고리
    private String kstatus; // 거래 상태
    private int klikes; // 좋아요
    private int kviews; // 조회수 (방문자)
    private String ktradeType; // 거래방식 ( 직 , 택배)
    private String kfile;  // 사진
    private String kdate; // 날짜
    private String madress1; // 시 지역
    private String madress2; // 구 지역
    private String madress3; // 동 지역




}
