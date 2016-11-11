package dpmCompetition.testing;

import dpmCompetition.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class DoubleUltrasonicSensorHeight extends Thread {
	
	private final EV3LargeRegulatedMotor leftMotor;
	private final EV3LargeRegulatedMotor rightMotor;
	
	private static double radius = 2.15;
	private static double tileLength = 30.48;
	
	private final UsPoller upperUsPoller;
	private final UsPoller lowerUsPoller;
	
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
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

}

