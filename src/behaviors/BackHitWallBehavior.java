package behaviors;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import utils.Hardware;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BackHitWallBehavior implements Behavior {

    private Navigator navigator;

    public BackHitWallBehavior(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public boolean takeControl() {
        return Hardware.SensorTouch.isPressed();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void action() {
        this.navigator.stop();
    }

    @Override
    public void suppress() {

    }
}
