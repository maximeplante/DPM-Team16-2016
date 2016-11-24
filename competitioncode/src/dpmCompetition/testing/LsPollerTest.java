package dpmCompetition.testing;

import dpmCompetition.Display;
import dpmCompetition.Driver;
import dpmCompetition.LsPoller;
import dpmCompetition.UsPoller;
import lejos.hardware.Sound;

public class LsPollerTest extends Thread{
	private final LsPoller rightLsPoller;
	private final LsPoller leftLsPoller;
	private Driver driver;
	
	public LsPollerTest(LsPoller rightLsPoller, LsPoller leftLsPoller,Driver driver){
		this.rightLsPoller = rightLsPoller;
		this.leftLsPoller = leftLsPoller;
		this.driver = driver;
	}
	
	public void run(){
		//driver.travelTo(90,0);
		while(true){
			if (rightLsPoller.isSeeingBlackLine()){
				Display.print("right", 3);
			}
			
			if (leftLsPoller.isSeeingBlackLine()){
				Display.print("left", 4);
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
