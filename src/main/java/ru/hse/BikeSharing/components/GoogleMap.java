package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Tag("google-map")
@HtmlImport("bower_components/google-map/google-map.html")
public class GoogleMap extends Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleMap.class);

    List<GoogleMapPolyline> polylines = new ArrayList<>();

    public GoogleMap() {
        LOGGER.info("Constructor ...");

        getElement().setAttribute("fitToMarkers", true);

        getElement().getStyle().set("height", "500px");
        getElement().getStyle().set("width", "100%");
    }

    public GoogleMap(String apiKey) {
        this();

        getElement().setProperty("apiKey", apiKey);
    }

    public void addMarker(GoogleMapMarker marker) {
        getElement().appendChild(marker.getElement());
        getElement().setProperty("latitude", marker.getLatitude());
        getElement().setProperty("longitude", marker.getLongitude());
    }

    public void addPolyline(GoogleMapPolyline polyline) {
        polylines.add(polyline);
        getElement().appendChild(polyline.getElement());
    }

    public List<GoogleMapPolyline> getPolylines() {
        return polylines;
    }

    public void clearPolylines() {
        getPolylines().forEach(poly -> poly.getElement().removeFromParent());

        polylines = new ArrayList<>();
    }

    public void addPoint(GoogleMapPoint point) {
        getElement().appendChild(point.getElement());
    }

    public void setLatitude(double lat) {
        getElement().setProperty("latitude", lat);
    }

    public void setLongitude(double lon) {
        getElement().setProperty("longitude", lon);
    }

}
