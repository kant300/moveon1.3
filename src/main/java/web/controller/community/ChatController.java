package web.controller.community;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.model.dto.community.BulkbuygroupDto;
import web.model.dto.community.ChattingDto;
import web.service.MemberService;
import web.service.community.ChatService;
import web.service.community.GroupbulkbuyService;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final MemberService memberService;
    private final GroupbulkbuyService groupbulkbuyService;

    // 메세지 내용 저장 (DB 저장)
    @PostMapping("/write")
    public ResponseEntity<?> writeChat(@RequestBody ChattingDto dto, HttpServletRequest request) {

        MemberDto memberDto = memberService.myInfo(request);

        dto.setMno(memberDto.getMno());
        System.out.println("memberDto = " + memberDto);
        System.out.println("ChatController.saveChat");
        boolean result = chatService.writeChat(dto);
        return ResponseEntity.ok(result);
    }

    // 메세지 내용 출력 어디에? 메세지 입력한 방(cno)에
    @GetMapping("/print")
    public ResponseEntity<?> printChat(@RequestParam int bno , HttpServletRequest request ) {
        System.out.println("ChatController.printChat");

        // 현재 접속한 회원의 접속날짜/시간 가져오기
        MemberDto memberDto = memberService.myInfo( request );
        int mno = memberDto.getMno();

        // ----> 해당 회원이 그룹방에 접속한 날짜/ 시간 구하기
        String joinDate = groupbulkbuyService.getJoinDate( bno , mno );
        System.out.println("joinDate = " + joinDate);

        List<ChattingDto> list = chatService.printChat(bno , joinDate );
        return ResponseEntity.ok(list);

    }



    // 인원 조회
    @GetMapping("/cocheck")
    public ResponseEntity<?> countChat(@RequestParam int bno) {
        System.out.println("bno = " + bno);
        BulkbuygroupDto result = chatService.countChat(bno);
        return ResponseEntity.ok(result);
    }

    // 인원 증가
    @PutMapping("/count/pp")
    public ResponseEntity<?> countpp(int bno) {
        System.out.println("ChatService.countChat");
        // 만약에 증가하기전에 인원이 찼으면 403 이고 성공하면 200
        int result = chatService.countCheck(bno);
        if (result > 0) {
            chatService.countpp(bno);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).body(false);
        }
    }

    // 인원 감소
    @PutMapping("/count/mm")
    public ResponseEntity<?> countmm(@RequestParam int bno) {
        System.out.println("ChatService.countChat");
        int result = chatService.countCheckmm(bno);
        if (result > 0) {
            chatService.countmm(bno);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).body(false);
        }
    }

    // 채팅 참여 인원
    @GetMapping("/play/name")
    public ResponseEntity< ? > playname(@RequestParam int bno) {
        System.out.println("ChatController.playname");
        List<MemberDto> dto = chatService.playname(bno);
        return ResponseEntity.ok(dto);
    }


}

