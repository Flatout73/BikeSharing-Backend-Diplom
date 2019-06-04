package ru.hse.BikeSharing.Services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Push;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.hse.BikeSharing.controller.MainController;
import ru.hse.BikeSharing.domain.Alert;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.repo.AlertRepo;
import ru.hse.BikeSharing.repo.BikeRepo;
import ru.hse.BikeSharing.repo.RestrictedZoneRepo;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class BikeListener {

    private static final Logger logger = LoggerFactory.getLogger(BikeListener.class);

    private static ZoneService service;

    @Autowired
    public void setZoneService (ZoneService service) {
        this.service = service;
    }


    @PostUpdate
    public void postUpdate(Bike bike) {
       Future<Boolean> future = service.checkZone(bike.getLocation(), bike.getName());
        while (true) {
            if (future.isDone()) {
                try {
                    if (future.get()){
                        logger.info(bike.getId() + " in zone");
                    } else {
                        logger.info(bike.getId() + " isn't in zone");
                        Broadcaster.broadcast("Велосипед \"" + bike.getName() + "\" не в зоне");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
