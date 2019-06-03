package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.domain.Feedback;
import ru.hse.BikeSharing.repo.FeedbackRepo;

@Route(value = "feedbacks", layout = MainLayout.class)
public class FeedbackView extends AbstractGridView<Feedback> {

    FeedbackRepo repo;

    @Autowired
    public FeedbackView(FeedbackRepo repo) {
        super(new Grid<>(Feedback.class));

        this.repo = repo;
        grid.setItems(repo.findAll());

        grid.removeColumnByKey("user");
    }
}
