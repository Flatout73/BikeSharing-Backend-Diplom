package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;

@DomEvent("google-map-marker-dragend")
public class DragEndEvent extends ComponentEvent<GoogleMapMarker> {

    public DragEndEvent(GoogleMapMarker source, boolean fromClient) {
        super(source, fromClient);
    }

}
