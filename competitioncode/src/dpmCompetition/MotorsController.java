package dpmCompetition;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class MotorsController {
	
	private final String leftWheelMotorPort = "A";
	private final String rightWheelMotorPort = "D";
	private final String doorMotorPort = "C";
	private final String clawMotorPort = "D";
	
	private final EV3LargeRegulatedMotor leftWheelMotor;
	private final EV3LargeRegulatedMotor rightWheelMotor;
	private final EV3LargeRegulatedMotor doorMotor;
	private final EV3LargeRegulatedMotor clawMotor;
	
	MotorsController() {
		leftWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(leftWheelMotorPort));
		rightWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(rightWheelMotorPort));
		doorMotor = null; //new EV3LargeRegulatedMotor(LocalEV3.get().getPort(doorMotorPort));
		clawMotor = null; //new EV3LargeRegulatedMotor(LocalEV3.get().getPort(clawMotorPort));
	}
	
	public EV3LargeRegulatedMotor getLeftWheelMotor() {
		return leftWheelMotor;
	}
	
	public EV3LargeRegulatedMotor getRightWheelMotor() {
		return rightWheelMotor;
	}

	public EV3LargeRegulatedMotor getDoorMotor() {
		return doorMotor;
	}

	public EV3LargeRegulatedMotor getClawMotor() {
		return clawMotor;
	}
	
}
