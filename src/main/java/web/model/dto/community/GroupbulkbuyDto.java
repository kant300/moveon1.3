package web.model.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class GroupbulkbuyDto {
    private int gmno;        //  참여 고유번호 (group_member PK)
    private int mno;         // 회원 번호 (FK)
    private int bno;         // 모임 번호 (FK)
    private int host_mno;    // 방장

    // JOIN용 (bulkbuygroup, members)
    private String mname;    // 회원 이름
    private String btitle;   // 모임 제목
    private int btotal; // 총 인원
    private int bcount; // 현재 인원

    // group_member 자체 컬럼
    private String joindate; // 참여 일시
    private String leavedate; //  퇴장 일시 (NULL 가능)
    private int active;      //  참여 여부 (1=참여중, 0=퇴장)

//    // 방 여부 / 일반 | 읽기
//    private int read_only; // 0 일반 : 1 읽기
}
