package ru.hse.BikeSharing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.geo.Point;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table
@Data
public class Ride extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Point startLocation;
    private Point endLocation;

//        @Column(updatable = false)
//    @JsonFormat(shape = JsonFormat.Shape., pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(JsonViews.FullMessage.class)
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;

    private Double price;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
