
package dpmCompetition.testing;

import dpmCompetition.*;

public class NavigationTest extends Thread {

	Driver driver;
	Localizer localizer;
	
	public NavigationTest(Driver driver, Localizer localizer) {
		this.driver = driver;
		this.localizer = localizer;
	}
	
	public void run() {
		
		localizer.localize();
		driver.travelTo(30, 20);
		
	}
	
}
