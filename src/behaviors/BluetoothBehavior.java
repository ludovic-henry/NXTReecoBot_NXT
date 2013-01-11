package behaviors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.NXTInputStream;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

import utils.BluetoothThread;
import utils.Hardware;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothBehavior implements Behavior  {
	private BTConnection btc;
	private Boolean supressed = false;
	private Navigator navigator;
	private boolean connected;
	    
    public BluetoothBehavior(Navigator navigator) {
    	this.navigator = navigator;
    	this.connected=true;
    	// crer thread
    	// lire contenu du socket
    	// ajouter cmd pour prendre le controle
    	// dans action mutex pour sarreter, et relacher quand on le laisse reprendre le controle
	}

	public boolean takeControl() {
    	return this.connected && (Hardware.getCurrentMode() != Hardware.EXPLORATION_MODE);
    }

   
    public void action() {
        //To change body of implemented methods use File | Settings | File Templates.
    	LCD.clear();
        LCD.drawString("Connected",0,0);
        supressed = false;
        while(!supressed && (Hardware.getCurrentMode() != Hardware.EXPLORATION_MODE)) {
        	try { Thread.sleep(200); }
	 	 	catch (Exception e) { ; }
        }

    }

    @Override
    public void suppress() {
//    	try {
//			btc.openDataInputStream().close();
//			btc.openDataOutputStream().close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	this.supressed=true;
//        btc.close();
    }
}
