package dpmCompetition;

import lejos.hardware.Sound;

/**
 * Provides robot the capability of localizing itself on the field
 *
 *
 */
public class Localizer {

	/** Reference to the odometer thread used by the robot */
	private Odometer odo;
	/** Reference to the navigation used by the robot */
	private Navigation navigation;
	/** Reference to the usPoller */
	private UsPoller usPoller;

//	private int noiseMargin = 10;
	/** x,y and distance variable */
	private double xCoord, yCoord, dist;

	/**
	 * Constructor
	 *
	 * @param odo Odometer
	 * @param usPoller
	 * @param navigation
	 */
	public Localizer(Odometer odo, UsPoller usPoller, Navigation navigation) {
		this.odo = odo;
		this.usPoller = usPoller;
		this.navigation = navigation;
	}

	/**
	 * Starts localization
	 */
	public void localize() {
		double angleA, angleB;
		// rotate the robot until it sees no wall

		while (seesWall()) {
			navigation.turnLeft();
		}
		// keep rotating until the robot sees a wall, then latch the angle
		while (!seesWall()) {
			navigation.turnRight();
		}
		angleA = odo.getTheta();

		// switch direction and wait until it sees no wall
		while (seesWall()) {
			navigation.turnLeft();
		}
		// keep rotating until the robot sees a wall, then latch the angle
		while (!seesWall()) {
			navigation.turnLeft();
		}
		angleB = odo.getTheta();

		//handles wrapping when angleA is larger than angleB
		if (angleA > angleB) {
			angleB += 360;
		}
		// angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'
		angleB = 45+((angleB-angleA)/2);
		odo.setPosition(new double [] {0.0, 0.0, angleB}, new boolean [] {true, true, true});
		navigation.turnTo(180);
		dist = usPoller.getFilteredData()+ Main.UPPER_US_OFFSET;
		xCoord = dist - Main.TILE_LENGTH;
		navigation.turnTo(270);
		dist = usPoller.getFilteredData()+ Main.UPPER_US_OFFSET;
		yCoord = dist - Main.TILE_LENGTH;
		odo.setPosition(new double [] {xCoord, yCoord, odo.getTheta()}, new boolean [] {true, true, true});
		Sound.beep();
	}

	/**
	 * Used to see if the robot is facing a wall
	 *
	 * @return true if the distance between the wall and the robot is less than 25cm
	 */
	private boolean seesWall() {
		if (usPoller.getFilteredData() < 25)
			return true;
		else
			return false;
//		else if (usPoller.getFilteredData() >= 25 + noiseMargin)
//			seesWall = false;
	}
}
