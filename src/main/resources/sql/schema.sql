-- ===============================
--  MEMBERS TABLE (회원)
-- ===============================

DROP TABLE IF EXISTS group_member;
DROP TABLE IF EXISTS chatting;
DROP TABLE IF EXISTS bulkbuygroup;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS guest_user;

CREATE TABLE members (
    mno INT AUTO_INCREMENT PRIMARY KEY,        -- 회원번호
    mid VARCHAR(50) NOT NULL UNIQUE,           -- 아이디
    mpwd VARCHAR(255) NOT NULL,                 -- 비밀번호
    mname VARCHAR(100) NOT NULL,               -- 닉네임
    mphone VARCHAR(20) NOT NULL UNIQUE,        -- 휴대번호
    memail VARCHAR(100) NOT NULL UNIQUE,       -- 이메일
    maddress1 VARCHAR(50) NOT NULL,            -- 주소(시)
    maddress2 VARCHAR(50) NOT NULL,            -- 주소(구)
    maddress3 VARCHAR(50) NOT NULL,            -- 주소(동)
    wishlist VARCHAR(100) NOT NULL,            -- 관심
    mdate DATE DEFAULT (CURRENT_DATE),         -- 날짜(생성)
    mdateup DATE DEFAULT (CURRENT_DATE)        -- 날짜(수정)
);

-- ===============================
--  BULKBUYGROUP TABLE (소분모임)
-- ===============================
CREATE TABLE bulkbuygroup (
     bno INT AUTO_INCREMENT PRIMARY KEY,        -- 글번호
     mno INT NOT NULL,                          -- 회원번호(FK)
     btitle VARCHAR(255) NOT NULL,              -- 제목
     bcontent VARCHAR(1000),                    -- 내용
     bdate DATE DEFAULT (CURRENT_DATE),         -- 날짜
     btotal INT NOT NULL,                       -- 인원(총)
     bcount INT NOT NULL,                       -- 인원(현재)
     read_only TINYINT(1) DEFAULT 0,            -- 읽기전용 여부 ex) 0=일반 채팅방 , 1=링크 삭제? 안보이게(방장 나감)
     bimg VARCHAR(255) ,
     CONSTRAINT fk_bulk_member FOREIGN KEY (mno)
         REFERENCES members(mno)
         ON DELETE CASCADE
);

-- ===============================
--  CHATTING TABLE (채팅)
-- ===============================
CREATE TABLE chatting (
    cno INT AUTO_INCREMENT PRIMARY KEY,        -- 채팅번호
    mno INT NOT NULL,                          -- 회원번호(FK)
    bno INT NOT NULL,                          -- 글번호(FK)
    mmessage VARCHAR(1000) NOT NULL,           -- 메시지
    cdate DATETIME DEFAULT NOW(),  -- ✅ 기존 DATE → DATETIME 으로 변경
    CONSTRAINT fk_chat_member FOREIGN KEY (mno)
         REFERENCES members(mno)
         ON DELETE CASCADE,
         CONSTRAINT fk_chat_group FOREIGN KEY (bno)
             REFERENCES bulkbuygroup(bno)
             ON DELETE CASCADE
);

-- ===============================
--  GROUP_MEMBER TABLE (소분모임 참여자)
-- ===============================


CREATE TABLE group_member (
    gmno INT AUTO_INCREMENT PRIMARY KEY,       -- 참여 고유번호 (PK)
    mno INT NOT NULL,                          -- 회원번호 (FK)
    bno INT NOT NULL,                          -- 모임번호 (FK)
    joindate DATETIME DEFAULT NOW(),           -- 참여 일시
    leavedate DATETIME NULL,                   -- 퇴장 일시
    active TINYINT(1) DEFAULT 1,               -- 참여 여부 (1=참여중, 0=퇴장)

    CONSTRAINT fk_groupmember_member FOREIGN KEY (mno)
    REFERENCES members(mno)
    ON DELETE CASCADE,

    CONSTRAINT fk_groupmember_group FOREIGN KEY (bno)
        REFERENCES bulkbuygroup(bno)
        ON DELETE CASCADE
);

CREATE TABLE guest_user (
	fno INT AUTO_INCREMENT PRIMARY KEY,  -- 고유 번호
	mno int null , -- 회원
	guestKey varchar(100) not null UNIQUE,
	gaddress1 varchar(100),
	gaddress2 varchar(100),
	gaddress3 varchar(100),
	wishlist varchar(50) null, -- 관심 내역
	createdDate DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일
	updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일
	UNIQUE KEY uq_member (mno , wishlist)        -- 회원 중복 방지
);
