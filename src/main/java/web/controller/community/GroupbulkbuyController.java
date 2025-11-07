package web.controller.community;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Select;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.model.dto.community.GroupbulkbuyDto;
import web.service.community.GroupbulkbuyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groupchat")
@RequiredArgsConstructor
public class GroupbulkbuyController {

    private final GroupbulkbuyService groupbulkbuyService;

    // 회원)mno 이 참여중인 bno 방 저장
    @PostMapping("join/Group")
    public ResponseEntity< ? > joinGroup(@RequestParam int mno , @RequestParam int bno){
        System.out.println("GroupbulkbuyController.joinGroup");
        groupbulkbuyService.joinGroup(mno , bno);
        return ResponseEntity.ok("참여 완료");
    }

    // 내정보 글 목록 출력
    @GetMapping("/my/Group")
    public ResponseEntity< ? > myGroups(@RequestParam int mno){
        System.out.println("ChatController.joinwrite");
        List<GroupbulkbuyDto> list = groupbulkbuyService.myGroups(mno);
        return ResponseEntity.ok(list);
    }

    // 방 퇴장시 내역
    @PutMapping("/leave/Group")
    public ResponseEntity< ? > leaveGroup(@RequestParam int mno , @RequestParam int bno ){
        System.out.println("GroupbulkbuyController.인스턴스 이니셜라이저");
        groupbulkbuyService.leaveGroup(mno , bno);
        return ResponseEntity.ok("퇴장");
    }

    // 방장 방 나가기
    @PutMapping("play/gmnoout")
    public ResponseEntity< ? > playgmnoout(@RequestParam Map<String,Object> maps) {
        System.out.println("GroupbulkbuyController.playgmnoout");

        boolean result =  groupbulkbuyService.playgmnoout(maps);

         Map<String,Object> map = new HashMap<>();

        map.put("gmno", maps.get("gmno"));
        map.put("bno" , maps.get("bno"));
        map.put("msg", result
                    ?
                        "방장님이 나가셨습니다.\n" +
                        "읽기 모드로 변경됩니다.\n" +
                        "채팅방 나가기 클릭시 입장불가합니다."
                : "나가기 실패"
        );
        return ResponseEntity.ok(map);
    }

    // 방장 나가기 후 채팅 읽기 전환
    @PutMapping("room/check")
    public ResponseEntity< ? > roomcheck(@RequestParam Map<String,Object> maps) {
        System.out.println("GroupbulkbuyController.roomcheck");

        boolean result =  groupbulkbuyService.roomcheck(maps);

        Map<String,Object> map = new HashMap<>();

        map.put("gmno", maps.get("gmno"));
        map.put("bno" , maps.get("bno"));
        map.put("status", result ? "locked" : "unlocked" );
        return ResponseEntity.ok(map);
    }
    // 읽기 확인
    @GetMapping("/room/lock")
    public ResponseEntity< ? > roomlock( @RequestParam int bno) {
        System.out.println("GroupbulkbuyController.roomlock");
        boolean readonly = groupbulkbuyService.roomlock(bno);

        Map<String,Object> map = new HashMap<>();
        map.put("read_only" , readonly ? 1 : 0 );
        return  ResponseEntity.ok(map);
    }





}
