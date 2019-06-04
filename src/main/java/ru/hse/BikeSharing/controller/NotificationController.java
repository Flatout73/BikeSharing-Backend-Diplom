package ru.hse.BikeSharing.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.Security.CurrentUser;
import ru.hse.BikeSharing.Security.UserPrincipal;
import ru.hse.BikeSharing.Services.NotificationService;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.repo.UserRepo;

import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class NotificationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    NotificationService notificationService;


    @PostMapping("/api/notification/register")
    public ResponseEntity<String> registerUser(@CurrentUser UserPrincipal currentUser, @RequestParam String token) {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));
        user.setPushToken(token);

        userRepo.save(user);

        return ResponseEntity.ok("Success");
    }

    @GetMapping("/send")
    public ApnsNotification notification(@RequestParam(required = false) Long userId, @RequestParam String text) throws ConnectException {
        return notificationService.sendPush(userId, text);
    }
}
