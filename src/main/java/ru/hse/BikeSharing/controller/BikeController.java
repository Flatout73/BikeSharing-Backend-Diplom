package ru.hse.BikeSharing.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.repo.BikeRepo;

import java.util.List;

@RestController
@RequestMapping("/api/bikes")
public class BikeController {
    private final BikeRepo bikeRepo;

    @Autowired
    public BikeController(BikeRepo bikeRepo) {
        this.bikeRepo = bikeRepo;
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
}
