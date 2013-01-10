import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import utils.Range;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Bttest {
	 final static LightSensor      sensor_light      = new LightSensor(SensorPort.S1);
	    final static UltrasonicSensor sensor_ultrasonic = new UltrasonicSensor(SensorPort.S3);
	    final static TouchSensor      sensor_touch      = new TouchSensor(SensorPort.S2);

	    final static NXTRegulatedMotor motor_left              = Motor.A;
	    final static NXTRegulatedMotor motor_right             = Motor.C;
	    final static NXTRegulatedMotor motor_sensor_ultrasonic = Motor.B;

	    final static int motor_mode_forward  = 1;
	    final static int motor_mode_backward = 2;
	    final static int motor_mode_stop     = 3;
	    final static int motor_mode_flt      = 4;

	    final static int motor_right_speed             = 180;
	    final static int motor_left_speed              = 180;
	    final static int motor_sensor_ultrasonic_speed = 720;

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
	    
	    
	    public static void main(String[] args) throws Exception {
	        // Sensors initialization
	        sensor_light.setFloodlight(true);

	        // Motor initialization
	        motor_left.setSpeed(motor_left_speed);
	        motor_right.setSpeed(motor_right_speed);
	        String connected = "Connected";
	        String waiting = "Waiting...";
	        String closing = "Closing...";
	        
	        LCD.drawString(waiting,0,0);
	        LCD.refresh();

	        BTConnection btc = Bluetooth.waitForConnection(0, NXTConnection.RAW);
	            
	        LCD.clear();
	        LCD.drawString(connected,0,0);
	        LCD.refresh();    

	        DataInputStream dis = btc.openDataInputStream();
            DataOutputStream dos = btc.openDataOutputStream();
	        LCD.drawString(btc.getAddress(),0,0);
	        
	        for(int i=0;i<10;i++){
	        	try { 
        			
        			dos.writeChars("hello");
            		dos.flush();
        	      } catch (IOException ioe) {
        	        LCD.drawString("Write Exception"+ioe, 0, 0);
        	      }
	        }
	        
	        
	        while (true) {
	            int command = dis.readInt();
	            int value = dis.readInt();
	            
	            int check = checkPos();
	            if (check>0){
	            	LCD.clear();
	            	LCD.drawString(""+check,0,0);
            		
            		try { 
            			
            			dos.writeInt(1000);
                		dos.flush();
            	      } catch (IOException ioe) {
            	        LCD.drawString("Write Exception"+ioe, 0, 0);
            	      }
            	}
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
	                case DISCONNECT: 
	                    break;
	                default: 
	                	Motor.A.setSpeed(value);
	                	Motor.C.setSpeed(value);
	                    Motor.A.stop();    
	                    Motor.C.stop();                            
	            }
	            if (command == DISCONNECT) break;

	    }
	        dis.close();
	        dos.close();
	        Thread.sleep(100); // wait for data to drain
	        LCD.clear();
	        LCD.drawString(closing,0,0);
	        LCD.refresh();
	        btc.close();
	        LCD.clear();
	        Button.waitForAnyPress();
	    }
	    


	    public static int checkPos(){
	    	int light = sensor_light.readValue();
	        int distance = sensor_ultrasonic.getDistance();
	    	if (range_white.contains(light)) {
                motor_right.stop();
                motor_left.stop();

                motor_left.waitComplete();
                motor_right.waitComplete();

                int distance_front = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(90);
                
                int distance_left = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(-180);

                int distance_right = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(90);
                
                if(distance_front < 25){//object front
                	if(distance_left<25){//object left
                		if(distance_right<25){//object right
                			return OBJECT_CULDESAC;
                		}else{
                			return OBJECT_LEFT_FRONT;
                		}
                	}else{
                		if(distance_right<25){//object right
                			return OBJECT_RIGHT_FRONT;
                		}else{
                			return OBJECT_FRONT;
                		}
                	}
                }else{
                	if(distance_left<25){//object left
                		if(distance_right<25){//object right
                			return OBJECT_RIGHT_LEFT;
                		}else{
                			return OBJECT_LEFT;
                		}
                	}else{
                		if(distance_right<25){//object right
                			return OBJECT_RIGHT;
                		}
                	}
                }
 
	    	}
			return 0;
	    }
}
