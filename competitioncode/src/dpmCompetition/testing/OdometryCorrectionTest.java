package dpmCompetition.testing;

import dpmCompetition.Driver;
import dpmCompetition.Localizer;
import dpmCompetition.Navigation;
import dpmCompetition.OdometerCorrection;

public class OdometryCorrectionTest extends Thread{
	private Driver driver;
	private Localizer localizer;
	private Navigation navigation;
	
	public OdometryCorrectionTest(Driver driver, Localizer localizer, Navigation navigation) {
		this.driver = driver;
		this.localizer = localizer;
		this.navigation = navigation;
	}
	
	public void run(){
		driver.travelTo(15, 0);
		driver.travelTo(15, 70);
	}
	
}
