package behaviors;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class EscapeButtonPressBehavior implements Behavior  {

    @Override
    public boolean takeControl() {
        return Button.ESCAPE.isDown();
    }

    @Override
    public void action() {
        System.exit(0);
    }

    @Override
    public void suppress() {

    }
}
