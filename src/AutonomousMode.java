import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.Range;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.remote.RemoteMotor;

public class AutonomousMode {

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
    public static final int MOTOR_A_STOP = 5;
    public static final int MOTOR_C_STOP = 6;
    public static final int MOTOR_A_C_FORWARD = 7; 
    public static final int ACTION=10;
    public static final int DISCONNECT = 99;    

    public static void main(String[] args) throws Exception {
        // Sensors initialization
        sensor_light.setFloodlight(true);

        // Motor initialization
        motor_left.setSpeed(motor_left_speed);
        motor_right.setSpeed(motor_right_speed);
        motor_sensor_ultrasonic.setSpeed(motor_sensor_ultrasonic_speed);
        initBTConnection();
        motor_left.forward();
        motor_right.forward();
        motor_sensor_ultrasonic.rotate(-90);
       
        // Run the main loop
        while (!Button.ESCAPE.isDown()) {
            int light = sensor_light.readValue();
            int distance = sensor_ultrasonic.getDistance();

            LCD.clear(2);
            LCD.clear(3);
            LCD.clear(4);
            LCD.clear(5);
            LCD.drawString("Distance:", 0, 2);
            LCD.drawString("left  = " + distance, 0, 3);
            LCD.drawString("front = ", 0, 4);
            LCD.drawString("right = ", 0, 5);

            String zone;

            if (range_black.contains(light)) {
                zone = "black";
            } else if (range_wood.contains(light)) {
                zone = "wood";
            } else if (range_white.contains(light)) {
                zone = "white";
            } else {
                zone = "unknown";
            }

            LCD.clear(0);
            LCD.drawString("Light: " + String.valueOf(light), 0, 0);

            LCD.clear(1);
            LCD.drawString("Zone: " + zone, 0, 1);

            if (range_wood.contains(light)) {
                if (new Range(0, 13).contains(distance)) {
                    // Trop à gauche
                    motor_right.setSpeed((int)(motor_right_speed * 0.9));
                } else if (new Range(15, 30).contains(distance)) {
                    // Trop à droite
                    motor_left.setSpeed((int)(motor_left_speed * 0.9));
                }
            } else if (range_white.contains(light)) {
                motor_right.stop();
                motor_left.stop();

                motor_left.waitComplete();
                motor_right.waitComplete();

                int distance_left = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(90);

                int distance_front = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(90);

                int distance_right = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(-180);

                LCD.clear(2);
                LCD.clear(3);
                LCD.clear(4);
                LCD.clear(5);
                LCD.drawString("Distance:", 0, 2);
                LCD.drawString("left  = " + distance_left, 0, 3);
                LCD.drawString("front = " + distance_front, 0, 4);
                LCD.drawString("right = " + distance_right, 0, 5);

                if (distance_front < 25) {
                    if (distance_left > 25) {
                        // Turn left
                        motor_left.stop(true);
                        motor_right.rotate(327);
                    } else if (distance_right > 25) {
                        // Turn right
                        motor_right.stop();
                        motor_left.rotate(327);
                    } else {
                        // Forward
                        motor_left.forward();
                        motor_right.backward();

                        Thread.sleep(4500);
                    }
                }

                motor_left.setSpeed(motor_left_speed);
                motor_right.setSpeed(motor_right_speed);
                motor_left.forward();
                motor_right.forward();

                Thread.sleep(1000);
            } else {
                motor_left.setSpeed(motor_left_speed);
                motor_right.setSpeed(motor_right_speed);
                motor_left.forward();
                motor_right.forward();
            }
        }

        motor_left.flt();
        motor_right.flt();

        motor_left.waitComplete();
        motor_right.waitComplete();
    }
    
    public static void initBTConnection() throws IOException, InterruptedException{
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
        
        while (true) {
            int command = dis.readInt();

            switch (command) {
                case MOTOR_A_FORWARD: 
                    Motor.A.forward();
                    break;
                case MOTOR_A_BACKWARD: 
                    Motor.A.backward();
                    break;
                case MOTOR_C_FORWARD:
                    Motor.C.forward();
                    break;
                case MOTOR_C_BACKWARD:
                    Motor.C.backward();
                    break;
                case MOTOR_A_C_FORWARD:
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
    		    case ACTION:
                    Motor.B.setSpeed(200);
                    Motor.B.rotate(-120);
                    Motor.B.rotate(120);			
                    break;
                default: 
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
   }
}
