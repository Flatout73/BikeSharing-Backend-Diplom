package ru.hse.BikeSharing.Services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.repo.BikeRepo;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BikeListener {


//    QRCodeGenerator generator;
//    BikeRepo repo;
//
//    @Autowired
//    public BikeListener(QRCodeGenerator generator, BikeRepo repo) {
//        this.generator = generator;
//        this.repo = repo;
//    }
//
//    @PostPersist
//    public void postPersist(Bike bike) {
//
//
//    }
}
