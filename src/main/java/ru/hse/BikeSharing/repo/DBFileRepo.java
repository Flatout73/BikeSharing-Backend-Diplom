package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.BikeSharing.domain.DBFile;

@Repository
public interface DBFileRepo extends JpaRepository<DBFile, Long> {

}