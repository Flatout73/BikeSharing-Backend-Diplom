package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.components.BikeEditor;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.repo.BikeRepo;


@Route(value = "bikes", layout = MainLayout.class)
public class BikeView extends VerticalLayout {

    private Grid<Bike> grid = new Grid<>(Bike.class);

    private BikeRepo bikeRepo;

    TextField filter = new TextField("", "Type to filter");
    Button addNewBtn = new Button("Add new");
    BikeEditor editor;

    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);

    @Autowired
    public BikeView(BikeRepo bikeRepo, BikeEditor editor) {
        this.bikeRepo = bikeRepo;
        this.editor = editor;

        add(toolbar, grid, editor);
        this.setHeight("100%");
       // grid.setHeight("300px");
        //grid.setColumns("id", "name");
       // grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Фильтр по имени");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showBikes(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editBike(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editBike(new Bike()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showBikes(filter.getValue());
        });


        showBikes("");
    }

    private void showBikes(String name) {
        if (name.isEmpty()) {
            grid.setItems(bikeRepo.findAll());
        } else {
            grid.setItems(bikeRepo.findByName(name));
        }
    }

}
