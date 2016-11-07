package dpmCompetition;

import java.util.ArrayList;

import lejos.robotics.SampleProvider;

public class LsPoller extends Thread {
	private static final long POLLING_PERIOD = 10;
	
	// The light sensor
	private SampleProvider lsSampleProvider;
	
	// The buffet for the color sensor data
	public float[] lsData;
	
	// How many color sensor readings we should keep in lastCSReadings
	private final int lastCSReadingsSize = 15;
	
	// The array containing the previous light sensor readings.
	// Public for the OdometryDisplay
	public ArrayList<Float> lastCSReadings;
	
	private boolean seeBlackLine;

	// constructor
	public LsPoller(SampleProvider lsSampleProvider) {
		// Setting up the light sensor
		this.lsSampleProvider = lsSampleProvider;
		lsData = new float[lsSampleProvider.sampleSize()];
		
		// Filling some default values before the correction starts.
		lsSampleProvider.fetchSample(lsData, 0);
		lastCSReadings = new ArrayList<Float>(lastCSReadingsSize);
		lastCSReadings.add(lsData[0]);
		
		// Assume we don't see a black line initially
		seeBlackLine = false;
		
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		
		// Polling loop
		while (true) {
			correctionStart = System.currentTimeMillis();
			
			// Read the current reading of the light sensor
			lsSampleProvider.fetchSample(lsData, 0);
			float currentLSReading = lsData[0];
			
			// Compared the current reading with the oldest reading in lastCSReadings to see if there is a major color change (a black line)
			if (currentLSReading < lastCSReadings.get(0) - 0.10) {
				seeBlackLine = true;
			} else {
				seeBlackLine = false;
			}
			
			// Add the latest light sensor reading to the table of the previous light sensor readings
			lastCSReadings.add(currentLSReading);
			// Remove the oldest light sensor reading from the table if we have more than [lastLSReadingsSize] stored.
			if (lastCSReadings.size() > lastCSReadingsSize) {
				lastCSReadings.remove(0);
			}

			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < POLLING_PERIOD) {
				try {
					Thread.sleep(POLLING_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}
	
	// Getter
	public boolean isSeeingBlackLine() {
		return seeBlackLine;
	}
}
