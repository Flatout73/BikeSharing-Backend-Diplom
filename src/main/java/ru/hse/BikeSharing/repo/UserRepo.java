package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
