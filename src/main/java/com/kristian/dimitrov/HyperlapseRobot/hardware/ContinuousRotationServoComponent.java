package com.kristian.dimitrov.HyperlapseRobot.hardware;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;

public class ContinuousRotationServoComponent {
    // Static
    private static final int DEFAULT_FREQUENCY = 50;
    private static final float DEFAULT_CLOCKWISE_ROTATION_DUTY_CYCLE = 2f;
    private static final float DEFAULT_COUNTERCLOCKWISE_ROTATION_DUTY_CYCLE = 8f;

    // Class
    private Pwm pwm;
    private int frequency;
    private float clockwise_rot_duty_cycle;
    private float counterclockwise_rot_duty_cycle;

    /**
     * @param pi4j        pi4j context
     * @param gpioPin     raspberry BCM pin number
     * @param name        name
     * @param description description
     */
    public ContinuousRotationServoComponent(Context pi4j, int gpioPin, String name, String description) {
        this(pi4j, gpioPin, DEFAULT_FREQUENCY, DEFAULT_CLOCKWISE_ROTATION_DUTY_CYCLE, DEFAULT_COUNTERCLOCKWISE_ROTATION_DUTY_CYCLE, name, description);
    }

    /**
     * @param pi4j pi4j context
     * @param gpioPin raspberry BCM pin number
     * @param name name
     */
    public ContinuousRotationServoComponent(Context pi4j, int gpioPin, String name) {
        this(pi4j, gpioPin, DEFAULT_FREQUENCY, DEFAULT_CLOCKWISE_ROTATION_DUTY_CYCLE, DEFAULT_COUNTERCLOCKWISE_ROTATION_DUTY_CYCLE, name, null);
    }

    /**
     * Full configuration constructor
     *
     * @param pi4j pi4j context
     * @param gpioPin raspberry BCM pin number
     * @param frequency frequency which the pwm will produce
     * @param clockwise_rot_duty_cycle duty cycle to rotate the servo clockwise
     * @param counterclockwise_rot_duty_cycle duty cycle to rotate the servo counter-clockwise
     * @param name name
     * @param description description
     */
    private ContinuousRotationServoComponent(Context pi4j, int gpioPin, int frequency, float clockwise_rot_duty_cycle, float counterclockwise_rot_duty_cycle, String name, String description) {
        this.frequency = frequency;
        this.clockwise_rot_duty_cycle = clockwise_rot_duty_cycle;
        this.counterclockwise_rot_duty_cycle = counterclockwise_rot_duty_cycle;

        pwm = pi4j.create(buildPwmConfig(pi4j, gpioPin, frequency, name, description));
    }

    public void stop() {
        pwm.off();
    }

    public void rotateClockwise() {
        pwm.on(clockwise_rot_duty_cycle);
    }

    public void rotateCounterclockwise() {
        pwm.on(counterclockwise_rot_duty_cycle);
    }

    /**
     * Builds a new PWM configuration.
     *
     * @param pi4j      Pi4J context
     * @param address   BCM address
     * @param frequency PWM frequency
     * @return PWM configuration
     */
    protected static PwmConfig buildPwmConfig(Context pi4j, int address, int frequency, String name, String description) {
        return Pwm.newConfigBuilder(pi4j)
                .id("BCM" + address)
                .name(name)
                .description(description)
                .address(address)
                .frequency(frequency)
                .initial(0)
                .shutdown(0)
                .build();
    }

    public String getName(){
        return pwm.getName();
    }

    public String getDescription(){
        return pwm.getDescription();
    }

    public int getAddress() {
        return pwm.getAddress();
    }
}
