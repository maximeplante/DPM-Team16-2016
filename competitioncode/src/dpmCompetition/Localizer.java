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

	/** x,y and distance variable */
	private double xCoord, yCoord, dist;
	
	/** When the localizer is scanning for the end of the wall, if the end of the wall
	 * is found under that delay (ms), it means that this wall is not really a wall but
	 * a block that has been detected as a wall.
	 */
	private static final int MINIMAL_VALID_WALL_DETECTION_DELAY = 2000;

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
		
		// Loop used to ignore blue blocks while localizing
		while (true) {
			// rotate the robot until it sees a wall
			while (!seesWall()) {
				navigation.turnLeft();
			}
			// Take the time before the robot starts finding the end of the wall
			double start = System.currentTimeMillis();
			// keep rotating until the robot sees no wall, then latch the angle
			while (seesWall()) {
				navigation.turnLeft();
			}
			/* If it took more than a certain delay before seeing the end of the wall,
			 * it is not a just seeing a block and it is the actual wall.
			 * Break the loop.
			 * If it took less than a certain delay before seeing the end of the wall,
			 * it is just a "small wall" so it's probably a block.
			 * Ignore it and search again for a wall (by looping)
			 */
			if (System.currentTimeMillis() - start > MINIMAL_VALID_WALL_DETECTION_DELAY) {
				break;
			}
		}
		angleA = odo.getTheta();

		// switch direction and wait until it sees a wall
		while (!seesWall()) {
			navigation.turnRight();
		}
		// keep rotating until the robot sees no wall, then latch the angle
		while ( seesWall()) {
			navigation.turnRight();
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
		dist = usPoller.getFilteredData()+ Main.US_OFFSET;
		xCoord = dist - Main.TILE_LENGTH;
		navigation.turnTo(270);
		dist = usPoller.getFilteredData()+ Main.US_OFFSET;
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
		if (usPoller.getFilteredData() < 30)
			return true;
		else
			return false;
//		else if (usPoller.getFilteredData() >= 25 + noiseMargin)
//			seesWall = false;
	}
}
