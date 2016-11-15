package dpmCompetition;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class MotorsController {
	
	private final EV3LargeRegulatedMotor leftWheelMotor;
	private final EV3LargeRegulatedMotor rightWheelMotor;
	private final EV3LargeRegulatedMotor doorMotor;
	private final EV3LargeRegulatedMotor clawMotor;
	
	MotorsController() {
		leftWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.LEFT_WHEEL_MOTOR_PORT));
		rightWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.RIGHT_WHEEL_MOTOR_PORT));
		doorMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.DOOR_MOTOR_PORT));
		clawMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.CLAW_MOTOR_PORT));
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
