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
	
	/** True only when the robot is moving straight (false if the robot is turning or not moving) */
	public boolean isGoingStraight;

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
		
		isGoingStraight = false;

	}

	// This method turns the plant to be make it aligned with a specific theta (does it with the minimal turning angle)
	/**
	 * Turns the robot on itself to a specific angle position.
	 *
	 * The angle increases counterclockwise. Returns once the robot has finished turning.
	 *
	 * @param theta the angle position (in degrees)
	 */
	public void turnTo(double theta) {
		
		turnTo(theta, false);

	}
	
	/**
	 * Turns the robot on itself to a specific angle position.
	 *
	 * The angle increases counterclockwise.
	 *
	 * @param theta the angle position (in degrees)
	 * @param immediateReturn true if the method should return without waiting for the robot to finish turning
	 */
	public void turnTo(double theta, boolean immediateReturn) {
		
		isGoingStraight = false;

		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		// Finds the minimal turning angle required to reach the target theta
		// minimalAngleDifference returns radians, but convertAngle uses degrees. A conversion is needed.
		double angle = minimalAngleDifference(odometer.getTheta(), theta);

		// Make the wheels turn the right amount of degrees to turn to the target theta
		leftMotor.rotate((int)-convertAngle(Main.WHEEL_RADIUS, Main.TRACK, angle), true);
		rightMotor.rotate((int)convertAngle(Main.WHEEL_RADIUS, Main.TRACK, angle), immediateReturn);
		
	}

	/**
	 * Turns the robot on itself by a certain amount of degrees.
	 *
	 * The angle increases counterclockwise. Returns once the robot has finished turning.
	 *
	 * @param theta the amount of degrees to turn (in degrees)
	 */
	public void turn(double theta) {
		
		turn(theta, false);

	}
	
	/**
	 * Turns the robot on itself by a certain amount of degrees.
	 *
	 * The angle increases counterclockwise.
	 *
	 * @param theta the amount of degrees to turn (in degrees)
	 * @param immediateReturn true if the method should return without waiting for the robot to finish turning
	 */
	public void turn(double theta, boolean immediateReturn) {
		
		isGoingStraight = false;

		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		// Make the wheels turn the right amount of degrees to turn to the target theta
		leftMotor.rotate((int)-convertAngle(Main.WHEEL_RADIUS, Main.TRACK, theta), true);
		rightMotor.rotate((int)convertAngle(Main.WHEEL_RADIUS, Main.TRACK, theta), immediateReturn);

	}

	/**
	 * Makes the robot go forward by a certain distance.
	 * 
	 * Returns once the robot has finished turning.
	 *
	 * @param distance the distance in centimeters
	 */
	public void goForward(double distance) {
		
		isGoingStraight = true;
		
		leftMotor.rotate((int)convertDistance(Main.WHEEL_RADIUS, distance), true);
		rightMotor.rotate((int)convertDistance(Main.WHEEL_RADIUS, distance), false);
		
		isGoingStraight = false;
		
	}


	/**
	 * Makes the robot go forward.
	 *
	 * It will never stop until {@link #stopMoving()} is called.
	 */
	public void goForward() {
		
		isGoingStraight = true;

		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);

		leftMotor.forward();
		rightMotor.forward();

	}

	/**
	 * Makes the robot turn on itself to the left.
	 *
	 * It will never stop until {@link #stopMoving()} is called.
	 */
	public void turnLeft() {
		
		isGoingStraight = false;
		
		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);

		leftMotor.backward();
		rightMotor.forward();
	}

	/**
	 * Makes the robot turn on itself to the right.
	 *
	 * It will never stop until {@link #stopMoving()} is called.
	 */
	public void turnRight() {
		
		isGoingStraight = false;
		
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
		
		isGoingStraight = false;
		
		leftMotor.stop(true);
		rightMotor.stop(true);
	}

	/**
	 * Calculates the minimal angle the robot needs to turn in order to get to the target theta.
	 *
	 * Used in {@link #turnTo(double)}.
	 *
	 * @param currentTheta the robot's current angle position (in degrees)
	 * @param targetTheta the target angle position (in degrees)
	 * @return the minimal angle the robot needs to turn to reach the target angle position (in degrees)
	 */
	public static double minimalAngleDifference(double currentTheta, double targetTheta) {
		double difference = targetTheta - currentTheta;
		if (difference > 180.0) {
			difference = difference - 360.0;
		} else if (difference < -180.0) {
			difference = difference + 360.0;
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
