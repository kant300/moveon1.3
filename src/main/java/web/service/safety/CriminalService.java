package web.service.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.model.dao.CriminalDao;
import web.model.dto.safety.CriminalDto;

import java.util.List;
import java.util.Map;

@Service  // 비지니스 로직을 처리
@RequiredArgsConstructor // 롬복제공 : final 변수에 대한 생성자 자동 제공
public class CriminalService {
    // @RequiredArgsConstructor 사용시 ( @Autowired 생략해도 자동으로 의존성이 처리된다.)
    private final CriminalDao criminalDao;

    // [1] 성범죄자 정보 등록
    public boolean criminalAdd(CriminalDto criminalDto ){
        System.out.println("criminalDto = " + criminalDto);
        return criminalDao.criminalAdd( criminalDto );
    }

    // [2] 성범죄자 실제거주지 위도,경도 전체조회
    public List<Map<String , Object >> criminalPrint() { return  criminalDao.criminalPrint(); }

    // [3] 성범죄자 정보삭제
    public boolean criminalDelete( int cNo ){ return  criminalDao.criminalDelete( cNo ); }

    // [4] 성범죄자 정보수정
    public boolean criminalUpdate( CriminalDto criminalDto ){ return criminalDao.criminalUpdate( criminalDto ); }
}