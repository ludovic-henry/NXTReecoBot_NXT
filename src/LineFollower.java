import lejos.nxt.*;

public class LineFollower {

    public static void main (String[] aArg) throws Exception {
        String left = "Turn left ";
        String right= "Turn right";

        LightSensor light = new LightSensor(SensorPort.S1);
        final int blackWhiteThreshold = 35;

        final int forward = 1;
        final int stop = 3;
        final int flt = 4;
        final int power = 80;

        // Use the light sensor as a reflection sensor
        light.setFloodlight(true);
        LCD.drawString("Light %: ", 0, 0);

        // Show light percent until LEFT is pressed
        LCD.drawString("Press LEFT", 0, 2);
        LCD.drawString("to start", 0, 3);

        while (! Button.LEFT.isDown()){
            LCD.drawInt(light.readValue(), 3, 9, 0);
        }

        // Follow line until ESCAPE is pressed
        LCD.drawString("Press ESCAPE", 0, 2);
        LCD.drawString("to stop ", 0, 3);
        while (! Button.ESCAPE.isDown()){

            if (light.readValue() > blackWhiteThreshold){
                // On white, turn right
                LCD.drawString(right, 0, 1);
                MotorPort.A.controlMotor(0,stop);
                MotorPort.C.controlMotor(power, forward);
            }
            else{
                // On black, turn left
                LCD.drawString(left, 0, 1);
                MotorPort.A.controlMotor(power, forward);
                MotorPort.C.controlMotor(0,stop);
            }
            LCD.drawInt(light.readValue(), 3, 9, 0);
            Thread.sleep(10);

        }

        // Stop car gently with free wheel drive
        MotorPort.A.controlMotor(0,flt);
        MotorPort.C.controlMotor(0,flt);
        LCD.clear();
        LCD.drawString("Program stopped", 0, 0);
        Thread.sleep(1000);
    }
}
