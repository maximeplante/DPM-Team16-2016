package dpmCompetition.testing;

import lejos.hardware.Sound;
import dpmCompetition.*;

/**
 * For testing class areaScanner
 *
 */
public class AreaScannerTest extends Thread {
	/** Reference to AreaScanner */
	private AreaScanner areaScanner;
	/** Reference to Driver */
	private Driver driver;
	/** Reference to Localizer */
	private Localizer localizer;

	/**
	 * Constructor 
	 * 
	 * @param areaScanner
	 * @param driver
	 * @param localizer
	 */
	public AreaScannerTest(AreaScanner areaScanner, Driver driver, Localizer localizer) {
		
		this.areaScanner = areaScanner;
		this.driver = driver;
		this.localizer = localizer;
		
	}
	
	
	public void run() {
		
		//localizer.localize();
		
		// start scanning 
		Block[] blocks = areaScanner.findCloseObjects();
		// beeps the number of blocks found
		for (Block block: blocks) {
			Sound.beep();
			Helper.sleep(500);
		}
		
	}
	
}
