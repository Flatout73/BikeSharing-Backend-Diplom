package ru.hse.BikeSharing.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import ru.hse.BikeSharing.components.GoogleMapMarker;

@DomEvent("google-map-marker-dragend")
public class DragEndEvent extends ComponentEvent<GoogleMapMarker> {

    public DragEndEvent(GoogleMapMarker source, boolean fromClient) {
        super(source, fromClient);
    }

}
