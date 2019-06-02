package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.domain.Ride;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.RideRepo;
import ru.hse.BikeSharing.repo.UserRepo;

import java.util.Optional;

@Route("rides")
public class RideView extends AbstractGridView<Ride> implements HasUrlParameter<String> {

  //  private Grid<Ride> grid = new Grid<>(Ride.class);
    private RideRepo rideRepo;
    private UserRepo userRepo;

    @Autowired
    public RideView(RideRepo rideRepo, UserRepo userRepo) {
        super(new Grid<>(Ride.class));

        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Optional<User> current = userRepo.findById(Long.parseLong(parameter));
        if (current.isPresent()) {
            User user = current.get();
            grid.setItems(rideRepo.findByUser(user.getId()));
            grid.removeColumnByKey("user");
        }
    }
}

