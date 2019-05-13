package ru.hse.BikeSharing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.geo.Point;
import ru.hse.BikeSharing.Services.BikeListener;
import ru.hse.BikeSharing.Services.CustomGenerator;
import ru.hse.BikeSharing.Services.QRCodeGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Entity
@Table
@Data
@EntityListeners(BikeListener.class)
public class Bike extends AuditModel {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Size(min = 3, max = 100)
    private String name;

    private Point location;

    private Boolean locked = true;

    @JsonIgnore
    private String qrcodeURL;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Ride> rides;
}
