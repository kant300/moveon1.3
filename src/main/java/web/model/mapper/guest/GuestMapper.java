package web.model.mapper.guest;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import web.model.dto.guest.GuestDto;

@Mapper
@Repository
public interface GuestMapper {

    // 게스트 생성
    @Insert("insert into guest_user(guestKey) values ( #{guestKey} )")
    void guestSave(String guestKey);

    // 게스트 주소
    @Update("update guest_user set gaddress1 = #{gaddress1}, gaddress2 = #{gaddress2}, gaddress3 = #{gaddress3} where guestKey = #{guestKey} ")
    void guestsavedetail(String guestKey, String gaddress1, String gaddress2, String gaddress3);

    // 게스트 주소
    @Update("update guest_user set wishlist = #{wishlist} where guestKey = #{guestKey} ")
    void guestDwishlist(String guestKey , String wishlist);

    // 게스트 정보
    @Select("select * from guest_user where guestKey = #{guestKey} ")
    GuestDto guestAddress(String guestKey);

    // 게스트 삭제
    @Delete("delete from guest_user where guestKey = #{guestKey} ")
    void guestDelete(String guestKey);
}