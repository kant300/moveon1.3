package web.controller.living;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.service.living.TrashService;

import java.util.Map;

@RestController
@RequestMapping("/living")
@RequiredArgsConstructor
public class TrashController {
    public final TrashService trashService;

    @GetMapping("/trash")
    public Map<String, String> getTrashData(@RequestParam String tCity, @RequestParam String tGu) {
        return trashService.getTrashData(tCity, tGu);
    }
}

















