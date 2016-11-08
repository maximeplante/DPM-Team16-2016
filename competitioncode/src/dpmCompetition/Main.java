package dpmCompetition;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Main {

	static private final String upperUsSensorPort = "A";

	public static void main(String[] args) {
		
		// Motos provider
		MotorsController motorsController = new MotorsController();
		
		// Odometry
		OdometerCorrection odometerCorrection = new OdometerCorrection();
		Odometer odometer = new Odometer(motorsController, 25, true);
		
		// Sensors
		EV3UltrasonicSensor upperUs = new EV3UltrasonicSensor(LocalEV3.get().getPort(upperUsSensorPort));
		UsPoller upperUsPoller = new UsPoller(upperUs);
		
		// Localization
		Localizer localizer = new Localizer(odometer, upperUsPoller, null, motorsController);
		
		// Display
		Display display = new Display(LocalEV3.get().getTextLCD());
		
		// Competition brain
		Brain brain = new Brain();

		// Starting the threads
		odometerCorrection.start();
		upperUsPoller.start();
		display.start();
		brain.start();
		
		// Wait for the user to quit
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
