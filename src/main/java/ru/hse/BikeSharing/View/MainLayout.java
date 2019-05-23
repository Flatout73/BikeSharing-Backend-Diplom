package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;

@StyleSheet("frontend://styles.css")
public class MainLayout extends Composite<Div>
        implements RouterLayout, AfterNavigationObserver {

    private Map<String, RouterLink> targetPaths = new HashMap<>();

    private Div container;

    /**
     * Main layout constructor.
     */
    public MainLayout() {
        Div menu = buildMenu();

        // Set up the container where sub views will be shown
        container = new Div();
        container.addClassName("container");

        getContent().addClassName("main-view");
        getContent().add(menu, container);
    }

    private Div buildMenu() {
        // Create links to each of the different sub views
        RouterLink template = new RouterLink("Map",
               MapView.class);
        template.setId("map-link");

        RouterLink components = new RouterLink("Bikes",
                BikeView.class);
        components.setId("bike-link");

        RouterLink elements = new RouterLink("Users",
                UserView.class);
        elements.setId("user-link");

        // Add menu links to a map for selection handling.
        targetPaths.put(template.getHref(), template);
        targetPaths.put(components.getHref(), components);
        targetPaths.put(elements.getHref(), elements);

        HtmlContainer ul = new HtmlContainer("ul");
        ul.setClassName("topnav");
        ul.add(template, components, elements);

        Div menu = new Div();
        menu.setClassName("menu");

        menu.add(ul);
        return menu;
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
