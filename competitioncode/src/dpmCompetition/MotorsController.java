package dpmCompetition;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * 
 * Provides access to the motors
 *
 */
public class MotorsController {
	
	/** Reference to the left wheel's motor */
	private final EV3LargeRegulatedMotor leftWheelMotor;
	/** Reference to the right wheel's motor */
	private final EV3LargeRegulatedMotor rightWheelMotor;
	/** Reference to the door's motor */
	private final EV3LargeRegulatedMotor doorMotor;
	/** Reference to the claw's motor */
	private final EV3LargeRegulatedMotor clawMotor;
	
	/**
	 * Constructor
	 */
	MotorsController() {
		leftWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.LEFT_WHEEL_MOTOR_PORT));
		rightWheelMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.RIGHT_WHEEL_MOTOR_PORT));
		doorMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.DOOR_MOTOR_PORT));
		clawMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Main.CLAW_MOTOR_PORT));
	}
	
	/**
	 * 
	 * @return leftWheelMotor which gives access to left wheel's motor.
	 */
	public EV3LargeRegulatedMotor getLeftWheelMotor() {
		return leftWheelMotor;
	}
	
	/**
	 * 
	 * @return rightWheelMotor which gives access to right wheel's motor.
	 */
	public EV3LargeRegulatedMotor getRightWheelMotor() {
		return rightWheelMotor;
	}

	/**
	 * 
	 * @return doorMotor which gives access to the door's motor.
	 */
	public EV3LargeRegulatedMotor getDoorMotor() {
		return doorMotor;
	}

	/**
	 * 
	 * @return clawMotor which gives access to claw's motor.
	 */
	public EV3LargeRegulatedMotor getClawMotor() {
		return clawMotor;
	}
	
}
