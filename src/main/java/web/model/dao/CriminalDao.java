package web.model.dao;

import org.springframework.stereotype.Repository;
import web.model.dto.safety.CriminalDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CriminalDao extends Dao{
    // [1] 성범죄자 정보 등록
    public boolean criminalAdd( CriminalDto criminalDto){
        try { // 1. sql 작성한다.
            String sql = " insert into criminal( cName, cAddress , cAddress2 , latitude,  longitude ) values( ?,?,?,?,? )";
            // 2. sql 기재한다.
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. sql 매개변수 대입
            ps.setString(1, criminalDto.getCName());
            ps.setString(2, criminalDto.getCAddress());
            ps.setString(3, criminalDto.getCAddress2());
            ps.setDouble(4, criminalDto.getLatitude());
            ps.setDouble(5, criminalDto.getLongitude());
            // 4. sql 실행
            int count = ps.executeUpdate();
            // 5. SQL 결과에 따른 로직/리턴/확인
            if( count == 1 ) return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // [2] 성범죄자 실제거주지 위도/경도 전체조회
    public List< Map<String, Object >> criminalPrint(){
        List<Map<String , Object>> list = new ArrayList<>();
        try{ // 1.sql 작성한다.
            String sql = "select latitude, longitude , cAddress from criminal"; // 실거주지 주소 , 위도, 경도만 조회
            // 2. sql 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. sql 매개변수 대입 // 매개변수 없음
            // 4. sql 실행
            ResultSet rs = ps.executeQuery();
            // 5. sql 결과에 따른 로직/리턴/확인
            while( rs.next()){
                Map<String , Object> map = new HashMap<>();
                map.put("latitude", rs.getDouble("latitude"));
                map.put("longitude", rs.getDouble("longitude"));
                map.put("cAddress" , rs.getString("cAddress"));
                list.add( map); // 리스트에 저장
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // [3] 성범죄자 정보삭제
    public boolean criminalDelete( int cNo ){
        try{ // 1. sql 작성한다.
            String sql = "delete from criminal where cNo = ?";
            // 2. sql 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. sql 매개변수 대입
            ps.setInt( 1, cNo );
            // 4. sql 실행
            int count = ps.executeUpdate();
            // 5. sql 결과에 따른 로직/리턴/확인
            if( count == 1 ) return  true;  // 1이면 삭제성공
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // [4] 성범죄자 정보수정
    public boolean criminalUpdate( CriminalDto criminalDto ){
        try{ // 1. sql 작성한다
            String sql = "update criminal set cName = ? , cAddress = ? , cAddress2 = ? latitude = ? , longitude = ? , where cNo = ?";
            // 2. sql 기재
            PreparedStatement ps = conn.prepareStatement(sql);
            // 3. sql 매개변수 대입
            ps.setString( 1, criminalDto.getCName());
            ps.setString( 2, criminalDto.getCAddress());
            ps.setString( 3, criminalDto.getCAddress2());
            ps.setDouble( 4, criminalDto.getLatitude());
            ps.setDouble( 5, criminalDto.getLongitude());
            ps.setInt( 6, criminalDto.getCNo());
            // 4. sql 실행
            int count = ps.executeUpdate();
            // 5. sql 결과에 따른 로직/리턴/확인
            if( count == 1 ) return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }



}// class end