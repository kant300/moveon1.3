//package web.controller.safety;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import web.service.safety.SexCrimeService;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/sexcrime")
//public class SexCrimeController {
//
//    private final SexCrimeService sexCrimeService;
//    @GetMapping("/all")
//    public ResponseEntity<?> getAll() {
//        return ResponseEntity.ok(sexCrimeService.getAllSexCrime());
//    }
//}
