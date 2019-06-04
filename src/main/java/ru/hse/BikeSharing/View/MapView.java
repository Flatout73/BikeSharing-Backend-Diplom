package ru.hse.BikeSharing.View;


import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import ru.hse.BikeSharing.Services.BikeListener;
import ru.hse.BikeSharing.Services.Broadcaster;
import ru.hse.BikeSharing.components.GoogleMap;
import ru.hse.BikeSharing.components.GoogleMapMarker;
import ru.hse.BikeSharing.components.GoogleMapPoint;
import ru.hse.BikeSharing.components.GoogleMapPolyline;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.events.PolyEvent;
import ru.hse.BikeSharing.repo.BikeRepo;
import ru.hse.BikeSharing.repo.RestrictedZoneRepo;

@Route("map")
@StyleSheet("frontend://styles.css")
public class MapView extends VerticalLayout implements ComponentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(BikeListener.class);

    BikeRepo repo;
    RestrictedZoneRepo zoneRepo;

    final String API_KEY = "AIzaSyA3wPm0y-ibxAD5qXCxTqz3yReluwcFbDE";

    GoogleMap map = new GoogleMap(API_KEY);

    PolyEvent currentEvent;

    @Autowired
    public MapView(BikeRepo repo, RestrictedZoneRepo zoneRepo) {
        this.repo = repo;
        this.zoneRepo = zoneRepo;

        for (Bike bike: repo.findAll()) {
            GoogleMapMarker marker = new GoogleMapMarker();
            marker.setLatitude(bike.getLocation().getX());
            marker.setLongitude(bike.getLocation().getY());
            map.addMarker(marker);
        }

//        GoogleMapPolyline polylineManual = new GoogleMapPolyline();
//        polylineManual.addPoint(new GoogleMapPoint(55, 37));
//        polylineManual.addPoint(new GoogleMapPoint(56, 37));
//        polylineManual.addPoint(new GoogleMapPoint(55, 36));
//        polylineManual.addPoint(new GoogleMapPoint(54, 38));
//        polylineManual.getElement().setAttribute("closed", true);
//        map.addPolyline(polylineManual);

        for (RestrictedZone zone: zoneRepo.findAll()) {
            GoogleMapPolyline polyline = new GoogleMapPolyline();
            polyline.zone = zone;
            for (Point point: zone.getPoints()) {
                GoogleMapPoint mapPoint = new GoogleMapPoint(point.getX(), point.getY());
                polyline.addPoint(mapPoint);
            }
            polyline.getElement().setAttribute("closed", true);
            polyline.addDragEndListener(this);
            map.addPolyline(polyline);
        }

        Checkbox valueChangeCheckbox = new Checkbox(
                "Editing");
        valueChangeCheckbox.addValueChangeListener(event -> {
            map.getPolylines().forEach(poly -> poly.getElement().setAttribute("editable", event.getValue()));
        });

        Button saveButton = new Button("Save", e -> {
            savePath();
        });

        add(map, valueChangeCheckbox, saveButton);
        setHeightFull();
    }

    @Override
    public void onComponentEvent(ComponentEvent event) {
        PolyEvent polyEvent = (PolyEvent) event;
        logger.info("Polyline for zone " + polyEvent.getSource().zone.getId() + " with new points: ", polyEvent.newPoints);

        GoogleMapPolyline polyline = ((PolyEvent) event).getSource();
        if (polyEvent.newPoints != null) {
            for (int i = 0; i < polyline.getElement().getChildren().count(); i++) {
                polyline.getElement().getChild(0).removeFromParent();
            }

            for (Point point: polyEvent.newPoints) {
                GoogleMapPoint mapPoint = new GoogleMapPoint(point.getX(), point.getY());
                polyline.addPoint(mapPoint);
            }
        }

        currentEvent = polyEvent;
    }

    void savePath() {
        if (currentEvent != null) {
            RestrictedZone zone = currentEvent.getSource().zone;
            zone.setPoints(currentEvent.newPoints);
            zoneRepo.save(zone);

            logger.info("Save path for zone " + zone.getId());
        }
    }
}
