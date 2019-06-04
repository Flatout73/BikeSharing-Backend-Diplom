package ru.hse.BikeSharing.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.Security.CurrentUser;
import ru.hse.BikeSharing.Security.UserPrincipal;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.repo.UserRepo;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    UserRepo userRepo;

    ApnsService service = APNS.newService()
            .withCert("src/main/resources/BikeSharingDevPush.p12", "qwerty123")
            .withSandboxDestination()
            .build();

    @Autowired
    public NotificationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@CurrentUser UserPrincipal currentUser, @RequestParam("token") String token) {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));
        user.setPushToken(token);

        userRepo.save(user);

        return ResponseEntity.ok("Success");
    }

    @GetMapping("/send")
    public String notification(@RequestParam(required = false) String userId, @RequestParam String text) {

        String token;
        if (userId != null) {
            User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
            token = user.getPushToken();
        } else {
            token = "e18b90bdeeed4953d5006833e2ea2ad30088669df30ecb31c70fc1ae08d0db6f";
        }
        System.out.println("Sending an iOS push notification…");

        String payload = APNS.newPayload()
                .alertBody(text).build();

        System.out.println("payload: "+payload);

        ApnsNotification notification = service.push(token, payload);

        return "Success";
    }
}
