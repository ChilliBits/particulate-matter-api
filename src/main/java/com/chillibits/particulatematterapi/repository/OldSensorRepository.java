/*
 * Copyright © Marc Auberer 2019 - 2020. All rights reserved
 */

package com.chillibits.particulatematterapi.repository;

import com.chillibits.particulatematterapi.model.dbold.OldSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OldSensorRepository extends JpaRepository<OldSensor, Integer>{}