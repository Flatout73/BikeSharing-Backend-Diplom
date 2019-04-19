package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hse.BikeSharing.domain.Bike;

import java.util.List;

public interface BikeRepo extends JpaRepository<Bike, Long> {
    @Query("from Bike b where b.name like concat('%', :name, '%')")
    List<Bike> findByName(@Param("name") String name);
}
