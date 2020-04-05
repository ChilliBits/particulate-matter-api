/*
 * Copyright © Marc Auberer 2019 - 2020. All rights reserved
 */

package com.chillibits.particulatematterapi.repository;

import com.chillibits.particulatematterapi.model.db.main.Sensor;
import com.chillibits.particulatematterapi.model.io.RankingItemCity;
import com.chillibits.particulatematterapi.model.io.RankingItemCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    @Query("SELECT s, (6371000 * acos(cos(radians(?1)) * cos(radians(s.gpsLatitude)) * cos(radians(s.gpsLongitude) - radians(?2)) + sin(radians(?1)) * sin(radians(s.gpsLatitude)))) AS distance FROM Sensor s GROUP BY distance HAVING distance <= ?3 ORDER BY distance ASC")
    List<Sensor> findAllInRadius(double lat, double lng, int radius);

    @Modifying
    @Query("UPDATE Sensor s SET s.gpsLatitude = ?2, s.gpsLongitude = ?3, s.country = ?4, s.city = ?5, s.lastEditTimestamp = ?6, s.notes = ?7, s.indoor = ?8, published = ?9 WHERE s.chipId = ?1")
    Integer updateSensor(
            long chipId,
            double latitude,
            double longitude,
            String country,
            String city,
            long lastEdit,
            String notes,
            boolean indoor,
            boolean published
    );

    @Query(value = "SELECT new com.chillibits.particulatematterapi.model.io.RankingItemCity(s.country, s.city, COUNT(s.city)) FROM Sensor s GROUP BY s.city, s.country ORDER BY COUNT(s.city) DESC, s.country, s.city")
    List<RankingItemCity> getRankingByCity(int items);

    @Query("SELECT new com.chillibits.particulatematterapi.model.io.RankingItemCountry(s.country, COUNT(s.country)) FROM Sensor s GROUP BY s.country ORDER BY COUNT(s.country) DESC, s.country")
    List<RankingItemCountry> getRankingByCountry(int items);

    @Query("SELECT s.chipId FROM Sensor s WHERE country = ?1")
    List<Long> getChipIdsOfSensorFromCountry(String country);

    @Query("SELECT s.chipId FROM Sensor s WHERE country = ?1 AND city = ?2")
    List<Long> getChipIdsOfSensorFromCity(String country, String city);

    @Query("SELECT COUNT(s.chipId) FROM Sensor s")
    Integer getSensorsMapTotal();

    @Query("SELECT COUNT(s.chipId) FROM Sensor s WHERE s.lastMeasurementTimestamp > ?1")
    Integer getSensorsMapActive(long minLastMeasurementTimestamp);
}