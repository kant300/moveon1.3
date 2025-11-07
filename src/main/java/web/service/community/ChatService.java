package web.service.community;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.model.dto.MemberDto;
import web.model.dto.community.BulkbuygroupDto;
import web.model.mapper.community.ChatMapper;
import web.model.dto.community.ChattingDto;

import java.util.List;


@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;

    // 메세지 내용 저장
    public boolean writeChat(ChattingDto dto){
        System.out.println("ChatController.writeChat");
        boolean result = chatMapper.writeChat(dto);
        return result;
    }

    // 메세지 내용 출력
    public List<ChattingDto> printChat(int bno , String joindate ){
        System.out.println("ChatService.printChat");
        List<ChattingDto> dtos = chatMapper.printChat(bno , joindate );
        return dtos;
    }

    public BulkbuygroupDto countChat(int bno){
        System.out.println("ChatService.countChat");
        BulkbuygroupDto result = chatMapper.countChat(bno);
        return result;
    }

    public int countpp(int bno){
        System.out.println("ChatService.countChat");
        try {
            int result = chatMapper.countpp(bno);
            return result;
        }catch (Exception e){
            return 0;
        }
    }
    public int countCheck(int bno){
        System.out.println("ChatService.countChat");
        try {
            int result = chatMapper.countCheck(bno);
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    public int countmm(int bno){
        System.out.println("ChatService.countChat");
        try {
            int result = chatMapper.countmm(bno);
            return result;
        }catch (Exception e){
            return 0;
        }
    }

    public int countCheckmm(int bno){
        System.out.println("ChatService.countCheckmm");
        try{
            int result = chatMapper.countCheckmm(bno);
            return result;
        }catch (Exception e) {
            return 0;
        }
    }

    public List<MemberDto> playname(int bno){
        System.out.println("ChatService.playname");
        return chatMapper.playname(bno);
    }

}