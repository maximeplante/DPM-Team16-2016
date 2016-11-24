package dpmCompetition;

import java.io.IOException;
import java.util.HashMap;

import dpmCompetition.testing.AreaScannerTest;
import dpmCompetition.testing.BlockCatchingTest;
import dpmCompetition.testing.NavigationTest;
import dpmCompetition.wifi.WifiConnection;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Main {
	
	/** The ports used on the EV3 brick to connect the color sensor */
	static public final String Front_LS_PORT = "S2";
	static public final String Left_LS_PORT = "S3";
	static public final String Right_LS_PORT = "S4";
	
	/** The port used on the EV3 brick to connect the ultrasonic sensor */
	static public final String US_SENSOR_PORT = "S1";
	/** The horizontal offset in cm of the ultrasonic sensor from the wheels' chassi */
	static public final double UPPER_US_OFFSET = 14.0;
	
	/** The length of a tile on the demo board (cm) */
	static public final double TILE_LENGTH = 30.48;
	
	/** The radius of the robot's wheels (cm) */
	static public final double WHEEL_RADIUS = 2.1;
	/** The distance between the robot's wheels (cm) */
	static public final double TRACK = 23.5;
	
	/** The port used on the EV3 brick to connect the left wheel motor */
	static public final String LEFT_WHEEL_MOTOR_PORT = "D";
	/** The port used on the EV3 brick to connect the right wheel motor */
	static public final String RIGHT_WHEEL_MOTOR_PORT = "A";
	/** The port used on the EV3 brick to connect the claw motor */
	static public final String CLAW_MOTOR_PORT = "B";
	/** The port used on the EV3 brick to connect the door motor */
	static public final String DOOR_MOTOR_PORT = "C";
	
	/** The IP address of the competition server */
	static public final String SERVER_IP = "192.168.2.3";
	/** The team number of the robot (used by the wifi code) */
	static public final int TEAM_NUMBER = 16;

	public static void main(String[] args) {
		
		Sound.beep();
		
		CompetitionData competitionData = CompetitionDataFetcher.fetch();
		
		if (competitionData == null) {
			System.out.println("The demo can't start without wifi data. It crashed.");
			while (Button.waitForAnyPress() != Button.ID_ESCAPE);
			System.exit(0);
		}
		
		// Motos provider
		MotorsController motorsController = new MotorsController();
		
		// Odometry
		//OdometerCorrection odometerCorrection = new OdometerCorrection();
		Odometer odometer = new Odometer(motorsController);
		
		// Sensors
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(LocalEV3.get().getPort(US_SENSOR_PORT));
		UsPoller usPoller = new UsPoller(us.getDistanceMode());
		
		EV3ColorSensor ls1 = new EV3ColorSensor(LocalEV3.get().getPort(Front_LS_PORT));
		LsPoller frontLsPoller = new LsPoller(ls1.getRGBMode());

		EV3ColorSensor ls2 = new EV3ColorSensor(LocalEV3.get().getPort(Right_LS_PORT));
		LsPoller rightLsPoller = new LsPoller(ls2.getRedMode());
		
		EV3ColorSensor ls3 = new EV3ColorSensor(LocalEV3.get().getPort(Left_LS_PORT));
		LsPoller leftLsPoller = new LsPoller(ls3.getRedMode());
		
		// Navigation
		Navigation navigation = new Navigation(odometer, motorsController);
		
		// Area Scanner
		AreaScanner areaScanner = new AreaScanner(navigation, usPoller, odometer);

		// Driver
		// Used for Driver debugging
		
		/*CompetitionData competitionData = new CompetitionData();
		competitionData.greenZone.lowerLeft.x = 0;
		competitionData.greenZone.lowerLeft.y = 60;
		competitionData.greenZone.upperRight.x = 60;
		competitionData.greenZone.upperRight.y = 90;*/
		
		Driver driver = new Driver(odometer, navigation, competitionData, areaScanner);
		
		// Localization
		Localizer localizer = new Localizer(odometer, usPoller, navigation);
		
		// Display
		Display display = new Display(LocalEV3.get().getTextLCD());
		
		// BlockManipulator
		BlockManipulator blockManipulator = new BlockManipulator(motorsController, navigation);
				
		// Competition brain
		Brain brain = new Brain(localizer, driver, navigation, odometer, blockManipulator, areaScanner);

		// Starting the threads
		//odometerCorrection.start();
		usPoller.start();
		display.start();
		odometer.start();
		
		frontLsPoller.start();
		rightLsPoller.start();
		leftLsPoller.start();
		
		// Navigation test
		//NavigationTest navigationTest = new NavigationTest(driver, localizer);
		//navigationTest.start();
		
		// Block catching test
		//BlockCatchingTest blockCatchingTest = new BlockCatchingTest(blockManipulator);
		//blockCatchingTest.start();
		
		// Area Scanner Test
		//AreaScannerTest areaScannerTest = new AreaScannerTest(areaScanner, driver, localizer);
		//areaScannerTest.start();
		
		brain.start();
		
		// Wait for the user to quit
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
