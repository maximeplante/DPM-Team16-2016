package dpmCompetition;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Sound;

// TODO: A lot of values in the code of this class could be turned into constants.

public class AreaScanner {
	
	private Navigation navigation;
	private UsPoller usPoller;
	private Odometer odometer;
	
	private static final int CLOSE_OBJECT_DISTANCE = 70;
	private static final int SCANNING_SPEED = 50;
	private static final int FILTER_SIZE = 3;
	private static final int OBJECT_END_THRESHOLD_DISTANCE = 15;
	private static final int WALL_UPPER_Y = 210;
	private static final int WALL_LOWER_Y = 0;
	private static final int WALL_UPPER_X = 210;
	private static final int WALL_LOWER_X = 0;
	private static final int MINIMAL_OBJECT_SIZE = 3;

	public AreaScanner(Navigation navigation, UsPoller usPoller, Odometer odometer) {
		
		this.navigation = navigation;
		this.usPoller = usPoller;
		this.odometer = odometer;
		
	}
	
	public Block[] findCloseObjects() {
		
		List<Integer> distances = new ArrayList<Integer>();
		List<Integer> angles = new ArrayList<Integer>();
		navigation.turn(90, true, SCANNING_SPEED);
		
		while(navigation.isMoving()) {
			
			distances.add((int) (usPoller.getRawData() + Main.UPPER_US_OFFSET));
			angles.add((int) odometer.getTheta());
			
			sleep(100);
			
		}
		
		int startObjectIndex = 0;
		int endObjectIndex = 0;
		
		List<Block> blocks = new ArrayList<Block>();
		
		while (true) {
			
			startObjectIndex = findNextObjectIndex(endObjectIndex + 1, distances);
			if (startObjectIndex == -1) {
				break;
			}
			
			endObjectIndex = findEndObjectIndex(startObjectIndex, distances);
			
			if (endObjectIndex - startObjectIndex < MINIMAL_OBJECT_SIZE) {
				continue;
			}
			
			int middleObjectIndex = (endObjectIndex - startObjectIndex)/2 + startObjectIndex;
			
			Coordinate center = pointPosition(angles.get(middleObjectIndex), distances.get(middleObjectIndex));
			Coordinate left = pointPosition(angles.get(endObjectIndex), distances.get(endObjectIndex));
			Coordinate right = pointPosition(angles.get(startObjectIndex), distances.get(startObjectIndex));
			
			if (center.x < WALL_LOWER_X || center.x > WALL_UPPER_X || center.y < WALL_LOWER_Y || center.y > WALL_UPPER_Y) {
				continue;
			}
			
			Sound.beep();
			
			sleep(500);
			
			Block block = new Block();
			block.center = center;
			block.left = left;
			block.right = right;
			
			blocks.add(block);
			
		}
		
		Block[] blocksArray = new Block[blocks.size()];
		blocks.toArray(blocksArray);
		
		return blocksArray;
	}
	
	private int findEndObjectIndex(int objectOffset, List<Integer> distances) {
		
		int filterCounter = 0;
		
		for (int i = objectOffset + FILTER_SIZE; i < distances.size(); i++) {
			for (int j = 0; j < FILTER_SIZE; j++) {
				if (Math.abs(distances.get(i) - distances.get(i-j-1)) > OBJECT_END_THRESHOLD_DISTANCE) {
					filterCounter++;
				}
			}
			if (filterCounter > 1) {
				return i-FILTER_SIZE;
			}
			filterCounter = 0;
		}
		
		return distances.size() - 1;
	}
	
	private int findNextObjectIndex(int offset, List<Integer> distances) {
		
		for(int i = offset; i < distances.size(); i++) {
			if (distances.get(i) < CLOSE_OBJECT_DISTANCE) {
				return i;
			}
		}
		
		return -1;
		
	}
	
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
