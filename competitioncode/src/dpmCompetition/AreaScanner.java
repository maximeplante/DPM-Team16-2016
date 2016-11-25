package dpmCompetition;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Sound;

// TODO: A lot of values in the code of this class could be turned into constants.

public class AreaScanner {
	
	/** Reference to the navigation object */
	private Navigation navigation;
	/** Reference to the ultrasonic poller */
	private UsPoller usPoller;
	/** Reference to the odometer */
	private Odometer odometer;
	
	/** The maximal distance (cm) at which a object is to be considered */
	private static final int CLOSE_OBJECT_DISTANCE = 70;
	/** The speed of the robot's wheel motors when it is turning on itself for the scanning */
	private static final int SCANNING_SPEED = 50;
	/** The threshold distance difference between two us reads to determine the boundaries of an object */
	private static final int OBJECT_END_THRESHOLD_DISTANCE = 15;
	/** The upper y position of the upper wall for wall ignoring */
	private static final int WALL_UPPER_Y = 210;
	/** The upper y position of the lower wall for wall ignoring */
	private static final int WALL_LOWER_Y = 0;
	/** The upper x position of the upper wall for wall ignoring */
	private static final int WALL_UPPER_X = 210;
	/** The upper x position of the lower wall for wall ignoring */
	private static final int WALL_LOWER_X = 0;
	/** If an object is detected in less than MINIMAL_OBJECT_SIZE consecutive reads, it is considered nothing but bad readings */
	private static final int MINIMAL_OBJECT_SIZE = 5;
	/** The delay (ms) between the US samples during the scanning */
	private static final int SCANNING_SAMPLE_DELAY = 100;
	/** Size of the filter used to detect the end of an object */
	private static final int FILTER_SIZE = 3;
	
	/**
	 * Constructor
	 * 
	 * @param navigation
	 * @param usPoller
	 * @param odometer
	 */
	public AreaScanner(Navigation navigation, UsPoller usPoller, Odometer odometer) {
		
		this.navigation = navigation;
		this.usPoller = usPoller;
		this.odometer = odometer;
		
	}
	
	/**
	 * Returns the position and the size of the objects around the robot.
	 * 
	 * @return a list of objects detected.
	 */
	public Block[] findCloseObjects() {
		
		// Holds the distance readings made by the US during the scan
		List<Integer> distances = new ArrayList<Integer>();
		// Holds the robot's angle position during each us read
		List<Integer> angles = new ArrayList<Integer>();
		
		// Make the robot turn on itself (without waiting for it to finish)
		navigation.turn(90, true, SCANNING_SPEED);
		
		// Sample the US readings while the robot is moving
		while(navigation.isMoving()) {
			
			// Add the reading and include the offset of the us compared to the robot's rotation center
			distances.add((int) (usPoller.getFilteredData() + Main.UPPER_US_OFFSET));
			// Records the angle of each reading
			angles.add((int) odometer.getTheta());
			
			sleep(SCANNING_SAMPLE_DELAY);
			
		}
		
		for (int i = 0; i < distances.size(); i++) {
			System.out.println(Math.round(angles.get(i)) + ":" + distances.get(i));
		}
		
		int startObjectIndex = 0;
		int endObjectIndex = 0;
		
		// Using an array list because of it's flexible size
		List<Block> blocks = new ArrayList<Block>();
		
		while (true) {
			
			// Find the sample at which a new object is detected
			startObjectIndex = findNextObjectIndex(endObjectIndex + 1, distances);
			if (startObjectIndex == -1) {
				// Break out of the loop if there is no new object
				break;
			}
			
			endObjectIndex = findEndObjectIndex(startObjectIndex, distances);
			
			/* Ignore this object if it is too small
			 * It's probably just a few bad readings.
			 */
			if (endObjectIndex - startObjectIndex < MINIMAL_OBJECT_SIZE) {
				continue;
			}
			
			// Get the center of this object
			int middleObjectIndex = (endObjectIndex - startObjectIndex)/2 + startObjectIndex;
			
			/* Get the x and y position of the object center, left and right side (from the robot's perspective)
			 * on the competition board.
			 */
			Coordinate center = pointPosition(angles.get(middleObjectIndex), distances.get(middleObjectIndex));
			Coordinate left = pointPosition(angles.get(endObjectIndex), distances.get(endObjectIndex));
			Coordinate right = pointPosition(angles.get(startObjectIndex), distances.get(startObjectIndex));
			
			// Ignore any wall
			if (center.x < WALL_LOWER_X || center.x > WALL_UPPER_X || center.y < WALL_LOWER_Y || center.y > WALL_UPPER_Y) {
				continue;
			}
			
			Sound.beep();
			sleep(500);
			
			// Create a block object with the object's information
			Block block = new Block();
			block.center = center;
			block.left = left;
			block.right = right;
			
			// Add the new object to the list of objects found
			blocks.add(block);
			
		}
		
		// Converting the array list back to an array.
		Block[] blocksArray = new Block[blocks.size()];
		blocks.toArray(blocksArray);
		
		return blocksArray;
	}
	
	/**
	 * Find the index of the distance sample at which an object is detected
	 * 
	 * @param offset index offset at which start the search for the object
	 * @param distances the list of distance samples
	 * @return the index of the sample at which the object is first detected
	 */
	private int findNextObjectIndex(int offset, List<Integer> distances) {
		
		for(int i = offset; i < distances.size(); i++) {
			if (distances.get(i) < CLOSE_OBJECT_DISTANCE) {
				return i;
			}
		}
		
		return -1;
		
	}
	
	/**
	 * Find the distance sample at which the object is ending.
	 * 
	 * @param objectOffset the index offset at which the object starts
	 * @param distances the list of distance samples
	 * @return the index of the distance sample where the object ends
	 */
	private int findEndObjectIndex(int objectOffset, List<Integer> distances) {
		
		int filterCounter = 0;
		
		// Search the distance samples for the ending of the object
		for (int i = objectOffset; i < distances.size() - FILTER_SIZE; i++) {
			for (int j = 0; j < FILTER_SIZE; j++) {
				if (Math.abs(distances.get(i) - distances.get(i+j)) > 15) {
					filterCounter++;
				}
			}
			if (filterCounter == FILTER_SIZE) {
				return i;
			}
			filterCounter = 0;
		}
		
		// If the end is not detected, this means that the object ends past the scanning. Gives the last sample.
		return distances.size() - 1;
	}
	
	/**
	 * Gets the X and Y position of a point on the competition ground based on its angle and distance from the robot
	 * 
	 * @param angle the angle position of the point from the robot (degrees)
	 * @param distance the distance of the point from the robot (cm)
	 * @return the position of the point on the competition ground
	 */
	private Coordinate pointPosition(int angle, int distance) {
		
		Coordinate coord = new Coordinate();
		coord.x = odometer.getX() + (distance * Math.cos(Math.toRadians(angle)));
		coord.y = odometer.getY() + (distance * Math.sin(Math.toRadians(angle)));
		return coord;
		
	}
	
	/**
	 * Helper method that hides the complexity of Thread.sleep() and simplifies the code.
	 * 
	 * @param time the sleep delay in milliseconds
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
