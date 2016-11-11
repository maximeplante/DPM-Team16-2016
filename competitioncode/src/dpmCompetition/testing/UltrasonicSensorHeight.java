package dpmCompetition.testing;

import dpmCompetition.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class UltrasonicSensorHeight extends Thread {
	
	private final EV3LargeRegulatedMotor leftMotor;
	private final EV3LargeRegulatedMotor rightMotor;
	
	private static double radius = 2.15;
	private static double tileLength = 30.48;
	
	private final UsPoller usPoller;
	
	public UltrasonicSensorHeight(MotorsController motorsController, UsPoller usPoller) {
		
		leftMotor = motorsController.getLeftWheelMotor();
		rightMotor = motorsController.getRightWheelMotor();
		
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
			System.out.print(String.format("%d: %d%n", System.currentTimeMillis(), distance));
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

