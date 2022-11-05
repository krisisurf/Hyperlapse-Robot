package com.kristian.dimitrov.HyperlapseRobot.exceptions;

public class FrequencyOutOfBoundsException extends RuntimeException{

    public FrequencyOutOfBoundsException(int target, int min, int max){
        super("Target frequency ( " + target + "Hz ) out of bounds [ " + min + " : " + max + " ]");
    }
}
