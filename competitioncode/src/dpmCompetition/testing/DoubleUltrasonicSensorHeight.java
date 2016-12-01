package dpmCompetition.testing;

import dpmCompetition.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * For testing the effect on the us reading due to the difference 
 * of height that a us is placed
 *
 */
public class DoubleUltrasonicSensorHeight extends Thread {
	/** Reference to the left motor */
	private final EV3LargeRegulatedMotor leftMotor;
	/** Reference to the right motor */
	private final EV3LargeRegulatedMotor rightMotor;
	
	/** Wheel radius */
	private static double radius = 2.15;
	/** Length of a tile */
	private static double tileLength = 30.48;
	
	/** Reference to the upper us poller */
	private final UsPoller upperUsPoller;
	/** Reference to the lower us poller */
	private final UsPoller lowerUsPoller;
	
	/**
	 * Constructor
	 * 
	 * @param motorsController
	 * @param upperUsPoller
	 * @param lowerUsPoller
	 */
	public DoubleUltrasonicSensorHeight(MotorsController motorsController, UsPoller upperUsPoller, UsPoller lowerUsPoller) {
		
		leftMotor = motorsController.getLeftWheelMotor();
		rightMotor = motorsController.getRightWheelMotor();
		
		this.upperUsPoller = upperUsPoller;
		this.lowerUsPoller = lowerUsPoller;
		
	}
	
	public void run(String[] args){
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);
		
		int tachoRot = convertDistance(radius, 6*tileLength);
		leftMotor.rotate(tachoRot, true);
		rightMotor.rotate(tachoRot, true);
		for (int i=0;i<450;i++){
			int upperDistance = (int) upperUsPoller.getFilteredData();
			int lowerDistance = (int) lowerUsPoller.getFilteredData();
			// Print the result to the screen (and an output file)
			System.out.println(String.format("%d,%d,%d", System.currentTimeMillis(), upperDistance, lowerDistance));
			Helper.sleep(100);
		}
	}
	
	/**
	 * Calculates how much degrees the wheels should turn to cover a given distance (cm)
	 *
	 * This code comes from the code given in lab 2.
	 *
	 * @param radius the wheels (cm)
	 * @param distance the desired distance (cm)
	 * @return the amount of degrees the wheels should turn (in degrees)
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

}

