package behaviors;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.nxt.LCD;
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

    public WhiteSquareBehavior(Navigator navigator, LineMap map) {
        this.navigator = navigator;
        this.map = map;
        this.previousWaypoint = this.navigator.getWaypoint();
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

        LCD.clear(2);
        LCD.clear(3);
        LCD.clear(4);
        LCD.clear(5);
        LCD.drawString("Distance:", 0, 2);
        LCD.drawString("left  = " + distance_left, 0, 3);
        LCD.drawString("front = " + distance_front, 0, 4);
        LCD.drawString("right = " + distance_right, 0, 5);

        if (distance_front < 25) {
            if (distance_left > 25) {
                ((DifferentialPilot)this.navigator.getMoveController()).rotate(-90);
            } else if (distance_right > 5) {
                ((DifferentialPilot)this.navigator.getMoveController()).rotate(90);
            } else {
                ((DifferentialPilot)this.navigator.getMoveController()).rotate(180);
            }
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
