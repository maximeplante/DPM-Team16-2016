package dpmCompetition.testing;

import dpmCompetition.*;
/**
 * For testing navigation
 *
 */
public class NavigationTest extends Thread {
	/** Reference to Driver */
	Driver driver;
	/** Reference to Localizer */
	Localizer localizer;
	
	/**
	 * Constructor 
	 * @param driver
	 * @param localizer
	 */
	public NavigationTest(Driver driver, Localizer localizer) {
		this.driver = driver;
		this.localizer = localizer;
	}
	
	public void run() {
		
		localizer.localize();
		driver.travelTo(30.48, 0);
		driver.travelTo(30.48*2, 30.48*2);
		driver.travelTo(0, 30.48*2);
		driver.travelTo(0, 0);
		
	}
	
}
