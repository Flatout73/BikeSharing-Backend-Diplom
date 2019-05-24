package ru.hse.BikeSharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MyUser")
@Data
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 100)
    private String name;


    private String googleID;

    private String facebookID;
    private String email;
    private String pictureURL;
    private String locale;
    @JsonIgnore
    private String pushToken;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   // @JoinColumn(name = "ride_id", nullable = false)
   // @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private List<Ride> rides = new ArrayList<>();
}
