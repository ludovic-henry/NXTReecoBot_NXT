import lejos.nxt.*;
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
    final static int motor_left_speed              = 190;
    final static int motor_sensor_ultrasonic_speed = 720;

    final static Range range_black = new Range(0, 35);
    final static Range range_wood  = new Range(36, 49);
    final static Range range_white = new Range(50, 100);

    public static void main(String[] args) throws Exception {
        // Sensors initialization
        sensor_light.setFloodlight(true);

        // Motor initialization
        motor_left.setSpeed(motor_left_speed);
        motor_right.setSpeed(motor_right_speed);
        motor_sensor_ultrasonic.setSpeed(motor_sensor_ultrasonic_speed);

        motor_left.forward();
        motor_right.forward();

        // Run the main loop
        while (!Button.ESCAPE.isDown()) {
            int light = sensor_light.readValue();

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
                motor_left.setSpeed((int)(motor_left_speed / 1.5));
            } else if (range_white.contains(light)) {
                motor_right.stop();
                motor_left.stop();

                motor_left.waitComplete();
                motor_right.waitComplete();

                int distance_front = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(90);

                int distance_right = sensor_ultrasonic.getDistance();

                motor_sensor_ultrasonic.rotate(-180);

                int distance_left = sensor_ultrasonic.getDistance();

                LCD.clear(2);
                LCD.clear(3);
                LCD.clear(4);
                LCD.clear(5);
                LCD.drawString("Distance:", 0, 2);
                LCD.drawString("l=" + distance_left, 0, 3);
                LCD.drawString("f=" + distance_front, 0, 4);
                LCD.drawString("r=" + distance_right, 0, 5);

                motor_sensor_ultrasonic.rotate(90);

                if (distance_front < 25) {
                    if (distance_left > 25) {
                        // Turn left
                        motor_left.stop(true);
                        motor_right.forward();

                        Thread.sleep(2250);
                    } else if (distance_right > 25) {
                        // Turn right
                        motor_right.stop();
                        motor_left.forward();

                        Thread.sleep(3000);
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
}
