package behaviors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.subsumption.Behavior;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothBehavior implements Behavior  {
	private BTConnection btc;
	private DataInputStream inputStream ;
	private DataOutputStream outputStream ;
    @Override
    public boolean takeControl() {
    	btc = Bluetooth.waitForConnection(0, NXTConnection.RAW);
    	if(btc.getAddress()!="") return true;
		return false;
    }

    @Override
    public void action() {
        //To change body of implemented methods use File | Settings | File Templates.
    	LCD.clear();
        LCD.drawString("Connected",0,0);
    }

    @Override
    public void suppress() {
    	try {
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        btc.close();
    }
}
