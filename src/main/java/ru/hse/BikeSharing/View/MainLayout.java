package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hse.BikeSharing.Services.Broadcaster;
import ru.hse.BikeSharing.domain.Alert;
import ru.hse.BikeSharing.domain.Feedback;

import java.util.HashMap;
import java.util.Map;

@StyleSheet("frontend://styles.css")
public class MainLayout extends Composite<Div>
        implements RouterLayout, AfterNavigationObserver, Broadcaster.BroadcastListener {

    private static final Logger logger = LoggerFactory.getLogger(MainLayout.class);

    private Map<String, RouterLink> targetPaths = new HashMap<>();

    private Div container;

    UI currentUI;

    public MainLayout() {
        Div menu = buildMenu();

        // Set up the container where sub views will be shown
        container = new Div();
        container.addClassName("container");

        container.setHeight("100%");

        getContent().addClassName("main-view");
        getContent().add(menu, container);

        getContent().setHeight("90%");
    }

    private Div buildMenu() {
        // Create links to each of the different sub views
        RouterLink template = new RouterLink("Alert",
                AlertView.class);
        template.setId("alert-link");

        RouterLink components = new RouterLink("Bikes",
                BikeView.class);
        components.setId("bike-link");

        RouterLink elements = new RouterLink("Users",
                UserView.class);
        elements.setId("user-link");

        RouterLink feedbacks = new RouterLink("Feedback",
                FeedbackView.class);
        elements.setId("feedback-link");

        RouterLink pushs = new RouterLink("Push",
                PushView.class);
        elements.setId("push-link");

        // Add menu links to a map for selection handling.
        targetPaths.put(template.getHref(), template);
        targetPaths.put(components.getHref(), components);
        targetPaths.put(elements.getHref(), elements);
        targetPaths.put(feedbacks.getHref(), feedbacks);
        targetPaths.put(pushs.getHref(), pushs);

        Anchor map = new Anchor("map", "Map");

        HtmlContainer ul = new HtmlContainer("ul");
        ul.setClassName("topnav");
        ul.add(template, components, elements, map, feedbacks, pushs);

        Div menu = new Div();
        menu.setClassName("menu");

        menu.add(ul);
        return menu;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        Broadcaster.register(this);
        logger.info("Register user for broadcast");

        currentUI = getUI().get();
        logger.info("Get ui");
    }

    @Override
    public void receiveBroadcast(final String message) {
        if (currentUI != null) {
            currentUI.access(new Command() {
                @Override
                public void execute() {
                    Notification n = new Notification(message, 2000);
                    n.open();
                }
            });

        }
    }

    @Override
    public void showRouterLayoutContent(HasElement child) {
        // Update what we show whenever the sub view changes
        container.removeAll();

        if (child != null) {
           // container.add(new H2(
               //     child.getClass().getAnnotation(PageTitle.class).value()));
            container.add((Component) child);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        targetPaths.values().forEach(link -> link.removeClassName("selected"));

        // We just use the location path as we don't have nested paths.
        if (!event.getLocation().getPath().isEmpty()) {
            targetPaths.get(event.getLocation().getPath())
                    .addClassName("selected");
        }
    }
}
