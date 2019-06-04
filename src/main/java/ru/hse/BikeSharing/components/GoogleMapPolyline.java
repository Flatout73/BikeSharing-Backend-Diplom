package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.events.DragEndEvent;
import ru.hse.BikeSharing.events.PolyEvent;

@Tag("google-map-poly")
@HtmlImport("google-map-poly.html")
public class GoogleMapPolyline extends Component {

    public RestrictedZone zone;

    public GoogleMapPolyline() {
        getElement().setAttribute("fill-color", "blue");
        getElement().setAttribute("fill-opacity", "0.1");

        getElement().setAttribute("stroke-opacity", "0");
    }

    public void addPoint(GoogleMapPoint point) {
        getElement().appendChild(point.getElement());
    }


    public Registration addDragEndListener(ComponentEventListener<PolyEvent> dragEndListener) {
        return super.addListener(PolyEvent.class, dragEndListener);
    }
}
