package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class BlockManipulator {
	private EV3LargeRegulatedMotor clawMotor;
	private EV3LargeRegulatedMotor doorMotor;
	private Navigation navigation;
	// degree needs to rotate the claw
	private int rotateDegree = 350;
	// claw motor rotate speed
	private int turnSpeed = 20;
	// degree needs to open the door
	private int doorDegree = 90;
	// 
	private int forward = 30;
	
	public BlockManipulator(MotorsController motorsController, Navigation navigation) {
		this.clawMotor = motorsController.getClawMotor();
		this.doorMotor = motorsController.getDoorMotor();
		this.navigation = navigation;
	}
	public void catchBlock(){
		navigation.goForward(-10);
		clawMotor.setSpeed(turnSpeed);
		clawMotor.rotate(-rotateDegree);
		navigation.goForward(10);
		clawMotor.rotate(rotateDegree);
	}
	
	public void releaseTower(){
		doorMotor.setSpeed(turnSpeed);
		doorMotor.rotate(-doorDegree);
		navigation.goForward(forward);
		doorMotor.rotate(doorDegree);
		
	}
}
