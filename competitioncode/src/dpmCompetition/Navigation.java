package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Navigation {
		
	private Odometer odometer;
	
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	// Speed constants
	private static final int FORWARD_SPEED = 100;
	private static final int TURN_SPEED = 100;
	
	private static double WHEEL_RADIUS = 1.4;
	private static double TRACK = 12.5;
	
	Navigation(Odometer odometer, MotorsController motorsController) {
		
		this.odometer = odometer;
		this.leftMotor = motorsController.getLeftWheelMotor();
		this.rightMotor = motorsController.getRightWheelMotor();
		
	}
	
	// This method turns the plant to be make it aligned with a specific theta (does it with the minimal turning angle)
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
	
	// Makes the robot go forward by a certain distance (cm)
	public void goForward(double distance) {
		leftMotor.rotate((int)convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate((int)convertDistance(WHEEL_RADIUS, distance), false);
	}
	
	// Makes the plant go forward
	public void goForward() {
		
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);

		leftMotor.forward();
		rightMotor.forward();
		
	}
	
	// Makes the robot turn left until stopMotors() is called
	public void turnLeft() {
		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);
		
		leftMotor.backward();
		rightMotor.forward();
	}
	
	// Makes the robot turn right until stopMotors() is called
	public void turnRight() {
		leftMotor.setSpeed(TURN_SPEED);
		rightMotor.setSpeed(TURN_SPEED);
		
		leftMotor.forward();
		rightMotor.backward();
	}
	
	// Stops the motors
	public void stopMotors() {
		leftMotor.stop(true);
		rightMotor.stop(true);
	}
	
	// Calculates the minimal angle the plant needs to turn in order to get to the target theta
	// NOTE: the parameters and the return value are in radians.
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
	private static double convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	// Code given with the lab 2
	// Calculates how much degrees the wheels should turn to cover a given distance (cm)
	// NOTE: the return value is in degrees.
	private static double convertDistance(double radius, double distance) {
		return ((180.0 * distance) / (Math.PI * radius));
	}
	
}
