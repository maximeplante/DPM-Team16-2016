package dpmCompetition.testing;

import dpmCompetition.*;

public class AreaScannerTest extends Thread {
	
	private AreaScanner areaScanner;
	private Driver driver;
	private Localizer localizer;

	public AreaScannerTest(AreaScanner areaScanner, Driver driver, Localizer localizer) {
		
		this.areaScanner = areaScanner;
		this.driver = driver;
		this.localizer = localizer;
		
	}
	
	
	public void run() {
		
		//localizer.localize();
		
		Block[] blocks = areaScanner.findCloseObjects();
		
		driver.travelTo(blocks[0].center.x, blocks[0].center.y);
		
	}
	
}
