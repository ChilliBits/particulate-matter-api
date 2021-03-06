/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.model.db.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    // Constants
    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_ONLINE_WITH_CAMPAIGN = 2;
    public static final int STATUS_OFFLINE = 3;
    public static final int STATUS_MAINTENANCE = 4;
    public static final int STATUS_SUPPORT_ENDED = 5;

    public static final String ROLE_APPLICATION = "A";
    public static final String ROLE_APPLICATION_CHILLIBITS = "CBA";
    public static final String ROLE_APPLICATION_ADMIN = "AA";

    public static final int TYPE_NONE = 0;
    public static final int TYPE_WEBSITE = 1;
    public static final int TYPE_DESKTOP_APPLICATION = 2;
    public static final int TYPE_ANDROID_APP = 3;
    public static final int TYPE_IOS_APP = 4;
    public static final int TYPE_CROSS_PLATFORM_APP = 5;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String readableName;
    private String secret;
    private int type;
    private String roles;
    private int status;
    private boolean active;
    private int minVersion;
    private String minVersionName;
    private int latestVersion;
    private String latestVersionName;
    private String owner;
    private String userMessage;
}