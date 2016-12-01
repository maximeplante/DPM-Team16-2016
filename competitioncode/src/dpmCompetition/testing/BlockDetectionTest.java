package dpmCompetition.testing;

import com.sun.istack.internal.localization.Localizer;

import dpmCompetition.Display;
import dpmCompetition.Driver;
import dpmCompetition.LsPoller;
import dpmCompetition.UsPoller;
import lejos.hardware.Sound;

public class BlockDetectionTest extends Thread{
	private final LsPoller frontLsPoller;
	private Driver driver;
	
	public BlockDetectionTest(LsPoller frontLsPoller,Driver driver){
		this.frontLsPoller = frontLsPoller;
		this.driver = driver;
	}
	
	public void run(){
		//driver.travelTo(90,0);
		while(true){
			
			if (frontLsPoller.isSeeingWoodenBlock()){
				Sound.beep();
			}
			
			sleep(50);
		}
	}
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
