package behaviors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Behavior;
import utils.Hardware;

/**
 * Created with IntelliJ IDEA.
 * User: ludovic
 * Date: 08/01/13
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
public class WhiteSquareBehavior implements Behavior {

    private Navigator navigator;
    private LineMap map;
    private Waypoint previousWaypoint;
    
    private BTConnection btc;
	private DataInputStream dataIn ;
	private DataOutputStream dataOut ;
	
	public static final int OBJECT_CULDESAC = 100;
    public static final int OBJECT_RIGHT_FRONT = 101;
    public static final int OBJECT_RIGHT_LEFT = 102;
    public static final int OBJECT_LEFT_FRONT = 103;
    public static final int OBJECT_FRONT = 104;
    public static final int OBJECT_LEFT = 105;
    public static final int OBJECT_RIGHT = 106;

    public WhiteSquareBehavior(Navigator navigator, LineMap map,BTConnection btc) {
        this.navigator = navigator;
        this.map = map;
        this.previousWaypoint = this.navigator.getWaypoint();
        this.btc=btc;

        if (this.btc != null) {
            this.dataOut=btc.openDataOutputStream();
        }

    }

    @Override
    public boolean takeControl() {
        return Hardware.RangeWhite.contains(Hardware.SensorLight.readValue())
                    || Hardware.SensorUltrasonic.getDistance() < 20;
    }

    @Override
    public void action() {
        this.navigator.stop();
        this.navigator.addWaypoint(this.navigator.getWaypoint());
        // this.updateMap();

        Hardware.MotorSensorUltrasonic.rotate(-90);
        int distance_left = Hardware.SensorUltrasonic.getDistance();

        Hardware.MotorSensorUltrasonic.rotate(90);
        int distance_front = Hardware.SensorUltrasonic.getDistance();

        Hardware.MotorSensorUltrasonic.rotate(90);
        int distance_right = Hardware.SensorUltrasonic.getDistance();

        Hardware.MotorSensorUltrasonic.rotate(-90);
        
        int square_type=0;
        
        if(distance_front < 25){//object front
        	if(distance_left<25){//object left
        		if(distance_right<25){//object right
        			square_type= OBJECT_CULDESAC;
        			
        		}else{
        			square_type= OBJECT_LEFT_FRONT;
        		}
        	}else{
        		if(distance_right<25){//object right
        			square_type= OBJECT_RIGHT_FRONT;
        		}else{
        			square_type= OBJECT_FRONT;
        		}
        	}
        }else{
        	if(distance_left<25){//object left
        		if(distance_right<25){//object right
        			square_type= OBJECT_RIGHT_LEFT;
        		}else{
        			square_type= OBJECT_LEFT;
        		}
        	}else{
        		if(distance_right<25){//object right
        			square_type= OBJECT_RIGHT;
        		}
        	}
        }

        if (this.btc != null) {
            try {
                dataOut.writeInt(square_type);
                dataOut.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            LCD.drawString("SENT"+square_type, 0, 0);
        }
        

        LCD.clear(2);
        LCD.clear(3);
        LCD.clear(4);
        LCD.clear(5);
        LCD.drawString("Distance:", 0, 2);
        LCD.drawString("left  = " + distance_left, 0, 3);
        LCD.drawString("front = " + distance_front, 0, 4);
        LCD.drawString("right = " + distance_right, 0, 5);
        
        if(Hardware.getCurrentMode()==Hardware.EXPLORATION_MODE){
        	if (distance_front < 25) {
        		if (distance_left > 25) {
        			((DifferentialPilot)this.navigator.getMoveController()).rotate(-90);
        			Hardware.setCurrentDirection(Hardware.WEST_ID);
        		} else if (distance_right > 5) {
        			((DifferentialPilot)this.navigator.getMoveController()).rotate(90);
        			Hardware.setCurrentDirection(Hardware.EAST_ID);
        		} else {
        			((DifferentialPilot)this.navigator.getMoveController()).rotate(180);
        			Hardware.setCurrentDirection(Hardware.NORTH_ID);
        		}
        	}
        	
        	((DifferentialPilot)this.navigator.getMoveController()).travel(2);
        } else {
        	
        }
    }

    @Override
    public void suppress() {

    }

    private void updateMap() {
        Line[] previousLines = this.map.getLines();
        Line[] newLines = new Line[previousLines.length + 1];

        for (int i = 0; i < previousLines.length; i++) {
            newLines[i] = previousLines[i];
        }

        newLines[newLines.length - 1] = new Line(this.previousWaypoint.x, this.previousWaypoint.y, this.navigator.getWaypoint().x, this.navigator.getWaypoint().y);

        this.map = new LineMap(newLines, this.map.getBoundingRect());
    }
}
