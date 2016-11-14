package dpmCompetition;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
/**
 * 
 * @author vivek
 * Calculates the X, Y displacement and the angle the robot is facing from a set origin (heading). 
 * Class extends Thread; it will be constantly running in a separate thread
 *
 */
public class Odometer extends Thread {
	/**
	 * Reference to the robot's left and right wheel motors
	 */
	private EV3LargeRegulatedMotor leftMotor, rightMotor;

	private final int DEFAULT_TIMEOUT_PERIOD = 20;
	
	/**
	 * Time the thread sleeps for before starting again
	 */
	private final int TIMEOUT_PERIOD = 20;
	/**
	 * Radius for both wheels, and the width of robot (cm)
	 */
	private double leftRadius, rightRadius, width;
	/**
	 * calculated x, y displacement and angle of orientation (heading)
	 */

	private double x, y, theta;
	/**
	 * x,y and theta are to updated according to the previous displacement of robot
	 */
	private double[] oldDH, dDH;
	/**
	 * provides access to motors (left and right wheel motors)
	 */
	private MotorsController motorsController;
	/**
	 * Constructor
	 * @param motorsController provides access to left and right wheel motors
	 */
	
	public Odometer (MotorsController motorsController) {
		this.motorsController = motorsController;
		this.leftMotor = this.motorsController.getLeftWheelMotor();
		this.rightMotor = this.motorsController.getRightWheelMotor();
				
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 0.0;
		this.oldDH = new double[2];
		this.dDH = new double[2];
	}
	
	/**
	 * Calculates displacement and heading as title suggests
	 */
	private void getDisplacementAndHeading(double[] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();

		data[0] = (leftTacho * Main.WHEEL_RADIUS + rightTacho * Main.WHEEL_RADIUS) * Math.PI / 360.0;
		data[1] = (rightTacho * Main.WHEEL_RADIUS - leftTacho * Main.WHEEL_RADIUS) / Main.TRACK;
	}
	
	/**
	 * Recompute the odometer values using the displacement and heading changes
	 */
	public void run() {
		
		while (true) {
			this.getDisplacementAndHeading(dDH);
			dDH[0] -= oldDH[0];
			dDH[1] -= oldDH[1];

			// update the position in a critical region
			synchronized (this) {
				theta += dDH[1];
				theta = fixDegAngle(theta);

				x += dDH[0] * Math.cos(Math.toRadians(theta));
				y += dDH[0] * Math.sin(Math.toRadians(theta));
			}

			oldDH[0] += dDH[0];
			oldDH[1] += dDH[1];
			
			Display.print(getX(), 0);
			Display.print(getY(), 1);
			Display.print(getTheta(), 2);
			
			try {
				Thread.sleep(TIMEOUT_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		/** 
		 * @return current X displacement of robot
		 */
		public double getX() {
			synchronized (this) {
				return x;
			}
		}

		/**
		 * @return current Y displacement of robot
		 */
		public double getY() {
			synchronized (this) {
				return y;
			}
		}

		/**
		 * @return current Heading of robot
		 */
		public double getTheta() {
			synchronized (this) {
				return theta;
			}
		}

		/**
		 * @param position array which contains current X and Y displacements
		 * @param update 
		 */
		public void setPosition(double[] position, boolean[] update) {
			synchronized (this) {
				if (update[0])
					x = position[0];
				if (update[1])
					y = position[1];
				if (update[2])
					theta = position[2];
			}
		}

		/**
		 * @param position array which stores current X, Y and Theta of robot
		 */
		public void getPosition(double[] position) {
			synchronized (this) {
				position[0] = x;
				position[1] = y;
				position[2] = theta;
			}
		}
		/** 
		 * @return the array which contains current X,Y and Theta of robot 
		 */
		public double[] getPosition() {
			synchronized (this) {
				return new double[] { x, y, theta };
			}
		}
		
		/**
		 * 
		 * @param angle
		 * @return
		 */
		public static double fixDegAngle(double angle) {
			if (angle < 0.0)
				angle = 360.0 + (angle % 360.0);

			return angle % 360.0;
		}
		/**
		 * is this method ever used? doesn't seem like it... if it is not being used, how's it turning min angle?
		 * @param a
		 * @param b
		 * @return
		 */
		public static double minimumAngleFromTo(double a, double b) {
			double d = fixDegAngle(b - a);

			if (d < 180.0)
				return d;
			else
				return d - 360.0;
		}

}
