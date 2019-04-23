package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hse.BikeSharing.domain.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("from User u where u.name like concat('%', :name, '%')")
    List<User> findByName(@Param("name") String name);

    @Query("from User u where u.googleID like concat('%', :id, '%')")
    List<User> findByGoogleID(@Param("id") String id);

}
