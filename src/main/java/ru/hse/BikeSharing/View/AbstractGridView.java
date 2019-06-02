package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.hse.BikeSharing.domain.AuditModel;
import ru.hse.BikeSharing.domain.Ride;

public abstract class AbstractGridView<E extends AuditModel> extends VerticalLayout {
    Grid<E> grid;

    public AbstractGridView(Grid<E> grid) {
        this.grid = grid;

        setHeight("100%");

        add(grid);

        grid.getColumns().stream().forEach(column -> column.setResizable(true));
    }

}
