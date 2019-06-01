package ru.hse.BikeSharing.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import org.springframework.data.geo.Point;
import ru.hse.BikeSharing.Services.PointDeserializer;
import ru.hse.BikeSharing.components.GoogleMapMarker;
import ru.hse.BikeSharing.components.GoogleMapPolyline;

import java.io.IOException;

@DomEvent("google-map-poly-editable-changed")
public class PolyEvent extends ComponentEvent<GoogleMapPolyline> {

    public Point[] newPoints;

    ObjectMapper mapper = new ObjectMapper();

    public PolyEvent(GoogleMapPolyline source, boolean fromClient,
                     @EventData("element._getPoints()") String coordinateString) {
        super(source, fromClient);

        if (coordinateString != null) {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Point.class, new PointDeserializer());
            mapper.registerModule(module);

            try {
                newPoints = mapper.readValue(coordinateString, Point[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
