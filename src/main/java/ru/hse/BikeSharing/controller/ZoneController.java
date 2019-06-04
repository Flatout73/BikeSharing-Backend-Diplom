package ru.hse.BikeSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.BikeSharing.domain.Alert;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.errors.NotFoundException;
import ru.hse.BikeSharing.repo.AlertRepo;
import ru.hse.BikeSharing.repo.RestrictedZoneRepo;

import java.util.List;

@RestController
@RequestMapping("zone")
public class ZoneController {

    @Autowired
    RestrictedZoneRepo restrictedZoneRepo;
    @Autowired
    AlertRepo alertRepo;


    @PostMapping("/create")
    public RestrictedZone createZone(@RequestBody RestrictedZone zone) {

        restrictedZoneRepo.save(zone);

        return zone;
    }

    @GetMapping("alerts")
    public List<Alert> getAlerts() {
        return alertRepo.findAll();
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteZone(@RequestParam Long zoneID) {
        RestrictedZone zone = restrictedZoneRepo.findById(zoneID).orElseThrow(()->new NotFoundException("Zone not found"));

        restrictedZoneRepo.delete(zone);

        return ResponseEntity.ok("Success");
    }
}
