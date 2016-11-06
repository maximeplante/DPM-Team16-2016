package dpmCompetition;

public class Localizer {

	import lejos.hardware.motor.EV3LargeRegulatedMotor;
	public enum LocalizationType { RISING_EDGE };

	public static double ROTATION_SPEED = 60;
	private Odometer odo;
	private Navigation navigation;
	private UsPoller usPoller;
	private LocalizationType locType;
	private MotorsController motorsController;
	private Driver driver = null;

	private boolean seesWall = true; //in diff class?
	private int noiseMargin = 10;
	private double tileLength = 30.48; //take out if in other class
	private double USplace = 7; //distance from us sensor to center of chassi
	private double xCoord, yCoord, dist;

	public Localizer(Odometer odo, UsPoller usPoller, LocalizationType locType, MotorsController motorsController) {
		this.odo = odo;
		this.usPoller = usPoller;
		this.locType = locType;
		this.motorsController = motorsController;
		this.driver = new driver(odo);
	}


	public void doLocalization() {
		double angleA, angleB; 
//		leftMotor.setSpeed((int) ROTATION_SPEED); //set speed in motorsController class?
//		rightMotor.setSpeed((int) ROTATION_SPEED);

		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall

			while (seesWall()) {
				rotateCCW();
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while (!seesWall()) {
				rotateCW();
			}
			angleA = odo.getAng();

			// switch direction and wait until it sees no wall
			while (seesWall()) {
				rotateCCW();
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while (!seesWall()) {
				rotateCCW();
			}
			angleB = odo.getAng();

			//handles wrapping when angleA is larger than angleB
			if (angleA > angleB) {
				angleB += 360;
			}
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			angleB = 45+((angleB-angleA)/2);
			odo.setPosition(new double [] {0.0, 0.0, angleB}, new boolean [] {true, true, true}); //change to setX, setY, setTheta
			navigation.turn(180-odo.getAng());
			dist = usPoller.getFilteredData()+ USplace;	
			xCoord = dist - tileLength;
			navigation.turn(270-odo.getAng());
			dist = getFilteredData()+ USplace;
			yCoord = dist - tileLength;
			odo.setPosition(new double [] {xCoord, yCoord, odo.getAng()}, new boolean [] {true, true, true});
//			driver.goTo(0.0, 0.0);
			navigation.turn(0-odo.getAng());
		}
	}
	private boolean seesWall() {
		if (usPoller.getFilteredData() < 25)
			seesWall = true;
		else if (usPoller.getFilteredData() >= 25 + noiseMargin)
			seesWall = false;
		return seesWall; //returns seesWall as is if within noise margin
	}
	private void rotateCW() {
		rightMotor.backward();
		leftMotor.forward();
	}
	private void rotateCCW() {
		rightMotor.forward();
		leftMotor.backward();
	}
}
