package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.RestrictedZone;

public interface RestrictedZoneRepo extends JpaRepository<RestrictedZone, Long> {


}
