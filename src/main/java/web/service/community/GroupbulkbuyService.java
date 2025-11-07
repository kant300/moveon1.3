package web.service.community;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.dto.community.BulkbuygroupDto;
import web.model.dto.community.GroupbulkbuyDto;
import web.model.mapper.community.GroupbulkbuyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupbulkbuyService {

    private final GroupbulkbuyMapper groupbulkbuyMapper;

    public void joinGroup(int mno , int bno){
        Map<String , Object> map = new HashMap<>();
        map.put("mno", mno);
        map.put("bno", bno);

        Integer joincount = groupbulkbuyMapper.joincount(map);
        if (joincount != null && joincount == 1) {
            return; // 만약 참여중인 방이 있고 active=1이면 중단
        }

        // 저장
        groupbulkbuyMapper.joinGroup(map);
    }

    public List<GroupbulkbuyDto> myGroups(int mno ){
        List<GroupbulkbuyDto> dto = groupbulkbuyMapper.myGroups(mno );
        return dto;
    }

    public void leaveGroup(int mno , int bno){
        System.out.println("GroupbulkbuyService.leaveGroup");
        groupbulkbuyMapper.leaveGroup(mno , bno);
    }

    // 방장 방 나가기
    public boolean playgmnoout( Map<String,Object> maps) {
        int result = groupbulkbuyMapper.playgmnoout(maps );
        return result > 0;
    }
    // 방장 나가기 후 채팅 읽기 전환
    public boolean roomcheck( Map<String,Object> maps) {
        int result = groupbulkbuyMapper.roomcheck(maps );
        return result > 0;
    }
    // 읽기 확인
    public boolean roomlock(int bno) {
        return groupbulkbuyMapper.roomlock(bno);

    }
    // 회원의 접속날짜/시간 구하기
    public String getJoinDate( int bno , int mno ){
        return groupbulkbuyMapper.getJoinDate( bno , mno );
    }

}
