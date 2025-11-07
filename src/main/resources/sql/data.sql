-- ===============================
--  MEMBERS 초기 데이터
-- ===============================
INSERT INTO members
(mid, mpwd, mname, mphone, memail, maddress1, maddress2, maddress3, mdate, mdateup)
VALUES ('hong123', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '홍길동', '010-1234-0001',
        'hong123@test.com', '인천광역시', '부평구', '십정동', '2025-10-13', '2025-10-14'),
       ('kimsky', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '김하늘', '010-1234-0002',
        'kimsky@test.com', '인천광역시', '계양구', '작전동', '2025-10-13', '2025-10-14'),
       ('parkyh', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '박영희', '010-1234-0003',
        'parkyh@test.com', '인천광역시', '연수구', '송도동', '2025-10-13', '2025-10-14'),
       ('choijs', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '조지수', '010-1234-0004',
        'choijs@test.com', '인천광역시', '부평구', '부개동', '2025-10-13', '2025-10-14'),
       ('leesmile', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '이미소', '010-1234-0005',
        'leesmile@test.com', '인천광역시', '남동구', '구월동', '2025-10-13', '2025-10-14'),
       ('janghm', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '장한민', '010-1234-0006',
        'janghm@test.com', '인천광역시', '미추홀구', '주안동', '2025-10-13', '2025-10-14'),
       ('jihoon', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '지훈찌', '010-8537-1882',
        'thdwlgns1210@naver.com', '인천광역시', '부평구', '산곡동', '2025-10-13', '2025-10-14'),
       ('kangsh', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '강승현', '010-1234-0008',
        'kangsh@test.com', '인천광역시', '연수구', '송도동', '2025-10-13', '2025-10-14'),
       ('songmj', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '송민정', '010-1234-0009',
        'songmj@test.com', '인천광역시', '중구', '운서동', '2025-10-13', '2025-10-14'),
       ('1313', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '송민종', '010-1234-0010',
        'songmj2@test.com', '인천광역시', '중구', '주안동', '2025-10-13', '2025-10-14'),
       ('test', '$2a$10$69bMrChodVYxOcvM/cUo7evsho3hw6YBJT9yepHudwBlIvi7KlV0.', '테스트', '010-9874-6541', 'test@test.com',
        '인천광역시', '남동구', '주안동', '2025-10-13', '2025-10-14');

-- ===============================
--  BULKBUYGROUP 초기 데이터
-- ===============================

INSERT INTO bulkbuygroup (mno, btitle, bcontent, bdate, btotal, bcount, read_only,bimg)
VALUES (1, '과자 공동 구매', '맛있는 과자 같이 사요', NOW(), 5, 3, 0 , "과자.jpg"),
       (2, '텀블러 공동 구매', '텀블러 싸게 같이 사요', NOW(), 3, 2, 0, "텀블러.jfif"),
       (3, '선물세트 나눔', '남는 선물세트 나눔합니다', NOW(), 6, 4, 1, "과자.jpg"), -- ✅ 방장이 나가서 읽기 전용
       (4, '식재료 나눔', '식재료 같이 구매할 분', NOW(), 4, 3, 0, "식재료.jfif"),
       (5, '책 교환', '읽은 책 교환해요', NOW(), 8, 6, 0, "책.jpg"),
       (6, '물품 공동구매', '생활용품 함께 사요', NOW(), 10, 8, 0, "생활용품.jpg"),
       (7, '의류 공동구매', '겨울 옷 같이 구매해요', NOW(), 5, 3, 0, "의류.jpg"),
       (8, '전자제품 공동구매', '소형가전 같이 구매할 분', NOW(), 7, 6, 0, "전자제품.jpg"),
       (9, '책상 공동 구매', '책상 나눠 사요', NOW(), 6, 5, 0, "책상.jpg");

-- ===============================
--  CHATTING 초기 데이터
-- ===============================
INSERT INTO chatting (mno, bno, mmessage, cdate)
VALUES (1, 1, '안녕하세요. 반갑습니다!', NOW()),
       (2, 2, '텀블러 같이 사요!', NOW()),
       (3, 3, '좋아요!', NOW()),
       (4, 4, '게시글 잘 봤어요.', NOW()),
       (5, 5, '책 교환 원해요!', NOW()),
       (6, 6, '같이 참여하고 싶어요.', NOW()),
       (7, 7, '연락 주세요.', NOW()),
       (8, 8, '전자제품 관심 있습니다.', NOW()),
       (9, 9, '좋은 하루 되세요!', NOW());

-- ===============================
--  GROUP_MEMBER 초기 데이터
-- ===============================

-- 홍길동(user1)이 과자 공동 구매 참여 (방장)
INSERT INTO group_member (mno, bno, active, joindate)
VALUES (1, 1, 1, NOW());

-- 김하늘(user2)이 과자 공동 구매 참여
INSERT INTO group_member (mno, bno, active, joindate)
VALUES (2, 1, 1, NOW());

-- 박영희(user3)이 텀블러 공동 구매 참여
INSERT INTO group_member (mno, bno, active, joindate)
VALUES (3, 2, 1, NOW());

-- 홍길동(user1)이 선물세트 나눔 참여 후 퇴장
INSERT INTO group_member (mno, bno, active, leavedate)
VALUES (1, 3, 0, NOW());
