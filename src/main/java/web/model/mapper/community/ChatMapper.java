package web.model.mapper.community;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import web.model.dto.MemberDto;
import web.model.dto.community.BulkbuygroupDto;
import web.model.dto.community.ChattingDto;

import java.util.List;

@Mapper
public interface ChatMapper {

    // 메세지 내용 저장
    boolean writeChat(ChattingDto dto);
    // 메세지 내용 출력
    List<ChattingDto> printChat(int bno , String joindate );

    BulkbuygroupDto countChat(int bno);



     int countpp(int bno);

    int countCheck( int bno );

    int countCheckmm( int bno );

     int countmm(int bno);

    List<MemberDto> playname(int bno);

}
