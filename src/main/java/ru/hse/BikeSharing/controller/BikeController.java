package ru.hse.BikeSharing.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.JsonViews;
import ru.hse.BikeSharing.repo.BikeRepo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bikes")
public class BikeController {
    private final BikeRepo bikeRepo;

    @Autowired
    public BikeController(BikeRepo bikeRepo) {
        this.bikeRepo = bikeRepo;
    }

    @GetMapping
    //@JsonView(Views.IdName.class)
    public List<Bike> list() {
        return bikeRepo.findAll();
    }

    @GetMapping("{id}")
    //@JsonView(Views.FullMessage.class)
    public Bike getOne(@PathVariable("id") Bike bike) {
        return bike;
    }

    @PostMapping
    public Bike create(@RequestBody Bike bike) {
        return bikeRepo.save(bike);
    }

    @PutMapping("{id}")
    public Bike update(
            @PathVariable("id") Bike messageFromDb,
            @RequestBody Bike bike
    ) {
        BeanUtils.copyProperties(bike, messageFromDb, "id");

        return bikeRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Bike bike) {
        bikeRepo.delete(bike);
    }
}
