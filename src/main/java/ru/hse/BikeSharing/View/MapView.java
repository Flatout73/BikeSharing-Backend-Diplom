package ru.hse.BikeSharing.View;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Bounds;
import ru.hse.BikeSharing.repo.BikeRepo;

@Route
public class MapView extends VerticalLayout /*implements ClickListener, Window.CloseListener*/ {

    BikeRepo repo;

    @Autowired
    public MapView(BikeRepo repo) {
        this.repo = repo;
    }
    private LMap map = new LMap();

}
