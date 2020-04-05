/*
 * Copyright © Marc Auberer 2019 - 2020. All rights reserved
 */

package com.chillibits.particulatematterapi.model.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    private long sensorsTotal;
    private long sensorsActive;
    private long sensorsMapTotal;
    private long sensorsMapActive;
    private long serverRequestsTotal;
    private long serverRequestsTodayApp;
    private long serverRequestsTodayWebApp;
    private long serverRequestsTodayGoogleActions;
    private long serverRequestsYesterdayApp;
    private long serverRequestsYesterdayWebApp;
    private long serverRequestsYesterdayGoogleActions;
    private long dataRecordsTotal;
    private long dataRecordsThisMonth;
    private long dataRecordsPrevMonth;
    private long dataRecordsToday;
    private long dataRecordsYesterday;
}