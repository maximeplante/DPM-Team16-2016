package dpmCompetition;

import java.util.ArrayList;
import java.util.Collections;

import lejos.robotics.SampleProvider;

/**
 * 
 * Provides access to the readings from the US sensor and 
 * gets the filtered reading.
 *
 */
public class UsPoller extends Thread {
	
	/** Reference to the ultrasonic sensor sample provider */
	private SampleProvider sampleProvider;
	
	/** The array of the previous readings made by the ultrasonic sensor */
	private ArrayList<Float> usPreviousData;
	/** The number of previous readings to keep in the usPreviousData array */
	private final int usPreviousDataSize = 6;
	
	/** The array needed by the sample provider */
	private float[] usData;
	
	/** The most recent median of the ultrasonic sensor readings (result of the median filter) */
	private float medianDistance;
	
	
	/**
	 * Constructor
	 * 
	 * @param sampleProvider
	 */
	UsPoller(SampleProvider sampleProvider) {
		this.sampleProvider = sampleProvider;
		this.usData = new float[sampleProvider.sampleSize()];
		usPreviousData = new ArrayList<Float>();
		
		// Do an initial data fetch to be sure that the usPreviousData array contains data
		sampleProvider.fetchSample(usData, 0);
		float distance = (float)(usData[0] * 100.0);
		usPreviousData.add(distance);
		
		// Apply the filter on the single value (it could be changed to medianDistance = usPreviousData.get(0), but I want to keep the code consistent)
		medianDistance = median(usPreviousData);
	}
	
	/**
	 * Starts polling to get the data from the sensor. The reading will 
	 * be stored in an array, then to be filtered by a median filter.
	 */
	public void run() {
		
		// Polling loop
		while (true) {
			// Fetch the latest reading an multiply it by 100 to convert it to ~cm.
			sampleProvider.fetchSample(usData, 0);
			float distance = (float)(usData[0] * 100.0);
			
			// Add the latest reading to the usPreviousData array
			usPreviousData.add(distance);
			// Remove the oldest readings from usPreviousData if there is more than [usPreviousDataSize] readings.
			if (usPreviousData.size() > usPreviousDataSize) {
				usPreviousData.remove(0);
			}
			
			// Execute the median filter on the previous readings (including the latest reading)
			medianDistance = median(usPreviousData);
			
			// Sleep for 70 ms because the ultrasonic sensor needs a sleep time of at 70 ms between the readings.
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/** 
	 * 
	 * @return the most recent filtered reading.
	 */
	public float getFilteredData() {
		return medianDistance;
	}
	
	public float getRawData() {
		return usPreviousData.get(usPreviousData.size() - 1);
	}
	
	
	/**
	 * Median filter
	 * Calculates the median of the whole data array
	 * 
	 * @param data an array that stores the readings
	 * @return the median of the sorted array
	 */
	private static float median(ArrayList<Float> data) {
		// Create a copy of the array to prevent race condition when sorting (even though it is unlikely to happen)
		ArrayList<Float> sortedData = new ArrayList<Float>(data);
		// Sort the array
		Collections.sort(sortedData);
		// Return the middle element
		// If the array contains one element, the single element is the center element
		if (sortedData.size() == 1) {
			return sortedData.get(0);
		}
		int center = sortedData.size()/2;
		// If the data array contains a uneven number of elements, return the center element.
		if (sortedData.size()%2 == 1) {
			return sortedData.get(center);
		}
		// Or do the average between the two closest elements to the center.
		return (float)((sortedData.get(center - 1) + sortedData.get(center)) / 2.0);
	}
}
