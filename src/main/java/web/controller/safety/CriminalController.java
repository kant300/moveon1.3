package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.model.dto.safety.CriminalDto;
import web.service.safety.CriminalService;

import java.util.List;
import java.util.Map;

@RestController // 1. HTTP 요청/응답 자료 매핑 기술
@RequestMapping("/safety/criminal") // 2. HTTP URL 매핑 기술
@RequiredArgsConstructor    // 3. final 변수에 대한 criminalService 자동 생성자 주입 , DI
public class CriminalController {
    // @RequiredArgsConstructor 사용함으로 @Autowired 생략 한다.
    public final CriminalService criminalService;

    // [1] 성범죄자 정보 등록
    @PostMapping("")
    public boolean criminalAdd(@RequestBody CriminalDto criminalDto){
        System.out.println("criminalDto = " + criminalDto);
        return criminalService.criminalAdd( criminalDto );
    }

    // [2] 성범죄자 실제거주지 위도/경도 전체조회
    @GetMapping("")
    public List<Map<String , Object >> criminalPrint() { return criminalService.criminalPrint(); }

    // [3] 성범죄자 정보삭제
    @DeleteMapping("") // http://localhost:8080/safety/criminal?cNo=1
    public boolean criminalDelete( @RequestParam int cNo ){ return  criminalService.criminalDelete( cNo ); }

    // [4] 성범죄자 정보수정
    @PutMapping("")
    public boolean criminalUpdate( @RequestBody CriminalDto criminalDto ){ return criminalService.criminalUpdate( criminalDto ); }


}// class end