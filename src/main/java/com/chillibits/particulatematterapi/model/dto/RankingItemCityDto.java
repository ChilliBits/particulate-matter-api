/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingItemCityDto {
    private String country;
    private String city;
    private long count;
}