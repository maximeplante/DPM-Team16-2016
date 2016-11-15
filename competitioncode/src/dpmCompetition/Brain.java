package dpmCompetition;

import lejos.hardware.Sound;

public class Brain extends Thread {
	
	private Localizer localizer;
	private Driver driver;
	private Navigation navigation;
	private Odometer odo;
	
	Brain(Localizer localizer, Driver driver, Navigation navigation, Odometer odo) {
		this.localizer = localizer;
		this.driver = driver;
		this.navigation = navigation;
		this.odo = odo;
	}
	
	public void run() {
		int startXcoord;
		int startYcoord;
		int startAng;
		Sound.setVolume(100);
		
		localizer.localize();
		driver.travelTo(0,0);
		/*while (driver.isNavigating()) {
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		driver.travelTo(30,30);*/
		//navigation.turnTo(0);
//		if (X1){
			startXcoord = 0;
			startYcoord = 0;
			startAng = 0;
//		}
//		else if (X2) {
//			startXcoord = 11;
//			startYcoord = 0;
//			startAng = 1;
//		}
//		else if (X3) {
//			startXcoord = 11;
//			startYcoord = 11;
//			startAng = 2;
//		}
//		else {
//			startXcoord = 0;
//			startYcoord = 11;
//			startAng = 3;
//		}
		//odo.setPosition(new double [] {startXcoord*Main.TILE_LENGTH, startYcoord*Main.TILE_LENGTH,startAng*90}, new boolean [] {true, true, true});
		Sound.beep();
	}
}
