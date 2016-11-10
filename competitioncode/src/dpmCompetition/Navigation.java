package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Provides an abstraction layer to access the robot's navigation capabilities.
 *
 */
public class Navigation {

	/** Reference to the odometer thread used by the robot */
	private Odometer odometer;

	/** Reference to the left wheel's motor */
	private EV3LargeRegulatedMotor leftMotor;
	/** Reference to the right wheel's motor */
	private EV3LargeRegulatedMotor rightMotor;

	/** The speed at which the wheels should turn when the robot is going forward */
	public static final int FORWARD_SPEED = 100;
	/** The speed at which the wheels should turn when the robot is turning on itself */
	private static final int TURN_SPEED = 100;

	/** The radius of the robot's wheels (cm) */
	private static double WHEEL_RADIUS = 2.1;
	/** The distance between the robot's wheels (cm) */
	private static double TRACK = 15.4;

	/**
	 * Constructor
	 *
	 * @param odometer a reference to the odometer thread used by the robot.
	 * @param motorsController provides a way to access the motors
	 */
	Navigation(Odometer odometer, MotorsController motorsController) {

		this.odometer = odometer;
		this.leftMotor = motorsController.getLeftWheelMotor();
		this.rightMotor = motorsController.getRightWheelMotor();

	}

	// This method turns the plant to be make it aligned with a specific theta (does it with the minimal turning angle)
	/**
	 * Turns the robot on itself to a specific angle position.
	 *
	 * The angle increases counterclockwise.
	 *
	 * @param theta the angle position (in radians)
	 */
	public void turnTo(double theta) {

		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		// Finds the minimal turning angle required to reach the target theta
		// minimalAngleDifference returns radians, but convertAngle uses degrees. A conversion is needed.
		double angle = Math.toDegrees(minimalAngleDifference(odometer.getTheta(), theta));

		// Make the wheels turn the right amount of degrees to turn to the target theta
		leftMotor.rotate((int)convertAngle(WHEEL_RADIUS, TRACK, angle), true);
		rightMotor.rotate((int)-convertAngle(WHEEL_RADIUS, TRACK, angle), false);

	}

	/**
	 * Turns the robot on itself by a certain amount of degrees.
	 *
	 * The angle increases counterclockwise.
	 *
	 * @param theta the amount of degrees to turn (in radians)
	 */
	public void turn(double theta) {

		double angle = Math.toDegrees(theta);

		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		// Make the wheels turn the right amount of degrees to turn to the target theta
		leftMotor.rotate((int)convertAngle(WHEEL_RADIUS, TRACK, angle), true);
		rightMotor.rotate((int)-convertAngle(WHEEL_RADIUS, TRACK, angle), false);

	}

	/**
	 * Makes the robot go forward by a certain distance
	 *
	 * @param distance the distance in centimeters
	 */
	public void goForward(double distance) {
		leftMotor.rotate((int)convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate((int)convertDistance(WHEEL_RADIUS, distance), false);
	}


	/**
	 * Makes the robot go forward.
	 *
	 * It will never stop until {@link #stopMoving()} is called
	 */
	public void goForward() {

		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);

		leftMotor.forward();
		rightMotor.forward();

	}

	/**
	 * Makes the robot turn on itself to the left.
	 *
	 * It will never stop until {@link #stopMoving()} is called
	 */
	public void turnLeft() {
		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		leftMotor.backward();
		rightMotor.forward();
	}

	/**
	 * Makes the robot turn on itself to the right.
	 *
	 * It will never stop until {@link #stopMoving()} is called
	 */
	public void turnRight() {
		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		leftMotor.forward();
		rightMotor.backward();
	}

	/**
	 * Stops the robot from moving.
	 *
	 * Stops the two wheel motors.
	 */
	public void stopMoving() {
		leftMotor.stop(true);
		rightMotor.stop(true);
	}

	/**
	 * Calculates the minimal angle the robot needs to turn in order to get to the target theta.
	 *
	 * Used in {@link #turnTo(double)}.
	 *
	 * @param currentTheta the robot's current angle position (in radians)
	 * @param targetTheta the target angle position (in radians)
	 * @return the minimal angle the robot needs to turn to reach the target angle position (in radians)
	 */
	public static double minimalAngleDifference(double currentTheta, double targetTheta) {
		double difference = targetTheta - currentTheta;
		if (difference > Math.PI) {
			difference = difference - 2 * Math.PI;
		} else if (difference < -Math.PI) {
			difference = difference + 2 * Math.PI;
		}
		return difference;
	}

	// Code given with the lab 2
	// Calculates how much degrees the wheels should turn to make the plant turn an [angle] number of degrees.
	// NOTE: the parameters and the value are in degrees.

	/**
	 * Calculates how much degrees the wheels should turn to make the robot turn a certain number of degrees on itself.
	 *
	 * This code comes from the code given in lab 2.
	 *
	 * @param radius the wheels (cm)
	 * @param width the distance between the two wheels (cm)
	 * @param angle the desired angle to turn (degrees)
	 * @return the amount of degrees the wheels should turn (in degrees)
	 */
	private static double convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	// Code given with the lab 2
	// Calculates how much degrees the wheels should turn to cover a given distance (cm)
	// NOTE: the return value is in degrees.
	/**
	 * Calculates how much degrees the wheels should turn to cover a given distance (cm)
	 *
	 * This code comes from the code given in lab 2.
	 *
	 * @param radius the wheels (cm)
	 * @param distance the desired distance (cm)
	 * @return the amount of degrees the wheels should turn (in degrees)
	 */
	private static double convertDistance(double radius, double distance) {
		return ((180.0 * distance) / (Math.PI * radius));
	}

}
