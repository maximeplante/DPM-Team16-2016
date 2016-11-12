package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Driver extends Thread{
	private double targetX;
	private double targetY;
	
	private Odometer odometer;
	private Navigation navigation;
	
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	private static final double acceptableError = 5;
	
	private boolean isNavigating;
	private boolean isGoingStraight;
	
	Driver(Odometer odometer, Navigation navigation){
		
		this.odometer = odometer;
		this.navigation = navigation;
		
		this.isNavigating = false;
		this.isGoingStraight = false;
		
		this.targetX = 0;
		this.targetY = 0;
	}
	
	public void travelTo(double x, double y) {
		
		targetX = x;
		targetY = y;
		isNavigating = true;
			
		}
	
	public void timedOut() {
			
			double distX = targetX - odometer.getX();
			double distY = targetY - odometer.getY();
			double distanceFromTarget = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
			
			if (distanceFromTarget < acceptableError) {
				// Do nothing
				leftMotor.stop();
				rightMotor.stop();
				isNavigating = false;
				return;
			}
			
			double angle = Math.toDegrees(Math.atan2(distX, distY));
			// Prevent theta from being smaller that 0 and bigger than 360
			if (angle> 180){
				angle = angle-360;
			}else if (angle < -180) {
				angle = angle + 360;
			}
			if (angle - odometer.getTheta() > acceptableError) {
				isGoingStraight = false;
				navigation.turnTo(angle);
			}
			
			navigation.goForward();
			isGoingStraight = true;
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
