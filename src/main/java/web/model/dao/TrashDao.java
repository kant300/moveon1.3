package web.model.dao;

import org.springframework.stereotype.Repository;
import web.model.dto.living.TrashDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrashDao extends Dao{

    // [1] 쓰레기 배출정보 등록 // 쓰레기 배출정보를 입력받아 저장한다.
    public boolean trashAdd(TrashDto trashDto){
        try{// 1. SQL 작성
            String sql = "insert into trash( tCity , tGu , tInfo ) values (? ,? ,?)";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setString(1,trashDto.getTCity());
            ps.setString(2,trashDto.getTGu());
            ps.setString(3,trashDto.getTInfo());
            System.out.println(trashDto.getTCity());
            // 4. SQL 실행
            int count = ps.executeUpdate();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( count >= 1 )return true;
        }catch (Exception e){System.out.println(e);}
        return false;
    } // m end

    // [2-1] 조회할 정보의 개수(페이지네이션)
    public int getTotalCount( int pNo ){
        try{// 1. SQL 작성
            String sql = "select count(*) from trash where pNo = ?";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setInt(1,pNo);
            // 4. SQL 실행
            ResultSet rs = ps.executeQuery();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( rs.next() ){
                return rs.getInt(1); // 첫번째 레코드의 속성값 1개 반환
            }
        }catch (Exception e){System.out.println(e);}
            return 0;
    }

    // [2-2] 쓰레기 배출정보 전체조회	//	모든 쓰레기 배출정보(dto)를  출력한다.
    public List<TrashDto> trashPrint(int pNo, int startRow , int count){
        List<TrashDto> list = new ArrayList<>();
        try{// 1. SQL 작성
            String sql = "select * from trash where pNo = ? order by tNo desc limit ?, ?";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setInt(1,pNo);
            ps.setInt(2,startRow);
            ps.setInt(3,count);
            // 4. SQL 실행
            ResultSet rs = ps.executeQuery();
            // 5. SQL 결과에 따른 로직/리턴/확인
            while ( rs.next() ){
                TrashDto trashDto = new TrashDto();
                trashDto.setPNo(rs.getInt("pNo"));
                trashDto.setTNo(rs.getInt("tNo"));
                trashDto.setTCity(rs.getString("tCity"));
                trashDto.setTGu(rs.getString("tGu"));
                trashDto.setTInfo(rs.getString("tInfo"));
                trashDto.setTDay(rs.getString("tDay"));
                list.add(trashDto);
            }
        }catch (Exception e){System.out.println(e);}
        return list;
    } // m end


    // [3] 쓰레기 배출정보 개별조회 // 특정한 쓰레기 번호로 쓰레기 배출정보 출력한다.
    public TrashDto trashFind( String tCity, String tGu ){
        try{// 1. SQL 작성
            String sql = "select * from trash where tCity = ? and tGu = ? ";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setString(1,tCity);
            ps.setString(2,tGu);
            // 4. SQL 실행
            ResultSet rs = ps.executeQuery();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( rs.next() ){
                TrashDto trashDto = new TrashDto();
                trashDto.setTNo(rs.getInt("tNo"));
                trashDto.setTCity(rs.getString("tCity"));
                trashDto.setTGu(rs.getString("tGu"));
                trashDto.setTInfo(rs.getString("tInfo"));
                trashDto.setTDay(rs.getString("tDay"));
                return trashDto;
            }
        }catch (Exception e){System.out.println(e);}
        return null;
    }

    // [4] 쓰레기 배출정보 삭제	 // 삭제할 쓰레기 번호(tNo)를 입력받아 삭제한다.
    public boolean trashDelete(int tNo){
        try{// 1. SQL 작성
            String sql = "delete from trash where tNo = ?";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setInt(1,tNo);
            // 4. SQL 실행
            int count = ps.executeUpdate();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( count == 1 )return true; // 1이면 삭제성공
        }catch (Exception e){System.out.println(e);}
        return false; // 오류 발생시 삭제실패
    }

    // [5] 쓰레기 배출정보 수정	// 수정할 쓰레기번호 와 배출지역, 배출정보를 수정한다.
    public boolean trashUpdate(TrashDto trashDto){
        try{// 1. SQL 작성
            String sql = "update trash set tCity = ? , tGu = ? , tInfo = ? where tNo = ?;";
            // 2. SQL 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. SQL 매개변수 대입
            ps.setString(1,trashDto.getTCity());
            ps.setString(2,trashDto.getTGu());
            ps.setString(3,trashDto.getTInfo());
            ps.setInt(4,trashDto.getTNo());
            // 4. SQL 실행
            int count = ps.executeUpdate();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( count == 1) return true; // 1이면 수정성공
        }catch (Exception e){System.out.println(e);}
        return false; // 오류 발생시 수정실패
    }


} // class end





















