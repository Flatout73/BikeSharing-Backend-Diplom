package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import ru.hse.BikeSharing.events.DragEndEvent;
import ru.hse.BikeSharing.events.PolyEvent;

@Tag("google-map-poly")
@HtmlImport("google-map-poly.html")
public class GoogleMapPolyline extends Component {

    public GoogleMapPolyline() {
        getElement().setAttribute("fill-color", "blue");
        getElement().setAttribute("fill-opacity", "0.1");

        //getElement().setAttribute("editable", true);
    }

    public void addPoint(GoogleMapPoint point) {
        getElement().appendChild(point.getElement());
    }


    public Registration addDragEndListener(ComponentEventListener<PolyEvent> dragEndListener) {
        return super.addListener(PolyEvent.class, dragEndListener);
    }

    @ClientCallable
    protected void _closedChanged() {
        System.out.println("lol");
    }
}
