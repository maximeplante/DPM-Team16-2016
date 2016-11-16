package dpmCompetition;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * 
 * Provides the robot the capabilities of catching a block 
 * and releasing the tower that it has carried.
 * 
 */
public class BlockManipulator {
	
	/** Reference to the claw's motor */
	private EV3LargeRegulatedMotor clawMotor;
	
	/** Reference to the door's motor */
	private EV3LargeRegulatedMotor doorMotor;
	
	/** Reference to the navigation used by the robot */
	private Navigation navigation;
	
	/** Distance needed for the robot to go backward in order to drop the claw */
	private static final int backward = 15;
	
	/** Degree needed to be rotated to drop the claw */
	private static final int clawDegree = 350;
	
	/** Claw's and door's motor rotate speed */
	private static final int turnSpeed = 20;
	
	/** Degree needed to be rotated in order to open the door */
	private static final int doorDegree = 90;
	
	/** Distance needed for the robot to go forward to release the tower */
	private static final int forward = 30;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param motorsController provides a way to access the motors
	 * @param navigation controls robot's motion
	 */
	public BlockManipulator(MotorsController motorsController, Navigation navigation) {
		this.clawMotor = motorsController.getClawMotor();
		this.doorMotor = motorsController.getDoorMotor();
		this.navigation = navigation;
	}
	
	/**
	 * The robot goes backward, drops the claw, goes forward, catches the block 
	 * and dumps the block into its back bin.
	 */
	public void catchBlock(){
		navigation.goForward(-backward);
		clawMotor.setSpeed(turnSpeed);
		clawMotor.rotate(-clawDegree);
		navigation.goForward(backward);
		clawMotor.rotate(clawDegree);
	}
	
	/**
	 * The robot opens its door, goes forward, releases its tower and closes its door. 
	 */
	public void releaseTower(){
		doorMotor.setSpeed(turnSpeed);
		doorMotor.rotate(-doorDegree);
		navigation.goForward(forward);
		doorMotor.rotate(doorDegree);
		
	}
}
