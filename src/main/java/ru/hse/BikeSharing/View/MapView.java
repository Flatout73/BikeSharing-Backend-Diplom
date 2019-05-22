package ru.hse.BikeSharing.View;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import ru.hse.BikeSharing.components.GoogleMap;
import ru.hse.BikeSharing.components.GoogleMapMarker;
import ru.hse.BikeSharing.components.GoogleMapPoint;
import ru.hse.BikeSharing.components.GoogleMapPolyline;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.repo.BikeRepo;
import ru.hse.BikeSharing.repo.RestrictedZoneRepo;

@Route("map")
@StyleSheet("frontend://styles.css")
public class MapView extends VerticalLayout {

    BikeRepo repo;
    RestrictedZoneRepo zoneRepo;

    final String API_KEY = "AIzaSyA3wPm0y-ibxAD5qXCxTqz3yReluwcFbDE";

    @Autowired
    public MapView(BikeRepo repo,  RestrictedZoneRepo zoneRepo) {
        this.repo = repo;
        this.zoneRepo = zoneRepo;

        GoogleMap map = new GoogleMap(API_KEY);

        for (Bike bike: repo.findAll()) {
            GoogleMapMarker marker = new GoogleMapMarker();
            marker.setLatitude(bike.getLocation().getX());
            marker.setLongitude(bike.getLocation().getY());

            map.addMarker(marker);
        }

        GoogleMapPolyline polyline = new GoogleMapPolyline();
        for (RestrictedZone zone: zoneRepo.findAll()) {
            for (Point point: zone.getPoints()) {
                GoogleMapPoint mapPoint = new GoogleMapPoint(point.getX(), point.getY());
                polyline.addPoint(mapPoint);
            }
        }

        polyline.getElement().setAttribute("closed", true);
        map.addPolyline(polyline);

        Paragraph copyright = new Paragraph();
        copyright.setText("(c) Google 2018");
        copyright.addClassName("copyright");

        add(map, copyright);
    }

}
