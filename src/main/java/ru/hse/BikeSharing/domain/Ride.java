package ru.hse.BikeSharing.domain;

import lombok.Data;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Entity
@Table
@Data
public class Ride extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Point start;
}
