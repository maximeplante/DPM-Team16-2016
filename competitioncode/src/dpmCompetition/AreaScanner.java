package dpmCompetition;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Sound;

// TODO: A lot of values in the code of this class could be turned into constants.

public class AreaScanner {
	
	private Navigation navigation;
	private UsPoller usPoller;
	private Odometer odometer;
	
	private static final int CLOSE_OBJECT_DISTANCE = 40;

	public AreaScanner(Navigation navigation, UsPoller usPoller, Odometer odometer) {
		
		this.navigation = navigation;
		this.usPoller = usPoller;
		this.odometer = odometer;
		
	}
	
	public Coordinate[] findCloseObjects() {
		
		List<Integer> distances = new ArrayList<Integer>();
		List<Integer> angles = new ArrayList<Integer>();
		navigation.turn(360, true);
		while(navigation.isMoving()) {
			distances.add((int) usPoller.getFilteredData());
			angles.add((int) odometer.getTheta());
			
			sleep(100);
		}
		
		int startObjectIndex = 0;
		int endObjectIndex = 0;
		
		List<Coordinate> points = new ArrayList<Coordinate>();
		
		boolean first = true;
		
		while (true) {
			
			startObjectIndex = findNextObjectIndex(endObjectIndex + 1, distances);
			if (startObjectIndex == -1) {
				break;
			}
			
			Sound.beep();
			
			sleep(500);
			
			endObjectIndex = findEndObjectIndex(startObjectIndex, distances);
			
			if (endObjectIndex - startObjectIndex < 3) {
				continue;
			}
			
			int middleObjectIndex = (endObjectIndex - startObjectIndex)/2 + startObjectIndex;
			
			if (first) {
				Display.print(startObjectIndex, 3);
				Display.print(middleObjectIndex, 4);
				Display.print(endObjectIndex, 5);
				first = false;
			}
			
			Coordinate point = pointPosition(angles.get(middleObjectIndex), distances.get(middleObjectIndex));
			
			/*if (point.x < 0 || point.x > 210 || point.y < 0 || point.y > 210) {
				continue;
			}*/
			
			points.add(point);
			
		}
		
		Coordinate[] pointsArray = new Coordinate[points.size()];
		points.toArray(pointsArray);
		
		return pointsArray;
	}
	
	private int findEndObjectIndex(int objectOffset, List<Integer> distances) {
		
		int filterCounter = 0;
		
		for (int i = objectOffset + 3; i < distances.size(); i++) {
			for (int j = 0; j < 3; j++) {
				if (Math.abs(distances.get(i) - distances.get(i-j-1)) > 15) {
					filterCounter++;
				}
			}
			if (filterCounter == 3) {
				return i-3;
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
		coord.x = (int) (odometer.getX() + (distance / Math.cos(Math.toRadians(angle))));
		coord.y = (int) (odometer.getY() + (distance / Math.sin(Math.toRadians(angle))));
		return coord;
		
	}
	
	private int averageObjectDistance(int start, int end, List<Integer> distances) {
		
		int sum = 0;
		for (int i = start; i < end; i++) {
			sum += distances.get(i);
		}
		return sum / (end - start);
		
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
