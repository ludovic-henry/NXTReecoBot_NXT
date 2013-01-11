package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;

public class BluetoothThread extends Thread {
	private BTConnection btc;
	private int command;
	private int value;
	private DataInputStream dataIn ;
	private DataOutputStream dataOut ;
	
	final static NXTRegulatedMotor motor_left              = Motor.A;
    final static NXTRegulatedMotor motor_right             = Motor.C;
    final static NXTRegulatedMotor motor_sensor_ultrasonic = Motor.B;
    
    final static LightSensor      sensor_light      = new LightSensor(SensorPort.S1);
    final static UltrasonicSensor sensor_ultrasonic = new UltrasonicSensor(SensorPort.S3);
    final static TouchSensor      sensor_touch      = new TouchSensor(SensorPort.S2);
    
    final static Range range_black = new Range(0, 35);
    final static Range range_wood  = new Range(36, 49);
    final static Range range_white = new Range(50, 100);
	
	public static final int MOTOR_A_C_STOP = 0;
	public static final int MOTOR_A_FORWARD = 1;
	public static final int MOTOR_A_BACKWARD = 2;
	public static final int MOTOR_C_FORWARD = 3;
	public static final int MOTOR_C_BACKWARD = 4; 
	public static final int MOTOR_C_STOP = 5; 
	public static final int MOTOR_A_STOP = 6; 
	public static final int MOTOR_A_C_FORWARD = 7; 
	public static final int ACTION=10;
	public static final int DISCONNECT = 99;
	
	public static final int OBJECT_CULDESAC = 100;
    public static final int OBJECT_RIGHT_FRONT = 101;
    public static final int OBJECT_RIGHT_LEFT = 102;
    public static final int OBJECT_LEFT_FRONT = 103;
    public static final int OBJECT_FRONT = 104;
    public static final int OBJECT_LEFT = 105;
    public static final int OBJECT_RIGHT = 106;
    
	public BluetoothThread(BTConnection blueetoothconnect) {
		this.btc=blueetoothconnect;
		this.dataIn=btc.openDataInputStream();
		this.dataOut=btc.openDataOutputStream();
	}
	
	public void run()
    {
		Hardware.setInitDirection(Hardware.NORTH_ID);
        do {
        	LCD.clear(7);
        	LCD.drawString(Integer.toString(Hardware.getCurrentMode()), 0, 7);
            try {
				command = dataIn.readInt();
				value = dataIn.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            LCD.clear(6);
        	LCD.drawInt((int) new Date().getSeconds(), 0, 6);
        	
            switch (command) {
	            case MOTOR_A_FORWARD:
	            	Motor.A.setSpeed(value);
	            	Motor.A.forward();
	            	break;
	            case MOTOR_A_BACKWARD: 
	            	Motor.A.setSpeed(value);
	            	Motor.A.backward();
	            	break;
	            case MOTOR_C_FORWARD:
	            	Motor.C.setSpeed(value);
	            	Motor.C.forward();
	            	break;
	            case MOTOR_C_BACKWARD:
	            	Motor.C.setSpeed(value);
	            	Motor.C.backward();
	            	break;
	            case MOTOR_A_C_FORWARD:
	            	Motor.A.setSpeed(value);
	            	Motor.C.setSpeed(value);
	            	Motor.A.forward();
	            	Motor.C.forward();
	            	break;
	            case MOTOR_A_STOP:
	            	Motor.A.stop();
	            	break;
	            case MOTOR_C_STOP:
	            	Motor.C.stop();
	            	break;
	            case Hardware.EXPLORATION_MODE:
	            	Hardware.setCurrentMode(Hardware.EXPLORATION_MODE);
	            	break;
	            case Hardware.REMOTE_MODE:
	            	Hardware.setCurrentMode(Hardware.REMOTE_MODE);
	            	break;
	            case Hardware.REMOTE_SIMPLE:
	            	Hardware.setCurrentMode(Hardware.REMOTE_SIMPLE);
	            	break;
	            case Hardware.WORLD_EXPLORATION_MODE:
	            	Hardware.setCurrentMode(Hardware.WORLD_EXPLORATION_MODE);
	            	break;
	            case DISCONNECT:
	            	this.interrupt();
	            	break;
	            default: 
	            	Motor.A.setSpeed(value);
	            	Motor.C.setSpeed(value);
	            	Motor.A.stop();    
	            	Motor.C.stop();                            
        }
            
        } while (!isInterrupted());
    }
    public int getCommand()
    {
        return command;
    }

}
