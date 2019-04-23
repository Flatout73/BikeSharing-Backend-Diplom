package ru.hse.BikeSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.NotFoundException;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    UserRepo userRepo;
    RideRepo rideRepo;

    @Autowired
    public UserController(UserRepo userRepo, RideRepo rideRepo) {
        this.userRepo = userRepo;
        this.rideRepo = rideRepo;
    }

    @PostMapping("ride/{userId}")
    public Ride createRide(/*@RequestHeader(value = "BS-User")*/ @PathVariable("userId") String userId, @RequestBody Ride ride) {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
        //rideRepo.save(ride);
        Ride newRide = new Ride();
        newRide.setUser(user);
        rideRepo.save(newRide);

        return ride;
    }

    @GetMapping("rides/{userId}")
    public List<Ride> getRides(@PathVariable("userId") String userId) {
        return rideRepo.findByUser(Long.parseLong(userId));
    }
}
