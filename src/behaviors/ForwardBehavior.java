package behaviors;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class ForwardBehavior implements Behavior {

    private Navigator navigator;
    private Boolean suppressed = false;

    public ForwardBehavior(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        navigator.getMoveController().forward();

        while (!this.suppressed) {
            Thread.yield();
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
