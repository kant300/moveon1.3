package web.controller.safety;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.safety.AmbulanceSevice;

@RestController
@RequestMapping("/ambulance")
@RequiredArgsConstructor
public class AmbulanceController {
    private final AmbulanceSevice ambulanceSevice;


}
