package dpmCompetition;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Main {

	/** The port used on the EV3 brick to connect the upper ultrasonic sensor */
	static public final String UPPER_US_SENSOR_PORT = "S1";
	/** The port used on the EV3 brick to connect the lower ultrasonic sensor */
	static public final String LOWER_US_SENSOR_PORT = "S2";
	/** The horizontal offset in cm of the upper ultrasonic sensor from the wheels' chassi */
	static public final double UPPER_US_OFFSET = 7.5;
	/** The horizontal offset in cm of the lower ultrasonic sensor from the wheels' chassi */
	static public final double LOWER_US_OFFSET = 7.5;
	
	/** The length of a tile on the demo board (cm) */
	static public final double TILE_LENGTH = 30.48;
	
	/** The radius of the robot's wheels (cm) */
	static public final double WHEEL_RADIUS = 2.1;
	/** The distance between the robot's wheels (cm) */
	static public final double TRACK = 15.4;
	
	/** The port used on the EV3 brick to connect the left wheel motor */
	static public final String LEFT_WHEEL_MOTOR_PORT = "A";
	/** The port used on the EV3 brick to connect the right wheel motor */
	static public final String RIGHT_WHEEL_MOTOR_PORT = "D";
	/** The port used on the EV3 brick to connect the claw motor */
	static public final String CLAW_MOTOR_PORT = "B";
	/** The port used on the EV3 brick to connect the door motor */
	static public final String DOOR_MOTOR_PORT = "C";

	public static void main(String[] args) {
		
		// Motos provider
		MotorsController motorsController = new MotorsController();
		
		// Odometry
		//OdometerCorrection odometerCorrection = new OdometerCorrection();
		Odometer odometer = new Odometer(motorsController, 25);
		
		// Sensors
		EV3UltrasonicSensor upperUs = new EV3UltrasonicSensor(LocalEV3.get().getPort(UPPER_US_SENSOR_PORT));
		UsPoller upperUsPoller = new UsPoller(upperUs);
		
		// Navigation
		Navigation navigation = new Navigation(odometer, motorsController);
		
		// Localization
		Localizer localizer = new Localizer(odometer, upperUsPoller, navigation);
		
		// Display
		Display display = new Display(LocalEV3.get().getTextLCD());
		
		// Competition brain
		Brain brain = new Brain(localizer);

		// Starting the threads
		//odometerCorrection.start();
		upperUsPoller.start();
		display.start();
		odometer.start();
		brain.start();
		
		// Wait for the user to quit
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
