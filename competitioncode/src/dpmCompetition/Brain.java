package dpmCompetition;

import lejos.hardware.Sound;

/**
 *
 * Controls robot's logic for the competition
 *
 */
public class Brain extends Thread {
	/** Reference to the localizer used by the robot */
	private Localizer localizer;
	/** Reference to the driver used by the robot */
	private Driver driver;
	/** Reference to the navigation used by the robot */
	private Navigation navigation;
	/** Reference to the odometer used by the robot */
	private Odometer odo;
	/** Reference to the blockManipulator used by the robot */
	private BlockManipulator blockManipulator;
	private AreaScanner areaScanner;

	/**
	 * Constructor
	 *
	 * @param localizer a reference to the localizer
	 * @param driver a reference to the driver
	 * @param navigation a reference to the navigation
	 * @param odo a reference to the odometer
	 * @param blockManipulator a reference to the blockManipulator
	 */
	Brain(Localizer localizer, Driver driver, Navigation navigation, Odometer odo, BlockManipulator blockManipulator, AreaScanner areaScanner) {
		this.localizer = localizer;
		this.driver = driver;
		this.navigation = navigation;
		this.odo = odo;
		this.blockManipulator = blockManipulator;
		this.areaScanner = areaScanner;
	}

	public void run() {
		int startXcoord;
		int startYcoord;
		int startAng;
		Sound.setVolume(100);

		localizer.localize();
		driver.travelTo(0,0);
		navigation.turnTo(0);
		//if (X1){
			startXcoord = 0;
			startYcoord = 0;
			startAng = 0;
		/*}
		else if (X2) {
			startXcoord = 11;
			startYcoord = 0;
			startAng = 1;
		}
		else if (X3) {
			startXcoord = 11;
			startYcoord = 11;
			startAng = 2;
		}
		else {
			startXcoord = 0;
			startYcoord = 11;
			startAng = 3;
		}*/
		odo.setPosition(new double [] {startXcoord*Main.TILE_LENGTH, startYcoord*Main.TILE_LENGTH,startAng*90}, new boolean [] {true, true, true});

		Coordinate[] objects = areaScanner.findCloseObjects();
		driver.travelTo(objects[0].x, objects[0].y);

		blockManipulator.catchBlock();

	}
}
