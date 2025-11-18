package web.model.mapper.guest;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import web.model.dto.guest.GuestDto;

@Mapper
@Repository
public interface GuestMapper {

    // 게스트 생성
    @Insert("insert into guest_user(guestKey) values ( #{guestKey} )")
    void guestSave(String guestKey);

    // 게스트 주소
    @Update("update guest_user set wishlist = #{wishlist}, gaddress1 = #{gaddress1}, gaddress2 = #{gaddress2}, gaddress3 = #{gaddress3} where guestKey = #{guestKey} ")
    void guestsavedetail(String guestKey, String gaddress1, String gaddress2, String gaddress3, String wishlist);
}