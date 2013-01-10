package utils;

import lejos.geom.Point;
import lejos.robotics.navigation.Waypoint;

public class WaypointWithWalls extends Waypoint{
	private boolean wall_north;
	private boolean wall_south;
	private boolean wall_east;
	private boolean wall_west;
	
	public WaypointWithWalls(Point p, boolean wall_north, boolean wall_south, boolean wall_east, boolean wall_west) {
		super(p);
		this.setWallNorth(wall_north);
		this.setWallSouth(wall_south);
		this.setWallEast(wall_east);
		this.setWallWest(wall_west);
	}

	public boolean isWallNorth() {
		return wall_north;
	}

	public void setWallNorth(boolean wall_north) {
		this.wall_north = wall_north;
	}

	public boolean isWallSouth() {
		return wall_south;
	}

	public void setWallSouth(boolean wall_south) {
		this.wall_south = wall_south;
	}

	public boolean isWallEast() {
		return wall_east;
	}

	public void setWallEast(boolean wall_east) {
		this.wall_east = wall_east;
	}

	public boolean isWallWest() {
		return wall_west;
	}

	public void setWallWest(boolean wall_west) {
		this.wall_west = wall_west;
	}

}
