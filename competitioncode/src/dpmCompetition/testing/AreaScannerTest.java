package dpmCompetition.testing;

import lejos.hardware.Sound;
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
		
		for (Block block: blocks) {
			Sound.beep();
			Helper.sleep(500);
		}
		
	}
	
}
