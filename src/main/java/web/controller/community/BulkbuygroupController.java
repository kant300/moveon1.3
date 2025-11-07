package web.controller.community;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.model.dto.community.BulkbuygroupDto;
import web.service.MemberService;
import web.service.community.BulkbuygroupService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class BulkbuygroupController {
    private final BulkbuygroupService bulkbuygroupService;
    private final MemberService memberService;

    // 글쓰기
    @PostMapping("/create")
    public ResponseEntity< ? > createGroup(@RequestBody BulkbuygroupDto dto , HttpServletRequest request){

        MemberDto memberDto = memberService.myInfo(request);

        if (memberDto == null){
            ResponseEntity.status(403).body("로그인 바람");
        }
        dto.setMno(memberDto.getMno());
        System.out.println("memberDto = " + memberDto);
        System.out.println("BulkbuygroupController.createGroup");
        boolean result = bulkbuygroupService.createGroup(dto);
        return ResponseEntity.ok(result);
    }

    // 글 리스트 출력
    @GetMapping("/list")
    public ResponseEntity< ? > listGroup(){
        List<BulkbuygroupDto> dto = bulkbuygroupService.listGroup();
        return ResponseEntity.ok(dto);
    }

    // 소분모임 주소 체크
    @GetMapping("/address")
    public ResponseEntity< ? > addressGroup(@RequestParam Map<String , Object> maps){
        List<BulkbuygroupDto> dto = bulkbuygroupService.addressGroup(maps);
        return ResponseEntity.ok(dto);
    }

    // 조회하기 ( 제목/내용 검색)
    @GetMapping("/listprint")
    public ResponseEntity< ? > listprint(@RequestParam Map<String , Object> maps){
        System.out.println("BulkbuygroupController.listprint");
        List<BulkbuygroupDto> dto = bulkbuygroupService.listprint(maps);
        return ResponseEntity.ok(dto);
    }

    // 본인 모임 기록 조회
    @GetMapping("/join/list")
    public ResponseEntity< ? > joinlist(@RequestParam Map<String , Object> maps ){
        System.out.println("BulkbuygroupController.joinlist");
        List<BulkbuygroupDto> dto = bulkbuygroupService.joinlist(maps);
        return ResponseEntity.ok(dto);
    }


    // 글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity< ? > deleteGroup(@RequestParam int bno){
        boolean result = bulkbuygroupService.deleteGroup(bno);
        return ResponseEntity.ok(result);
    }

    // 글 수정
    @PutMapping("/update")
    public ResponseEntity< ? > updateGroup(@RequestBody BulkbuygroupDto dto){
        boolean result = bulkbuygroupService.updateGroup(dto);
        return ResponseEntity.ok(result);
    }

    // 방 입장 인원
    @PostMapping("/bcno")
    public ResponseEntity< ? > countCheck(@RequestBody Map<String , Object> map){
        int result = bulkbuygroupService.countCheck(map);
        return ResponseEntity.ok(result);
    }
}
