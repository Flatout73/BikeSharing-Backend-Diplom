package ru.hse.BikeSharing.Services;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.repo.UserRepo;

import java.net.ConnectException;

@Service
public class NotificationService {

    @Autowired
    UserRepo userRepo;

    ApnsService service = APNS.newService()
            .withCert("src/main/resources/BikeSharingDevPush.p12", "qwerty123")
            .withSandboxDestination()
            .build();


    public ApnsNotification sendPush(Long userID, String text) throws NotFoundException, ConnectException {

        String payload = APNS.newPayload()
                .alertBody(text).build();

        System.out.println("Sending an iOS push notification…");
        
        String token;
        if (userID != null) {
            User user = userRepo.findById(userID).orElseThrow(() -> new NotFoundException("Not found user"));
            token = user.getPushToken();
            
            return service.push(token, payload);
        } else {
            ApnsNotification notification = null;
            for (User user: userRepo.findAll()) {
                token = user.getPushToken();
                notification = service.push(token, payload);
            }
            
            return notification;
        }
    }
}
