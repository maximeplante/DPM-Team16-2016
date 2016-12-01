package dpmCompetition.testing;

import dpmCompetition.AreaScanner;
import dpmCompetition.Block;
import dpmCompetition.Driver;
import dpmCompetition.Localizer;
import dpmCompetition.Navigation;
import lejos.hardware.Sound;

/** 
 * For testing localization
 *
 */
public class LocalizationTest extends Thread{
	/** Reference to Driver */
	private Driver driver;
	/** Reference to Localizer */
	private Localizer localizer;
	/** Reference to Navigation */
	private Navigation navigation;
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param localizer
	 * @param navigation
	 */
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