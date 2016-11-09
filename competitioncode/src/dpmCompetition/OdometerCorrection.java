package dpmCompetition;

// TODO don't do correction when the robot is not traveling in a straight line 
public class OdometerCorrection extends Thread {

	private Odometer odometer;
	private LeftLsPoller leftLsPoller;
	private RightLsPoller rightLsPoller;
	private Navigation navigation;
	private MotorsController motor;
	private int cornerRadius = 5; // margin of the corner
	private int squareLength = 30;
	double radius = 1.4;// get the value from other class later (radius of the wheels)
	
	// to be determine 
	private double sensorRadius; // distance between two sensor

	public OdometerCorrection(Odometer odometer, LeftLsPoller leftLsPoller, RightLsPoller rightLsPoller, Navigation navigation, MotorsController motor) {
		this.odometer = odometer;
		this.leftLsPoller = leftLsPoller;
		this.rightLsPoller = rightLsPoller;
		this.navigation = navigation;
		this.motor = motor;
	}

	public void run() {
		double x1, x2, y1, y2;
		double distanceTravel;
		double theta;
		double lasttTachoRight, currentTachoRight;
		int direction = 1;
		double[] position = new double[3]; 
		boolean[] update = new boolean[3];
		while (true){
			// first sensor sees the line
			while (!leftLsPoller.isSeeingBlackLine() || !rightLsPoller.isSeeingBlackLine() || isAtCorner()) {
				sleep(50);
			}
			if (leftLsPoller.isSeeingBlackLine())
				direction = 1;
			else 
				direction = -1;
			lasttTachoRight = motor.getRightWheelMotor().getTachoCount();
			x1 = odometer.getX();
			y1 = odometer.getY();
			// the other sensor sees the line
			while (!leftLsPoller.isSeeingBlackLine() || !rightLsPoller.isSeeingBlackLine()) {
				sleep(50);
			}
			currentTachoRight = lasttTachoRight = motor.getRightWheelMotor().getTachoCount();
			x2 = odometer.getX();
			y2 = odometer.getY();
			//if the robot is crossing a horizontal line, only correct y value and theta accordingly 
			if (isCrossingX((x1+x2)/2.0, (y1+y2)/2.0)){
				distanceTravel = ((currentTachoRight - lasttTachoRight)/180)*Math.PI*radius*direction;
				theta = Math.toDegrees(Math.atan(distanceTravel/sensorRadius));
				// traveling in positive y direction
				if (odometer.getTheta()>= 10 && odometer.getTheta() <= 170){
					position[2] = 90.0-theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the sensor needs to be added later
					position[1]= odometer.getY()/30 * 30.0 + (Math.cos(Math.toRadians(theta))* distanceTravel/2.0); // new y value
					update[1] = true; // will correct y value
					update[0] = false; // won't correct x value
					odometer.setPosition(position, update);
				} else if (odometer.getTheta()>=190 && odometer.getTheta()<= 350){ // traveling in negative y direction 
					//calculate the new position values
					position[2] = 270.0-theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the sensor needs to be added later
					position[1]= odometer.getY()/30 * 30.0 - (Math.cos(Math.toRadians(theta))* distanceTravel/2.0); // new y value
					update[1] = true; // will correct y value
					update[0] = false; // won't correct x value
					odometer.setPosition(position, update); //update new position
				}
			}else{ // crossing a vertical line - correct x rather than y
				distanceTravel = ((currentTachoRight - lasttTachoRight)/180)*Math.PI*radius*direction*(-1);
				theta = Math.toDegrees(Math.atan(distanceTravel/sensorRadius));
				if (odometer.getTheta()>= 280 && odometer.getTheta() <= 80){ // traveling in positive x direction
					position[2] = theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the sensor needs to be added later
					position[0]= odometer.getX()/30 * 30.0 + (Math.cos(Math.toRadians(theta))* distanceTravel/2.0); // new x value
					update[0] = true; // will correct x value
					update[1] = false; // won't correct y value
					odometer.setPosition(position, update); //update position
				} else if (odometer.getTheta()>=100 && odometer.getTheta()<= 260){ // traveling in negative x direction
					position[2] = 180.0+theta; // new theta
					update[2] = true; // will correct theta
					// the distance between the center of the wheel-base and the sensor needs to be added later
					position[0]= odometer.getY()/30 * 30.0 - (Math.cos(Math.toRadians(theta))* distanceTravel/2.0); // new x value
					update[0] = true; // will correct x value
					update[1] = false; // won't correct y value
					odometer.setPosition(position, update); // update position
				}
			}
		}
	}

	// check if the robot is at the corner of the square
	private boolean isAtCorner() {
		if (cornerRadius < (squareLength - (odometer.getX() % squareLength))
				&& (squareLength - (odometer.getX() % squareLength)) < (squareLength - cornerRadius)
				&& cornerRadius < (squareLength - (odometer.getY() % squareLength))
				&& (squareLength - (odometer.getY() % squareLength)) < (squareLength - cornerRadius)) {
			return false;
		}
		return true;
	}

	// determine whether the robot is crossing an x axis (horizontal) or a y
	// (vertical).
	private boolean isCrossingX(double x, double y) {
		if (Math.abs(x % squareLength) >= (y % squareLength)) {
			return true;
		}
		return false;
	}
}
