package web.service.community;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.model.dto.community.BulkbuygroupDto;
import web.model.mapper.community.BulkbuygroupMapper;
import web.model.mapper.community.GroupbulkbuyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BulkbuygroupService {

    private final BulkbuygroupMapper bulkbuygroupMapper;
    private final GroupbulkbuyMapper groupbulkbuyMapper; // 방장 보여주기위함

    // 글쓰기
    public boolean createGroup(BulkbuygroupDto dto){
        System.out.println("BulkbuygroupService.createGroup");
        boolean result = bulkbuygroupMapper.createGroup(dto);
        if (result) {
            Map<String, Object> map = new HashMap<>();
            map.put("mno", dto.getMno()); // 회원번호
            map.put("bno", dto.getBno()); // 방번호
            groupbulkbuyMapper.joinGroup(map); // 재사용
        }
        return result;
    }

    // 글 리스트 출력
    public List<BulkbuygroupDto> listGroup(){
        List<BulkbuygroupDto> dto = bulkbuygroupMapper.listGroup();
        return dto;
    }

    public List<BulkbuygroupDto> joinlist(Map<String , Object> maps){
        List<BulkbuygroupDto> dto = bulkbuygroupMapper.joinlist(maps);
        return dto;

    }

    // 소분모임 주소 체크
    public List<BulkbuygroupDto> addressGroup(Map<String , Object> maps){
        return bulkbuygroupMapper.addressGroup(maps);

    }

    // 제목/내용 검색하기
    public List<BulkbuygroupDto> listprint(Map<String , Object> maps){
        List<BulkbuygroupDto> dto = bulkbuygroupMapper.listprint(maps);
        return dto;
    }


    // 글삭제
    public boolean deleteGroup(int bno){
        boolean result = bulkbuygroupMapper.deleteGroup(bno);
        return result;
    }

    // 글 수정
    public boolean updateGroup(BulkbuygroupDto dto){
        boolean result = bulkbuygroupMapper.updateGroup(dto);
        return result;
    }

    // 방입장시 인원+1
    public int countCheck( Map<String , Object> map){
        int result = bulkbuygroupMapper.countCheck(map);
        if (result==1) return 1;
        return 0;
    }
}
