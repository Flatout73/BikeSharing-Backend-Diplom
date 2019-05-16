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
import ru.hse.BikeSharing.repo.BikeRepo;

@Route("map")
@StyleSheet("frontend://styles.css")
public class MapView extends VerticalLayout {

    BikeRepo repo;

    final String API_KEY = "AIzaSyA3wPm0y-ibxAD5qXCxTqz3yReluwcFbDE";

    @Autowired
    public MapView(BikeRepo repo) {
        this.repo = repo;

        GoogleMapMarker marker = new GoogleMapMarker();
        marker.setLatitude(62);
        marker.setLongitude(24);
        //marker.setDraggable(true);

        GoogleMap map = new GoogleMap(API_KEY);
        map.setLatitude(62);
        map.setLongitude(24);

        GoogleMapPoint point1 = new GoogleMapPoint();
        point1.setLatitude(60.0);
        point1.setLongitude(24.9);
        GoogleMapPoint point2 = new GoogleMapPoint();
        point2.setLatitude(68.7);
        point2.setLongitude(29.5);

        GoogleMapPoint point3 = new GoogleMapPoint(57.7, 30.5);
      //  point3.setLatitude(57.7);
       // point3.setLongitude(30.5);

        //point1.getElement().setAttribute("hidden", false);
       // point1.getElement().setProperty("hidden", false);

        //point2.getElement().setAttribute("hidden", false);
       // point2.getElement().setProperty("hidden", false);

        //point3.getElement().setAttribute("hidden", false);
       // point3.getElement().setProperty("hidden", false);
//
//        polyline.addPoint(point1);
//        polyline.addPoint(point2);
//        polyline.addPoint(point3);

        GoogleMapPoint[] ps = new GoogleMapPoint[]{ point1, point2, point3 };
        GoogleMapPolyline polyline = new GoogleMapPolyline(ps);
        polyline.getElement().setAttribute("closed", true);

        map.addMarker(marker);
        map.addPolyline(polyline);

        Paragraph copyright = new Paragraph();
        copyright.setText("(c) Google 2018");
        copyright.addClassName("copyright");

        add(map, copyright);
    }

}
