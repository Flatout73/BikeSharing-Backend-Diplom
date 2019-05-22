package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;

@Tag("google-map-point")
@HtmlImport("bower_components/google-map/google-map-point.html")
public class GoogleMapPoint extends Component {
    public GoogleMapPoint(double lat, double lon) {
        setLatitude(lat);
        setLongitude(lon);
    }

    public void setLatitude(Double lat) {
        //getElement().setAttribute("latitude", lat.toString());
        getElement().setProperty("latitude", lat);
    }

    public void setLongitude(Double lon) {
        getElement().setProperty("longitude", lon.toString());
    }
}
