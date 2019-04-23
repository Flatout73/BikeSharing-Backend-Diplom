package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.Ride;

public interface RideRepo extends JpaRepository<Ride, Long> {
}
