package com.kristian.dimitrov.HyperlapseRobot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String getDTTM(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
