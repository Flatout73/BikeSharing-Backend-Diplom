package ru.hse.BikeSharing.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.Security.JwtTokenProvider;
import ru.hse.BikeSharing.Services.DBFileStorageService;
import ru.hse.BikeSharing.domain.*;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.errors.PaymentException;
import ru.hse.BikeSharing.repo.FeedbackRepo;
import ru.hse.BikeSharing.repo.TransactionRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import org.springframework.social.facebook.api.Facebook;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class MainController {

    private static final JacksonFactory jacksonFactory = new JacksonFactory();

    UserRepo userRepo;
    TransactionRepo transactionRepo;
    FeedbackRepo feedbackRepo;

    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    private Facebook facebook;

    private DBFileStorageService DBFileStorageService;

    @Autowired
    public MainController(UserRepo userRepo, DBFileStorageService fileService, TransactionRepo transactionRepo, FeedbackRepo feedbackRepo) {
        this.userRepo = userRepo;
        this.DBFileStorageService = fileService;
        this.transactionRepo = transactionRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @PostMapping("/tokensignin/{isGoogle}")
    public String create(@RequestBody String idTokenString, @PathVariable Boolean isGoogle) {
        //return userRepo.save(bike);

        if (isGoogle) {
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
                }

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                userId
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = tokenProvider.generateToken(authentication);

                return jwt;

            } else {
                System.out.println("Invalid ID token.");

                throw new NotFoundException("Invalid ID token.");
            }
        } else {
            facebook = new FacebookTemplate(idTokenString, "", "417024999083480");
            org.springframework.social.facebook.api.User fb = facebook.userOperations().getUserProfile();
            System.out.println(fb);
            return "Success";
        }
    }

    @PostMapping("/pay")
    public void pay(@RequestBody Transaction transaction) {
        Stripe.apiKey = "sk_test_ZqNt8J9LjjVxFYgl79HegSIt00wxmbVVyS";

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(transaction.getCost() * 100));
        chargeParams.put("currency", transaction.getCurrency());
        chargeParams.put("description", transaction.getDescription());
        chargeParams.put("source", transaction.getToken());

        transactionRepo.save(transaction);

        try {
            Charge.create(chargeParams);
        } catch (StripeException e) {
            throw new PaymentException(e.getMessage());
        }
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // Load file from database
        DBFile dbFile = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @PostMapping("/feedback")
    public Feedback createFeedback(@RequestHeader(value = "BS-User") String userId, @RequestBody Feedback feedback) {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));

        feedback.setUser(user);
        feedbackRepo.save(feedback);

        return feedback;
    }
}
