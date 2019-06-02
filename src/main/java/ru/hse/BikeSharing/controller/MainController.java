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
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.Security.CurrentUser;
import ru.hse.BikeSharing.Security.JwtTokenProvider;
import ru.hse.BikeSharing.Security.UserPrincipal;
import ru.hse.BikeSharing.Services.DBFileStorageService;
import ru.hse.BikeSharing.domain.*;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.errors.PaymentException;
import ru.hse.BikeSharing.repo.FeedbackRepo;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.TransactionRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import org.springframework.social.facebook.api.Facebook;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
public class MainController {

    private static final JacksonFactory jacksonFactory = new JacksonFactory();

    UserRepo userRepo;
    TransactionRepo transactionRepo;
    FeedbackRepo feedbackRepo;

    @Autowired
    RideRepo rideRepo;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    private FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("417024999083480", "2c7eb574a383c4f33871746fb826e67a");

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

                // Get profile information from payload
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");

                User user = userRepo.findByEmail(email) .orElse(new User());

                user.setName(name);
                user.setGoogleID(userId);
                user.setEmail(email);
                user.setPictureURL(pictureUrl);
                user.setLocale(locale);

                userRepo.save(user);


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
            AccessGrant accessGrant = new AccessGrant(idTokenString);
            Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
            Facebook facebook = connection.getApi();
            String [] fields = { "id", "email",  "first_name", "last_name", "locale", "link" };
            org.springframework.social.facebook.api.User userProfile = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
            String imageURL = connection.createData().getImageUrl();

            User user = userRepo.findByEmail(userProfile.getEmail()).orElse(new User());
            user.setName(userProfile.getFirstName() + " " + userProfile.getLastName());
            user.setFacebookID(userProfile.getId());
            user.setEmail(userProfile.getEmail());
            user.setPictureURL(imageURL);

            userRepo.save(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            userProfile.getId()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);

            return jwt;
        }
    }

    @PostMapping("api/pay/{rideID}")
    public Transaction pay(@PathVariable Long rideID, @RequestBody Transaction transaction) {
        Stripe.apiKey = "sk_test_ZqNt8J9LjjVxFYgl79HegSIt00wxmbVVyS";

        Ride ride = rideRepo.findById(rideID).orElseThrow(() -> new NotFoundException("Ride not found"));

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(transaction.getCost() * 100));
        chargeParams.put("currency", transaction.getCurrency());
        chargeParams.put("description", transaction.getDescription());
        chargeParams.put("source", transaction.getToken());

        ride.setTransaction(transaction);

        transactionRepo.save(transaction);
        rideRepo.save(ride);

        try {
            Charge.create(chargeParams);
        } catch (StripeException e) {
            throw new PaymentException(e.getMessage());
        }

        return transaction;
    }

    @GetMapping("api/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // Load file from database
        DBFile dbFile = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @PostMapping("api/feedback")
    public Feedback createFeedback(@CurrentUser UserPrincipal currentUser, @RequestBody Feedback feedback) {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));

        feedback.setUser(user);
        feedbackRepo.save(feedback);

        return feedback;
    }
}
