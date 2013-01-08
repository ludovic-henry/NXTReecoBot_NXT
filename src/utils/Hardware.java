package utils;

import lejos.nxt.*;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 23/12/12
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class Hardware {
    public final static LightSensor SensorLight           = new LightSensor(SensorPort.S1);
    public final static UltrasonicSensor SensorUltrasonic = new UltrasonicSensor(SensorPort.S4);
    public final static TouchSensor SensorTouch           = new TouchSensor(SensorPort.S2);

    public final static NXTRegulatedMotor MotorLeft             = Motor.C;
    public final static NXTRegulatedMotor MotorRight            = Motor.A;
    public final static NXTRegulatedMotor MotorSensorUltrasonic = Motor.B;

    public final static int MotorRightSpeed            = 180;
    public final static int MotorLeftSpeed             = 185;
    public final static int MotorSensorUltrasonicSpeed = 720;


    public final static Range RangeBlack = new Range(0, 35);
    public final static Range RangeWood  = new Range(36, 49);
    public final static Range RangeWhite = new Range(50, 100);

}
