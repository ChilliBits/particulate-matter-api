/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.model.dbold;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "old_sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OldSensor {
    @Id
    private int chipId;
    private String firmwareVersion;
    private long creationDate;
    private String notes;
    private long lastUpdate;
    private long lastEdit;
    private String lat;
    private String lng;
    private String alt;
    private String country;
    private String city;
    private String mapsUrl;
    private double lastValue;
    private double lastValue_2;
}