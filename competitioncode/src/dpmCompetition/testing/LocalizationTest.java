package dpmCompetition.testing;

import dpmCompetition.AreaScanner;
import dpmCompetition.Block;
import dpmCompetition.Driver;
import dpmCompetition.Localizer;
import dpmCompetition.Navigation;
import lejos.hardware.Sound;

public class LocalizationTest extends Thread{
	private Driver driver;
	private Localizer localizer;
	private Navigation navigation;
	
	public LocalizationTest(Driver driver, Localizer localizer, Navigation navigation) {
		this.driver = driver;
		this.localizer = localizer;
		this.navigation = navigation;
		
	}
	
	
	public void run() {
		
		localizer.localize();
		driver.travelTo(0, 0);
		navigation.turnTo(0);
	}
}