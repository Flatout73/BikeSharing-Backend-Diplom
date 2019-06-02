package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.domain.Alert;
import ru.hse.BikeSharing.domain.User;
import ru.hse.BikeSharing.repo.AlertRepo;
import ru.hse.BikeSharing.repo.UserRepo;

@Route(value = "alerts", layout = MainLayout.class)
public class AlertView extends AbstractGridView<Alert> {

    private AlertRepo repo;

    @Autowired
    public AlertView(AlertRepo repo) {
        super(new Grid<>(Alert.class));
        this.repo = repo;


        grid.asSingleSelect().addValueChangeListener(e -> {
            grid.getUI().ifPresent(ui -> ui.navigate(RideView.class, e.getValue().getId().toString()));
        });

        showUsers("");
    }


    private void showUsers(String name) {
        if (name.isEmpty()) {
            grid.setItems(repo.findAll());
        }
    }
}
