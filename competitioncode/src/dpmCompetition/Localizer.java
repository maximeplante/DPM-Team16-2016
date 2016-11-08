package dpmCompetition;

public class Localizer {

	private Odometer odo;
	private Navigation navigation;
	private UsPoller usPoller;

//	private int noiseMargin = 10;
	private double tileLength = 30.48; //take out if in other class
	private double USplace = 7; //distance from us sensor to center of chassi
	private double xCoord, yCoord, dist;

	public Localizer(Odometer odo, UsPoller usPoller) {
		this.odo = odo;
		this.usPoller = usPoller;
	}

	public void localize() {
		double angleA, angleB;
		// rotate the robot until it sees no wall

		while (seesWall()) {
			navigation.turnLeft();
		}
		// keep rotating until the robot sees a wall, then latch the angle
		while (!seesWall()) {
			navigation.turnRight();
		}
		angleA = odo.getTheta();

		// switch direction and wait until it sees no wall
		while (seesWall()) {
			navigation.turnLeft();
		}
		// keep rotating until the robot sees a wall, then latch the angle
		while (!seesWall()) {
			navigation.turnLeft();
		}
		angleB = odo.getTheta();

		//handles wrapping when angleA is larger than angleB
		if (angleA > angleB) {
			angleB += 360;
		}
		// angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'
		angleB = 45+((angleB-angleA)/2);
		odo.setPosition(new double [] {0.0, 0.0, angleB}, new boolean [] {true, true, true});
		navigation.turnTo(180);
		dist = usPoller.getFilteredData()+ USplace;	
		xCoord = dist - tileLength;
		navigation.turnTo(270);
		dist = usPoller.getFilteredData()+ USplace;
		yCoord = dist - tileLength;
		odo.setPosition(new double [] {xCoord, yCoord, odo.getTheta()}, new boolean [] {true, true, true});
		navigation.turnTo(0);
	}
	private boolean seesWall() {
		if (usPoller.getFilteredData() < 25)
			return true;
		else
			return false;
//		else if (usPoller.getFilteredData() >= 25 + noiseMargin)
//			seesWall = false;
	}
}
