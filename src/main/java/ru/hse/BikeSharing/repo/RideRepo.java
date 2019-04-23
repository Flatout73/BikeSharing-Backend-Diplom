package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hse.BikeSharing.domain.Ride;

import java.util.List;

public interface RideRepo extends JpaRepository<Ride, Long> {
    @Query("from Ride r where r.user.id = :userId")
    List<Ride> findByUser(@Param("userId") Long userId);
}
