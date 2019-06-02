package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hse.BikeSharing.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("from User u where u.name like concat('%', :name, '%')")
    List<User> findByName(@Param("name") String name);

    @Query("from User u where u.googleID like concat('%', :id, '%')")
    Optional<User> findByGoogleID(@Param("id") String id);

    @Query("from User u where u.facebookID like concat('%', :id, '%')")
    Optional<User> findByFacebookID(@Param("id") String id);

    @Query("from User u where u.email like concat('%', :email, '%')")
    Optional<User> findByEmail(@Param("email") String email);

}
