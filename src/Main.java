import behaviors.*;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import utils.Hardware;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 12f, Hardware.MotorLeft, Hardware.MotorRight, true);
        Navigator navigator = new Navigator(pilot);
        LineMap map = new LineMap();

        Behavior forward = new ForwardBehavior(navigator);
        Behavior whiteSquare = new WhiteSquareBehavior(navigator, map);
        Behavior backHitWall = new BackHitWallBehavior(navigator);
        Behavior escapeButtonPress = new EscapeButtonPressBehavior();
        Behavior bluetoohSlave = new BluetoothBehavior();

        Behavior[] behaviors = {forward, whiteSquare, backHitWall, bluetoohSlave, escapeButtonPress};

        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.start();
    }
}
