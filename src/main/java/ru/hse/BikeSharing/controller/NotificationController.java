package ru.hse.BikeSharing.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void registerUser(@CurrentUser UserPrincipal currentUser, @RequestParam String token) {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));
        user.setPushToken(token);

        userRepo.save(user);
    }

    @GetMapping("/send")
    public String notification(@RequestParam(required = false) String userId) {

        String token;
        if (userId != null) {
            User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
            token = user.getPushToken();
        } else {
            token = "343522a8456ab9bb91280b619b442e42dbb947c2c6e8b2a17bdb6eee99434253";
        }
        System.out.println("Sending an iOS push notification…");

        String payload = APNS.newPayload()
                .alertBody("Сеня пидор").build();
                //.alertTitle("test alert title").build()

        System.out.println("payload: "+payload);

        ApnsNotification notification = service.push(token, payload);

        return "Success";
    }
}
