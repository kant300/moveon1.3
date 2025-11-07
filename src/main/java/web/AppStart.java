package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import web.service.transport.ParkingService;

@SpringBootApplication
public class AppStart {
    public static void main(String[] args) {
        ParkingService p = new ParkingService();
        p.getParkingData();
        SpringApplication.run( AppStart.class );
    // test 용도
    }
}
