package ru.hse.BikeSharing.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.repo.BikeRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api/bikes")
public class BikeController {
    private final BikeRepo bikeRepo;
    private final UserRepo userRepo;

    @Autowired
    public BikeController(BikeRepo bikeRepo, UserRepo userRepo) {
        this.bikeRepo = bikeRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("all")
    //@JsonView(Views.IdName.class)
    public List<Bike> list() {
        return bikeRepo.findAll();
    }

    @GetMapping
    //@JsonView(Views.FullMessage.class)
    public Bike getOne(@RequestParam("id") Bike bike) {
        return bike;
    }

    @PostMapping
    public Bike create(@RequestBody Bike bike) {
        return bikeRepo.save(bike);
    }

    @PutMapping
    public Bike update(
            @RequestParam("id") Bike messageFromDb,
            @RequestBody Bike bike
    ) {
        BeanUtils.copyProperties(bike, messageFromDb, "id");

        return bikeRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Bike bike) {
        bikeRepo.delete(bike);
    }


    @PutMapping("location")
    public void updateLocation(@RequestBody Point point, @RequestParam String id) {
        Bike bike = bikeRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found bike"));
        bike.setLocation(point);
        bikeRepo.save(bike);
    }

    @PutMapping("booking")
    public ResponseEntity<String> occupyBike(@RequestHeader(value = "BS-User") Long userId, @RequestParam String id, @RequestParam Boolean occupied) {
        Bike bike = bikeRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found bike"));

        if (occupied) {
            bike.setOccupiedByUserID(userId);
        } else {
            bike.setOccupiedByUserID(userId);
        }

        bikeRepo.save(bike);

        return ResponseEntity.ok("Success");
    }


}
