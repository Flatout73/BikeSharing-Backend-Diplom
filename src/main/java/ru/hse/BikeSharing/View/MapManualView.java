package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.hse.BikeSharing.components.MapManual;


@Route("map2")
public class MapManualView extends VerticalLayout {

    public MapManualView() {
//        MapManual map = new MapManual();
//        add(map);

        MapManual hello1 = new MapManual();

        Div layout = new Div();
        layout.add(hello1);

        add(layout);
    }
}
