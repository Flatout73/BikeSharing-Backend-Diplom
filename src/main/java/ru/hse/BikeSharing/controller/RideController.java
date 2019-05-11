package ru.hse.BikeSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    UserRepo userRepo;
    RideRepo rideRepo;

    Double price = 100.0;

    @Autowired
    public RideController(UserRepo userRepo, RideRepo rideRepo) {
        this.userRepo = userRepo;
        this.rideRepo = rideRepo;
    }

    @PostMapping("start")
    public Ride createRide(@RequestHeader(value = "BS-User") /*@PathVariable("userId")*/ String userId, @RequestBody Ride ride) {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
        //rideRepo.save(ride);;
        ride.setUser(user);
        rideRepo.save(ride);

        return ride;
    }

    @PutMapping("end")
    public Ride endRide(@RequestHeader(value = "BS-User") String userId, @RequestBody Ride ride) {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
        //rideRepo.save(ride);;
        Ride existingRide = user.getRides()
                .stream()
                .filter(current -> current.getId().equals(ride.getId()))
                .findAny().orElseThrow(() -> new NotFoundException("Not found current ride"));

        existingRide.setEndTime(new Date());
        existingRide.setEndLocation(ride.getEndLocation());
        existingRide.setCost(price * ((existingRide.getEndTime().getTime() - existingRide.getStartTime().getTime()) / 1000.0 / 60.0 + 1));
        rideRepo.save(existingRide);

        return existingRide;
    }

    @GetMapping
    public List<Ride> getRides(@RequestHeader(value = "BS-User") String userId) {
        return rideRepo.findByUser(Long.parseLong(userId));
    }
}
