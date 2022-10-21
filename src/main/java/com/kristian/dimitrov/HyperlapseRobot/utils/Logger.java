package com.kristian.dimitrov.HyperlapseRobot.utils;

public class Logger {

    public static void makeLog(String message, Throwable t){
        String dttm = Time.getDTTM();

        String className = t.getStackTrace()[0].getClassName();
        String methodName = t.getStackTrace()[0].getMethodName();

        System.out.println(dttm + "  INFO [ " + className + ", method " + methodName + " ], msg: " + message);
    }
}