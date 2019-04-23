package ru.hse.BikeSharing.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.BikeSharing.NotFoundException;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.UserRepo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

@RestController
public class MainController {


    private static final JacksonFactory jacksonFactory = new JacksonFactory();

    UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/tokensignin")
    public User create(@RequestBody String idTokenString) {
        //return userRepo.save(bike);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList("680941561279-iblnhng1op6pm79k0gk6dj6igd3eu7ch.apps.googleusercontent.com"))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            List<User> users = userRepo.findByGoogleID(userId);
            User user;
            if (!users.isEmpty()) {
                 user = users.get(0);
                return user;
            } else {
                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

                user = new User();
                user.setName(name);
                user.setGoogleID(userId);
                user.setEmail(email);
                user.setPictureURL(pictureUrl);
                user.setLocale(locale);

                userRepo.save(user);
                return user;
            }

        } else {
            System.out.println("Invalid ID token.");

            throw new NotFoundException("Invalid ID token.");
        }


    }
}
