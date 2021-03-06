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
    
    public static final int NORTH_ID=1000;
	public static final int SOUTH_ID=1001;
	public static final int EAST_ID=1002;
	public static final int WEST_ID=1003;
    
    public static int INITIAL_DIRECTION;
    public static int CURRENT_DIRECTION;
    public static int CURRENT_MODE;
    public static final int EXPLORATION_MODE=50;
    public static final int REMOTE_MODE = 51;
    public static final int WORLD_EXPLORATION_MODE = 52;
    public static final int REMOTE_SIMPLE = 53;
    
    public static void setInitDirection(int d){
    	INITIAL_DIRECTION=d;
    }
    
    public static void setCurrentDirection(int d){
    	CURRENT_DIRECTION=d;
    }

	public static int getCurrentMode() {
		return CURRENT_MODE;
	}
	
	public static void setCurrentMode(int d) {
		CURRENT_MODE = d;
	}

}
