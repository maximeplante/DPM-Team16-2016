package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

// TODO don't do correction when the robot is not traveling in a straight line 
/**
 * Provide a separate thread to do odometry correction while the robot is
 * traveling
 *
 *
 */
public class OdometerCorrection extends Thread {

	/** Reference to the odometer thread used by the robot */
	private Odometer odometer;

	/** Reference to the left lightsensor poller thread used by the robot */
	private LsPoller leftLsPoller;

	/** Reference to the right lightsensor poller thread used by the robot */
	private LsPoller rightLsPoller;

	/** Reference to the navigation class used by the robot */
	private Navigation navigation;

	/** Reference to the right wheel's motor */
	private EV3LargeRegulatedMotor rightMotor;

	/** The margin of the corners */
	private int cornerRadius = 5;

	/** The length of each unit square */
	private double squareLength = 30.0;

	/** The radius of the robot's wheels (cm) */
	double radius = 1.4;// get the value from other class later (radius of the
						// wheels)

	// to be determine
	/** The distance between two sensors */
	private double sensorRadius;

	/**
	 * Constructor
	 * 
	 * @param odometer
	 * @param leftLsPoller
	 * @param rightLsPoller
	 * @param navigation
	 * @param motor
	 */
	public OdometerCorrection(Odometer odometer, LsPoller leftLsPoller, LsPoller rightLsPoller,
			Navigation navigation, MotorsController motor) {
		this.odometer = odometer;
		this.leftLsPoller = leftLsPoller;
		this.rightLsPoller = rightLsPoller;
		this.navigation = navigation;
		this.rightMotor = motor.getRightWheelMotor();
	}

	/**
	 * Start odometer's correction
	 */
	public void run() {
		double x1, x2, y1, y2;
		double distanceTravel;
		double theta;
		double lasttTachoRight, currentTachoRight;
		int direction = 1;
		double[] position = new double[3];
		boolean[] update = new boolean[3];
		while (true) {
			// first sensor sees the line
			while (!leftLsPoller.isSeeingBlackLine() || !rightLsPoller.isSeeingBlackLine() || isAtCorner()) {
				sleep(50);
			}
			if (leftLsPoller.isSeeingBlackLine())
				direction = 1;
			else
				direction = -1;
			lasttTachoRight = rightMotor.getTachoCount();
			x1 = odometer.getX();
			y1 = odometer.getY();
			// the other sensor sees the line
			while (!leftLsPoller.isSeeingBlackLine() || !rightLsPoller.isSeeingBlackLine()) {
				sleep(50);
			}
			currentTachoRight = lasttTachoRight = rightMotor.getTachoCount();
			x2 = odometer.getX();
			y2 = odometer.getY();
			// if the robot is crossing a horizontal line, only correct y value
			// and theta accordingly
			if (isCrossingX((x1 + x2) / 2.0, (y1 + y2) / 2.0)) {
				distanceTravel = ((currentTachoRight - lasttTachoRight) / 180) * Math.PI * radius * direction;
				theta = Math.toDegrees(Math.atan(distanceTravel / sensorRadius));
				// traveling in positive y direction
				if (odometer.getTheta() >= 10 && odometer.getTheta() <= 170) {
					position[2] = 90.0 - theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the
					// sensor needs to be added later
					position[1] = Math.round(odometer.getY() / squareLength) * squareLength
							+ (Math.cos(Math.toRadians(theta)) * distanceTravel / 2.0); // new
																						// y
																						// value
					update[1] = true; // will correct y value
					update[0] = false; // won't correct x value
					odometer.setPosition(position, update);
				} else if (odometer.getTheta() >= 190 && odometer.getTheta() <= 350) { // traveling
																						// in
																						// negative
																						// y
																						// direction
					// calculate the new position values
					position[2] = 270.0 - theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the
					// sensor needs to be added later
					position[1] = Math.round(odometer.getY() / squareLength) * squareLength
							- (Math.cos(Math.toRadians(theta)) * distanceTravel / 2.0); // new
																						// y
																						// value
					update[1] = true; // will correct y value
					update[0] = false; // won't correct x value
					odometer.setPosition(position, update); // update new
															// position
				}
			} else { // crossing a vertical line - correct x rather than y
				distanceTravel = ((currentTachoRight - lasttTachoRight) / 180) * Math.PI * radius * direction * (-1);
				theta = Math.toDegrees(Math.atan(distanceTravel / sensorRadius));
				if (odometer.getTheta() >= 280 && odometer.getTheta() <= 80) { // traveling
																				// in
																				// positive
																				// x
																				// direction
					position[2] = theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the
					// sensor needs to be added later
					position[0] = Math.round(odometer.getX() / squareLength) * squareLength
							+ (Math.cos(Math.toRadians(theta)) * distanceTravel / 2.0); // new
																						// x
																						// value
					update[0] = true; // will correct x value
					update[1] = false; // won't correct y value
					odometer.setPosition(position, update); // update position
				} else if (odometer.getTheta() >= 100 && odometer.getTheta() <= 260) { // traveling
																						// in
																						// negative
																						// x
																						// direction
					position[2] = 180.0 + theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the
					// sensor needs to be added later
					position[0] = Math.round(odometer.getY() / squareLength) * squareLength
							- (Math.cos(Math.toRadians(theta)) * distanceTravel / 2.0); // new
																						// x
																						// value
					update[0] = true; // will correct x value
					update[1] = false; // won't correct y value
					odometer.setPosition(position, update); // update position
				}
			}
		}
	}

	// check if the robot is at the corner of the square
	/**
	 * Determine whether the robot is at the corner base on its current
	 * odometer's reading If the robot is within 5cm around the corner, it is
	 * considered in the corner
	 * 
	 * @return a boolean variable that represents whether the robot is at the
	 *         corner
	 */
	private boolean isAtCorner() {
		if (cornerRadius < (squareLength - (odometer.getX() % squareLength))
				&& (squareLength - (odometer.getX() % squareLength)) < (squareLength - cornerRadius)
				&& cornerRadius < (squareLength - (odometer.getY() % squareLength))
				&& (squareLength - (odometer.getY() % squareLength)) < (squareLength - cornerRadius)) {
			return false;
		}
		return true;
	}

	// determine whether the robot is crossing an x axis (horizontal) or a y
	// (vertical).
	/**
	 * Determine whether the robot is crossing a vertical line or a horizontal
	 * line. If the robot is closer to the vertical axis(y) it is considered
	 * crossing a y, crossing an x otherwise.
	 * 
	 * @param x
	 *            - position x of the robot
	 * @param y
	 *            - position y of the robot
	 * @return a boolean variable that represents if the robot is crossing an x
	 *         axis or a y axis base on its current position
	 */
	private boolean isCrossingX(double x, double y) {
		double distanceToNearestY = Math.abs(x - squareLength * Math.round(x / squareLength));
		double distanceToNearestX = Math.abs(y - squareLength * Math.round(y / squareLength));
		if (distanceToNearestY >= distanceToNearestX) {
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method that hides the complexity of Thread.sleep() and simplifies the code.
	 * 
	 * @param time the sleep delay in milliseconds
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
