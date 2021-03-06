import behaviors.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import utils.BluetoothThread;
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
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 12f, Hardware.MotorLeft, Hardware.MotorRight);
        Navigator navigator = new Navigator(pilot);
        LineMap map = new LineMap();

        BTConnection btc = null; //Bluetooth.waitForConnection(0, NXTConnection.RAW);
        
        Behavior forward = new ForwardBehavior(navigator);
        Behavior whiteSquare = new WhiteSquareBehavior(navigator, map,btc);
        Behavior backHitWall = new BackHitWallBehavior(navigator);
        Behavior escapeButtonPress = new EscapeButtonPressBehavior();
        Behavior bluetoohSlave = new BluetoothBehavior(navigator);
        Behavior wood = new WoodBehavior(navigator);

        Hardware.setCurrentMode(Hardware.EXPLORATION_MODE);
        
//        BluetoothThread BTThread = new BluetoothThread(btc);
//        BTThread.start();

        Behavior[] behaviors = {forward, backHitWall, bluetoohSlave, whiteSquare,wood,escapeButtonPress};


        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.start();
    }
}
