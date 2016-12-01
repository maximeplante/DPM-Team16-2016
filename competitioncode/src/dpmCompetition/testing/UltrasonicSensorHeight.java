package dpmCompetition.testing;

import dpmCompetition.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
/**
 * For testing usPoller
 *
 */
public class UltrasonicSensorHeight extends Thread {
	
	/** Reference to the left motor */
	private final EV3LargeRegulatedMotor leftMotor;
	/** Reference to the right motor */
	private final EV3LargeRegulatedMotor rightMotor;
	
	/** Wheel radius */
	private static double radius = 2.15;
	/** Length of a tile */
	private static double tileLength = 30.48;
	
	/** Reference to usPoller */
	private final UsPoller usPoller;
	
	/**
	 * Constructor
	 * @param motorsController
	 * @param usPoller
	 */
	public UltrasonicSensorHeight(MotorsController motorsController, UsPoller usPoller) {
		
		this.leftMotor = motorsController.getLeftWheelMotor();
		this.rightMotor = motorsController.getRightWheelMotor();
		
		this.usPoller = usPoller;
		
	}
	
	public void run(String[] args){
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);
		
		int tachoRot = convertDistance(radius, 6*tileLength);
		leftMotor.rotate(tachoRot, true);
		rightMotor.rotate(tachoRot, true);
		for (int i=0;i<450;i++){
			int distance = (int) usPoller.getFilteredData();
			// Print the result to the screen (and an output file)
			System.out.println(String.format("%d: %d", System.currentTimeMillis(), distance));
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

