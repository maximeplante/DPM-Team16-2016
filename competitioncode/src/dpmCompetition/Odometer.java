package dpmCompetition;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class Odometer extends Thread {
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private final int DEFAULT_TIMEOUT_PERIOD = 20;
	private double leftRadius, rightRadius, width;
	private double x, y, theta;
	private double[] oldDH, dDH;
	// motor controller
	private MotorsController motorsController;
	
	// constructor
	public Odometer (MotorsController motorsController, int INTERVAL) {
		this.motorsController = motorsController;
		this.leftMotor = this.motorsController.getLeftWheelMotor();
		this.rightMotor = this.motorsController.getRightWheelMotor();
				
		// default values, modify for your robot
		this.rightRadius = 2.1;
		this.leftRadius = 2.1;
		this.width = 15.4;
				
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 0.0;
		this.oldDH = new double[2];
		this.dDH = new double[2];
	}
	
	/*
	 * Calculates displacement and heading as title suggests
	 */
	private void getDisplacementAndHeading(double[] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();

		data[0] = (leftTacho *leftRadius + rightTacho * rightRadius) * Math.PI / 360.0;
		data[1] = (rightTacho * rightRadius - leftTacho * leftRadius) / width;
	}
	
	/*
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
				Thread.sleep(DEFAULT_TIMEOUT_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// return X value
		public double getX() {
			synchronized (this) {
				return x;
			}
		}

		// return Y value
		public double getY() {
			synchronized (this) {
				return y;
			}
		}

		// return theta value
		public double getTheta() {
			synchronized (this) {
				return theta;
			}
		}

		// set x,y,theta
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

		// return x,y,theta
		public void getPosition(double[] position) {
			synchronized (this) {
				position[0] = x;
				position[1] = y;
				position[2] = theta;
			}
		}

		public double[] getPosition() {
			synchronized (this) {
				return new double[] { x, y, theta };
			}
		}
		
		// accessors to motors
		public EV3LargeRegulatedMotor [] getMotors() {
			return new EV3LargeRegulatedMotor[] {this.leftMotor, this.rightMotor};
		}
		public EV3LargeRegulatedMotor getLeftMotor() {
			return this.leftMotor;
		}
		public EV3LargeRegulatedMotor getRightMotor() {
			return this.rightMotor;
		}

		// static 'helper' methods
		public static double fixDegAngle(double angle) {
			if (angle < 0.0)
				angle = 360.0 + (angle % 360.0);

			return angle % 360.0;
		}

		public static double minimumAngleFromTo(double a, double b) {
			double d = fixDegAngle(b - a);

			if (d < 180.0)
				return d;
			else
				return d - 360.0;
		}

		public double getLeftRadius() {
			return leftRadius;
		}

		public double getWidth() {
			return width;
		}
}
