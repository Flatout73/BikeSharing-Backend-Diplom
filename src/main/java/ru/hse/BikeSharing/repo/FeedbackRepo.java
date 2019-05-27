package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
}
