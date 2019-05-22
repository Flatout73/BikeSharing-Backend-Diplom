package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("google-map-poly")
@HtmlImport("google-map-poly.html")
public class GoogleMapPolyline extends Component {

    public GoogleMapPolyline() { }

    public GoogleMapPolyline(GoogleMapPoint[] points) {
        for (GoogleMapPoint point: points) {
            getElement().appendChild(point.getElement());
        }
    }

    public void addPoint(GoogleMapPoint point) {
        getElement().appendChild(point.getElement());
    }
}
