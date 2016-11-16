package dpmCompetition;

public class Driver {
	private Odometer odometer;
	private Navigation navigation;
	private CompetitionData competitionData;
	private AreaScanner areaScanner;

	private static final double acceptableError = 2;

	private final int TIMEOUT_PERIOD = 20;

	private final double squareOffset = 15.0;

	Driver(Odometer odometer, Navigation navigation, CompetitionData competitionData, AreaScanner areaScanner) {

		this.odometer = odometer;
		this.navigation = navigation;
		this.competitionData = competitionData;
		this.areaScanner = areaScanner;

	}

	public void travelToBlueBlock() {
		Coordinate[] blueBlocks = areaScanner.findCloseObjects();
		int index = 0;
		double odometerX = odometer.getX();
		double odometerY = odometer.getY();
		double minDist = Math
				.sqrt(Math.pow(blueBlocks[0].x - odometerX, 2) + Math.pow(blueBlocks[0].y - odometerY, 2));
		for (int i = 1; i < blueBlocks.length; i++) {
			double dist = Math
					.sqrt(Math.pow(blueBlocks[i].x - odometerX, 2) + Math.pow(blueBlocks[i].y - odometerY, 2));
			if (dist < minDist){
				minDist = dist;
				index = i;
			}
		}
		travelTo(blueBlocks[index].x, blueBlocks[index].y);
	}

	// only consider the case that the robot will only go to the green zone once
	public void travelToGreenZone() {
		double x = competitionData.greenZone.lowerLeft.x;
		double y = competitionData.greenZone.lowerLeft.y;

		travelTo(x + squareOffset, y + squareOffset);
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

			// Calculate the angle the plant needs to face in order to get to
			// the target
			double angle = Math.toDegrees(Math.atan2(distY, distX));

			// Turn only if the minimal angle to turn is larger than 50 degrees
			// (in any direction)
			// Prevents the plant from doing a lot of small turns that could
			// induce more error in the odometer.
			if (Navigation.minimalAngleDifference(odometer.getTheta(), angle) > acceptableError
					|| Navigation.minimalAngleDifference(odometer.getTheta(), angle) < -acceptableError) {
				navigation.turnTo(angle);
			}

			// After turning, go forward in the new direction.
			navigation.goForward();

			try {
				Thread.sleep(TIMEOUT_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Helper method that hides the complexity of Thread.sleep() and simplifies
	 * the code.
	 * 
	 * @param time
	 *            the sleep delay in milliseconds
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
