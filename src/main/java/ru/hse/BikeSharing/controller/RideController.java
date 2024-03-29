package ru.hse.BikeSharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.hse.BikeSharing.Security.CurrentUser;
import ru.hse.BikeSharing.Security.UserPrincipal;
import ru.hse.BikeSharing.Services.DBFileStorageService;
import ru.hse.BikeSharing.Services.PointDeserializer;
import ru.hse.BikeSharing.domain.DBFile;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    UserRepo userRepo;
    RideRepo rideRepo;

    Double price = 100.0;

    private DBFileStorageService DBFileStorageService;

    @Autowired
    public RideController(UserRepo userRepo, RideRepo rideRepo, DBFileStorageService fileService) {
        this.userRepo = userRepo;
        this.rideRepo = rideRepo;
        this.DBFileStorageService = fileService;
    }

    @PostMapping("start")
    public Ride createRide(@CurrentUser UserPrincipal currentUser, @RequestBody Ride ride) {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));
        //rideRepo.save(ride);;
        ride.setUser(user);
        //удаление транзакции на старте
        ride.setTransaction(null);
        rideRepo.save(ride);

        return ride;
    }

    @PutMapping("end")
    public Ride endRide(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser, @RequestParam("ride") String rideString) throws IOException {
        User user = userRepo.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("Not found user"));

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Point.class, new PointDeserializer());
        mapper.registerModule(module);

        Ride ride = mapper.readValue(rideString, Ride.class);

        Ride existingRide = user.getRides()
                .stream()
                .filter(current -> current.getId().equals(ride.getId()))
                .findAny().orElseThrow(() -> new NotFoundException("Not found current ride"));

        existingRide.setEndTime(new Date());
        existingRide.setEndLocation(ride.getEndLocation());
        existingRide.setCost(price * ((existingRide.getEndTime().getTime() - existingRide.getStartTime().getTime()) / 1000.0 / 60.0 + 1));
        existingRide.setLocations(ride.getLocations());
        //existingRide.setStartAddress(ride.getStartAddress());
        existingRide.setEndAddress(ride.getEndAddress());

        DBFile dbFile = DBFileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        existingRide.setImageURL(fileDownloadUri);
        rideRepo.save(existingRide);

        return existingRide;
    }

    @GetMapping("all")
    public List<Ride> getRides(@CurrentUser UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        return rideRepo.findByUser(userId);
    }
}
