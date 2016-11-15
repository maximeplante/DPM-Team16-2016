package dpmCompetition;

public class Driver {
	private Odometer odometer;
	private Navigation navigation;
	
	private static final double acceptableError = 2;

	private final int TIMEOUT_PERIOD = 20;
	
	private boolean isGoingStraight;
	
	Driver(Odometer odometer, Navigation navigation){
		
		this.odometer = odometer;
		this.navigation = navigation;
		
		this.isGoingStraight = false;
		
	}
	
	public void travelTo(double x, double y) {
		
		boolean traveling = true;
		
		while (traveling) {
			double distX = x - odometer.getX();
			double distY = y - odometer.getY();
			double distanceFromTarget = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
			
			if (distanceFromTarget < acceptableError) {
				navigation.stopMoving();
				traveling = false;
				continue;
			}
			
			// Calculate the angle the plant needs to face in order to get to the target
			double angle = Math.toDegrees(Math.atan2(distY, distX));
			
			// Turn only if the minimal angle to turn is larger than 50 degrees (in any direction)
			// Prevents the plant from doing a lot of small turns that could induce more error in the odometer.
			if (Navigation.minimalAngleDifference(odometer.getTheta(), angle) > acceptableError || Navigation.minimalAngleDifference(odometer.getTheta(), angle) < -acceptableError) {
				navigation.turnTo(angle);
			}
			
			// After turning, go forward in the new direction.
			navigation.goForward();
			isGoingStraight = true;
			
			try {
				Thread.sleep(TIMEOUT_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
	public boolean isGoingStraight() {
		return isGoingStraight;
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
