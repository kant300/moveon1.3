package web.controller.community;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.community.MarketService;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarKetController {
    private final MarketService marketService;

//    @PostMapping("")
//    public ResponseEntity< ? > marketCreate(@RequestBody MarketD)
}
