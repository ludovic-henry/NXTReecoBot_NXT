package behaviors;

import utils.Hardware;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

public class WoodBehavior implements Behavior{
	private int initial_speed;
	@Override
	public boolean takeControl() {
		initial_speed=Hardware.MotorLeftSpeed;
		 return Hardware.RangeWood.contains(Hardware.SensorLight.readValue())
                 || Hardware.SensorUltrasonic.getDistance() < 20;
	}

	@Override
	public void action() {
		do{
			Hardware.MotorSensorUltrasonic.rotate(-90);
	        int distance_left = Hardware.SensorUltrasonic.getDistance();

	        Hardware.MotorSensorUltrasonic.rotate(90);
	        int distance_front = Hardware.SensorUltrasonic.getDistance();

	        Hardware.MotorSensorUltrasonic.rotate(90);
	        int distance_right = Hardware.SensorUltrasonic.getDistance();
	        
	        if(distance_left<distance_right){
	        	Hardware.MotorLeft.setSpeed(initial_speed+10);
	        }
	        try {
				Thread.sleep (1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Hardware.MotorLeft.setSpeed(initial_speed);
		}while(Hardware.RangeWood.contains(Hardware.SensorLight.readValue()));
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
