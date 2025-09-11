package com.pickmeup.taxi_service.domain.utils;

import static java.lang.Math.*;

import java.util.Comparator;
import java.util.List;

import com.pickmeup.taxi_service.domain.models.Driver;
import com.pickmeup.taxi_service.domain.transferobjects.AvailableDriver;

public class DistanceCalculator {

    /**
     * Calculates the straight-line Euclidean distance between two geographical points.
     * This method assumes a flat 2D plane and is a simplified model, not suitable
     * for accurate real-world travel distances over long ranges.
     *
     * @param lat1 The latitude of the first point.
     * @param lon1 The longitude of the first point.
     * @param lat2 The latitude of the second point.
     * @param lon2 The longitude of the second point.
     * @return The straight-line Euclidean distance. The unit of the distance
     * will be the same as the unit of the input coordinates.
     */
    public static double calculateEuclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate the difference in coordinates
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        // Apply the Euclidean distance formula: sqrt((x2 - x1)^2 + (y2 - y1)^2)
        // In our case, x = longitude and y = latitude
        return sqrt(pow(deltaLat, 2) + pow(deltaLon, 2));
    }

    public static List<AvailableDriver> calculateAvailableDriverDistances(List<Driver> drivers, double riderLatitude, double riderLongitude){
        return drivers.stream().map(driver -> new AvailableDriver(driver, DistanceCalculator
                .calculateEuclideanDistance(riderLatitude,riderLongitude,driver.getCurrentLocation().latitude(),
                        driver.getCurrentLocation().longitude()))).sorted(Comparator.comparingDouble(AvailableDriver::distance)).toList();
    }
}
