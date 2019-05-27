package ru.hse.BikeSharing.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import ru.hse.BikeSharing.domain.Alert;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.domain.RestrictedZone;
import ru.hse.BikeSharing.repo.AlertRepo;
import ru.hse.BikeSharing.repo.RestrictedZoneRepo;

import java.util.concurrent.Future;

@Service
@EnableAsync
public class ZoneService {

    @Autowired
    private AlertRepo repo;
    @Autowired
    private RestrictedZoneRepo zoneRepo;


    @Async
    public Future<Boolean> checkZone(Point point) {
        Boolean isContains = false;
        for (RestrictedZone zone: zoneRepo.findAll()) {
            if (contains(point, zone.getPoints())) {
                isContains = true;
                break;
            }
        }

        if (!isContains) {
            Alert alert = new Alert();
            alert.setMessage("Bike isn't in zone");
            repo.save(alert);
        }

        return new AsyncResult<Boolean>(isContains);
    }

    public boolean contains(Point test, Point[] points) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].getY() > test.getY()) != (points[j].getY() > test.getY()) &&
                    (test.getX() < (points[j].getX() - points[i].getX()) * (test.getY() - points[i].getY()) / (points[j].getY()-points[i].getY()) + points[i].getX())) {
                result = !result;
            }
        }
        return result;
    }
}
