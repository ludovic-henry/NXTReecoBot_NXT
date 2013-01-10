package behaviors;

import utils.Hardware;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class WoodBehavior implements Behavior{
	private int initial_speed;
	private boolean suppressed = false;
	
	private Navigator nav;
	public WoodBehavior(Navigator navigator){
		initial_speed=Hardware.MotorLeftSpeed;
		this.nav=navigator;
	}
	
	@Override
	public boolean takeControl() {
		 return Hardware.RangeWood.contains(Hardware.SensorLight.readValue()) 
				 && (Hardware.getCurrentMode()!= Hardware.REMOTE_SIMPLE) 
				 && (Hardware.getCurrentMode()!= Hardware.REMOTE_MODE);
	}

	@Override
	public void action() {
			this.nav.stop();

			int angle = 10;

			for (
					int i = 1; 
					!this.suppressed && Hardware.RangeWood.contains(Hardware.SensorLight.readValue());
					i++){

				LCD.clear(0);
				LCD.drawString("CONTROL "+Hardware.SensorLight.readValue(), 0, 0);
				((DifferentialPilot)this.nav.getMoveController()).rotate(i * angle * Math.pow(-1, i));
			}
			LCD.drawString(Integer.toString(Hardware.getCurrentMode()), 0, 6);

			this.suppressed = false;
		
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}

}
