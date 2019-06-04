package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.hse.BikeSharing.Services.BikeListener;
import ru.hse.BikeSharing.Services.Broadcaster;

/**
 * The navigation target to show when opening the application to root.
 */
@PageTitle("Start")
@Route(value = "", layout = MainLayout.class)
public class EntryPoint extends Div {

    private static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    /**
     * Navigation target constructor.
     */
    public EntryPoint() {
        setText("Выберите страницу");

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);

       // Broadcaster.unregister(this);
    }
}
