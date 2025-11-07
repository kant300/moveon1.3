package web.model.mapper.community;

import org.apache.ibatis.annotations.Mapper;

import web.model.dto.community.GroupbulkbuyDto;

import java.util.List;
import java.util.Map;


@Mapper
public interface GroupbulkbuyMapper {

    Integer joincount(Map<String, Object> map);

    void joinGroup(Map<String,Object> map);

    List<GroupbulkbuyDto> myGroups(int mno );

    void leaveGroup(int mno , int bno);

    int playgmnoout(Map<String,Object> maps);

    int roomcheck(Map<String,Object> maps);

    boolean roomlock(int bno);

    String getJoinDate(int bno , int mno);


}
