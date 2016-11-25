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
	/** Reference to the data of this competition */
	private CompetitionData competitionData;

	/**
	 * Constructor
	 *
	 * @param localizer a reference to the localizer
	 * @param driver a reference to the driver
	 * @param navigation a reference to the navigation
	 * @param odo a reference to the odometer
	 * @param blockManipulator a reference to the blockManipulator
	 * @param competitionData the data for this competition
	 */
	Brain(Localizer localizer, Driver driver, Navigation navigation, Odometer odo, BlockManipulator blockManipulator, CompetitionData competitionData) {
		this.localizer = localizer;
		this.driver = driver;
		this.navigation = navigation;
		this.odo = odo;
		this.blockManipulator = blockManipulator;
		this.competitionData = competitionData;
	}

	public void run() {
		int startXcoord;
		int startYcoord;
		int startAng;
		Sound.setVolume(100);
		
		localizer.localize();
		driver.travelTo(0,0);
		navigation.turnTo(0);
		
		// Choosing the starting corner depending on the robot's role
		int startingCorner = competitionData.builderTeamNumber == Main.TEAM_NUMBER ? competitionData.builderStaringCorner : competitionData.collectorStartingCorner;
		// Setting the X and Y coordinates, and angle position
		switch(startingCorner) {
		case 1:
			startXcoord = 0;
			startYcoord = 0;
			startAng = 0;
			break;
		case 2:
			startXcoord = 10;
			startYcoord = 0;
			startAng = 1;
			break;
		case 3:
			startXcoord = 10;
			startYcoord = 10;
			startAng = 2;
			break;
		case 4:
			startXcoord = 0;
			startYcoord = 10;
			startAng = 3;
			break;
		default:
			startXcoord = 0;
			startYcoord = 0;
			startAng = 0;
			break;
		}
		odo.setPosition(new double [] {startXcoord*Main.TILE_LENGTH, startYcoord*Main.TILE_LENGTH, startAng*90}, new boolean [] {true, true, true});

		driver.travelToBlueBlock();

		blockManipulator.catchBlock();
		driver.travelTo(startXcoord*Main.TILE_LENGTH, startYcoord*Main.TILE_LENGTH);
		//driver.travelToHomeZone();
	
	}
}
