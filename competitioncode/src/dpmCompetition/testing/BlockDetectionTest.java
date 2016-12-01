package dpmCompetition.testing;

import dpmCompetition.LsPoller;
import lejos.hardware.Sound;

/**
 * For testing block detection
 *
 */
public class BlockDetectionTest extends Thread{
	/** Reference to front lsPoller */
	private final LsPoller frontLsPoller;
	
	/**
	 * Constructor
	 * @param frontLsPoller
	 */
	public BlockDetectionTest(LsPoller frontLsPoller){
		this.frontLsPoller = frontLsPoller;
	}
	
	public void run(){
		while(true){
			// beep when sees a wooden block
			if (frontLsPoller.isSeeingWoodenBlock()){
				Sound.beep();
			}
			
			sleep(50);
		}
	}
	
	/**
	 * sleep method
	 * @param time
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
