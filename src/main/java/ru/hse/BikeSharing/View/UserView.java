package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.UserRepo;

@Route(value = "users", layout = MainLayout.class)
public class UserView extends AbstractGridView<User> {
    private UserRepo userRepo;

    @Autowired
    public UserView(UserRepo userRepo) {
        super(new Grid<>(User.class));
        this.userRepo = userRepo;

        grid.asSingleSelect().addValueChangeListener(e -> {
            grid.getUI().ifPresent(ui -> ui.navigate(RideView.class, e.getValue().getId().toString()));
        });

        showUsers("");
    }


    private void showUsers(String name) {
        if (name.isEmpty()) {
            grid.setItems(userRepo.findAll());
        } else {
            grid.setItems(userRepo.findByName(name));
        }

        grid.removeColumnByKey("rides");
    }
}
