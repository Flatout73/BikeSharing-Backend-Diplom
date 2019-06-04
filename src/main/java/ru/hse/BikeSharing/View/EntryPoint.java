package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import ru.hse.BikeSharing.Services.Broadcaster;

/**
 * The navigation target to show when opening the application to root.
 */
@PageTitle("Start")
@Route(value = "", layout = MainLayout.class)
public class EntryPoint extends Div implements Broadcaster.BroadcastListener{

    /**
     * Navigation target constructor.
     */
    public EntryPoint() {
        setText("Выберите страницу");
        Broadcaster.register(this);
    }

    UI currentUI;


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        currentUI = getUI().get();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);

       // Broadcaster.unregister(this);
    }

    @Override
    public void receiveBroadcast(final String message) {
        currentUI.access(new Command() {
            @Override
            public void execute() {
                Notification n = new Notification(message, 2000);
                n.open();
            }
        });


    }
}
