package web.controller.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.guest.GuestDto;
import web.service.JwtService;
import web.service.guest.GuestService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
public class GuestController {
    final GuestService guestService;
    final JwtService jwtService;

    // guest add
    @PostMapping("/save")
    public ResponseEntity< ? > guestSave(){
        String guestKey = UUID.randomUUID().toString(); // 랜덤 식별자
        guestService.guestSave(guestKey);

        String token = jwtService.createguestToken(guestKey);
        return ResponseEntity.ok(Map.of("token",token));
    }
    // guest address / wishlist 저장
    @PostMapping("/detail")
    public ResponseEntity< ? > guestDetail(@RequestHeader("Authorization") String tokens, @RequestBody GuestDto guestDto){

        String token = tokens.replace("Bearer ", "");
        String guestKey = jwtService.getGuestKey(token);

        guestService.guestsavedetail(guestKey , guestDto);
        System.out.println("guestKey = " + guestKey);

        return ResponseEntity.ok(true);
    }

}
