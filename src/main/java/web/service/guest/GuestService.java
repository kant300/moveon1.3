package web.service.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.model.dto.guest.GuestDto;
import web.model.mapper.guest.GuestMapper;

@Service
@RequiredArgsConstructor
public class GuestService {
    final GuestMapper guestMapper;

    public void guestSave(String guestKey){
        guestMapper.guestSave(guestKey);
    }

    public void guestsavedetail(String guestKey ,GuestDto guestDto){
        guestMapper.guestsavedetail(
                guestKey ,
                guestDto.getGaddress1() ,
                guestDto.getGaddress2() ,
                guestDto.getGaddress3() ,
                guestDto.getWishlist());
    }
}
