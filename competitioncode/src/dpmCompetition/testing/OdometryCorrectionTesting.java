package dpmCompetition.testing;

import dpmCompetition.AreaScanner;
import dpmCompetition.Driver;
import dpmCompetition.Localizer;
import dpmCompetition.OdometerCorrection;

public class OdometryCorrectionTesting extends Thread{
	private OdometerCorrection odoCorrection;
	private Driver driver;
	private Localizer localizer;
	
	public OdometryCorrectionTesting(OdometerCorrection odoCorrection, Driver driver, Localizer localizer) {
		this.odoCorrection = odoCorrection;
		this.driver = driver;
		this.localizer = localizer;
	}
	
	public void run(){
		localizer.localize();
		driver.travelTo(15, 0);
		driver.travelTo(15, 70);
	}
	
}
