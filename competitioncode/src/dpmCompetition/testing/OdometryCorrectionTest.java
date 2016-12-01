package dpmCompetition.testing;

import dpmCompetition.Driver;
import dpmCompetition.Localizer;
import dpmCompetition.Navigation;
import dpmCompetition.OdometerCorrection;

/** 
 * For testing odometry correction 
 * 
 */
public class OdometryCorrectionTest extends Thread{
	/** Reference to Driver */
	private Driver driver;
	/** Reference to Localizer */
	private Localizer localizer;
	/** Reference to Navigation */
	private Navigation navigation;
	
	/**
	 * Constructor 
	 * @param driver
	 * @param localizer
	 * @param navigation
	 */
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
