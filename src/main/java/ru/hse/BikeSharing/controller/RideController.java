package ru.hse.BikeSharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.hse.BikeSharing.DBFileStorageService;
import ru.hse.BikeSharing.PointDeserializer;
import ru.hse.BikeSharing.domain.DBFile;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public Ride createRide(@RequestHeader(value = "BS-User") /*@PathVariable("userId")*/ String userId, @RequestBody Ride ride) {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));
        //rideRepo.save(ride);;
        ride.setUser(user);
        rideRepo.save(ride);

        return ride;
    }

    @PutMapping("end")
    public Ride endRide(@RequestParam("file") MultipartFile file, @RequestHeader(value = "BS-User") String userId, @RequestParam("ride") String rideString) throws IOException {
        User user = userRepo.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("Not found user"));

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

        DBFile dbFile = DBFileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

       // existingRide.setImage(file.getBytes());
        rideRepo.save(existingRide);

        return existingRide;
    }

    @GetMapping("all")
    public List<Ride> getRides(@RequestHeader(value = "BS-User") String userId) {
        return rideRepo.findByUser(Long.parseLong(userId));
    }
}
