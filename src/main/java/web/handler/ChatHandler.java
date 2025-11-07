package web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import web.model.dto.community.BulkbuygroupDto;
import web.model.dto.community.ChattingDto;
import web.service.community.ChatService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // private final MemberService memberservice;  멤버 서비스 만들편 // 풀기
    // 리스트 player 명단목록 // ConcurrentHashMap  Hashtable 차이  Hashtable 구식 , 안전하게 사용하려면 ConcurrentHashMap 권장
    private static final Map<String, List<WebSocketSession>> player = new ConcurrentHashMap<>();

    // 채팅방 @after 치면나옴 클라이언트 실행시 시작되는 메소드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Client Start");
    } // afterEstablished end

    // 채팅방 @close 치면 나옴 , 클라이언트 종료시 실행되는 메소드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Client stop");
        // 클아이언트 정보
        String bno = (String) session.getAttributes().get("bno"); // 접속 채팅방 번호
        String mno = (String) session.getAttributes().get("mno"); // 접속 회원 번호
        String mname = (String) session.getAttributes().get("mname"); // 회원 이름

        if (bno != null || mno != null) {
            // 방 접속 목록 가져와
            List<WebSocketSession> list = player.get(bno);
            // 세션 없애기
            list.remove(session);
            // 방 나기기 알림
            alarmMessage(bno, mname + "님이 방을 나가셨습니다. ");
            // dto에서 bno가 여기선 문자열이기때문에 인트로 받기위해 사용하여 countChat 인원 조회 서비스에서 가져옴
            //  conutinfo로 반환 후 메소드 내 방번호의 get ( bcount , btotal ) 를 가져와 사용
            BulkbuygroupDto countInfo = chatService.countChat(Integer.parseInt(bno));
            countchecking(bno , countInfo.getBcount() , countInfo.getBtotal() );
        }
    } // afterClosed end

    // 인월 실시간 확인
    public void countchecking(String bno , int bcount , int btotal ) throws Exception {
        Map<String , Object > msg = new HashMap<>();
        msg.put("type", "count"); // 타입 구분
        msg.put("bcount", bcount); // 인원(현재)
        msg.put("btotal", btotal); // 총 인원

        String retime = objectMapper.writeValueAsString(msg); // 자바( obj )  , json 문자열 변환
        // 목록에 있는 방번호 를 불러와서
        List<WebSocketSession> sessions = player.get(bno);
        // 불러온 방번호가 null이 아니면
        if (sessions != null ){
            // 반복문을사용하여 count , total 클라이언트 전송
            for (WebSocketSession client : sessions){
                client.sendMessage(new TextMessage(retime));
            }

        }
        // writeValueAsString() → Java → JSON 문자열로 변환
        // readValue() → JSON 문자열 → Java 객체로 변환
    }

    // @handle 치면 뜸  클라이언트 실행 후 메세지 보내기
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message");
        System.out.println("session = " + session + ", message = " + message);
        Map<String, Object> msg = objectMapper.readValue(message.getPayload(), Map.class); // type 별로 분리 시켜줘  ex) 챗 1 , 내용 : 안녕 , 이름 : 길동
        String type = (String) msg.get("type"); // 전송 타입(join, msg 등)

        if ("join".equals(type)) {
            //  방 입장 처리
            String mmessage = (String) msg.get("mmessage"); // (입장 시 메시지는 없을 수 있음)
            String mname = (String) msg.get("mname");       // 회원 이름
            String mno = String.valueOf(msg.get("mno"));    // 회원 번호(숫자 → 문자열 변환)
            String bno = String.valueOf(msg.get("bno"));    // 방 번호(숫자 → 문자열 변환)
            session.getAttributes().put("bno", bno);
            session.getAttributes().put("mno", mno);
            session.getAttributes().put("mname", mname);
            // 세션 저장 채팅방 인원들 저장하기위해 사용 put ,덮어쓰기 ,추가수정
            if (player.containsKey(bno)) { // 만약에 방번호가 존재하면
                player.get(bno).add(session); // 방번호 세션 추가
            } else { // 존재하지 않을 시
                List<WebSocketSession> list = new Vector<>();
                list.add(session); // 새로운 세션 추가
                player.put(bno, list); // 새로운 리스트 방 생성후  세션 목록에 추가
            }
            alarmMessage(bno, mname + "님이 방을 입장하셨습니다. ");
            BulkbuygroupDto countinfo = chatService.countChat(Integer.parseInt(bno));
            countchecking(bno, countinfo.getBcount(), countinfo.getBtotal());

        } else if (msg.get("type").equals("msg")) {
            // 채팅방에게 받은 모든 세션들에게 받은 메세지(내역) 보내기
            String bno = (String) session.getAttributes().get("bno");
            String mno = (String) session.getAttributes().get("mno");
            String mmessage = String.valueOf(msg.get("mmessage"));

            // DB 내용 기록
            ChattingDto dto = new ChattingDto();
            dto.setBno(Integer.parseInt(bno));
            dto.setMno(Integer.parseInt(mno));
            dto.setMmessage(mmessage);
            chatService.writeChat(dto);
            // 반복 돌려서 client 에 방번호 목록 가져와
            for (WebSocketSession client : player.get(bno)) {
                client.sendMessage(message); // 메세지 내보내기
            }
            System.out.println("ChatHandler.handleTextMessage");
            System.out.println(player); // 확인용
        } else if ("leave".equals(type)) {
            String bno = (String) msg.get("bno");
            String mname = (String) msg.get("mname");
            String mmessage = (String) msg.get("message");

            alarmMessage(bno, mmessage);

            List<WebSocketSession> list = player.get(bno);
            if (list != null) list.remove(session);

        } else if ("alarm".equals(type)) {
            String bno = (String) msg.get("bno");
            String mmessage =  String.valueOf(msg.get("message"));
            alarmMessage(bno , mmessage);

        }
    } // TextMessage end
    

    public void alarmMessage(String bno, String mmessage) throws Exception {
        // String cno  몇번방 // String mmessage 무슨 메세지를?
        // 예외 던지기
        // map 보내고자 하는 정보 작성
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "alarm");
        msg.put("message", mmessage);
        // map 타입 json 으로 변환
        String jmsg = objectMapper.writeValueAsString(msg);
        // 같은방 모든 메세지 내용 알림 보내기
        for (WebSocketSession session : player.get(bno)) {
            session.sendMessage(new TextMessage(jmsg));
        }
    } // alarmMessage end

} // class end
