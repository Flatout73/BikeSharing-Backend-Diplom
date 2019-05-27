package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.Alert;

public interface AlertRepo extends JpaRepository<Alert, Long> {
}
