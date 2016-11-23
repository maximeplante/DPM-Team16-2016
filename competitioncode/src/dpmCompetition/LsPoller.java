package dpmCompetition;

import java.util.ArrayList;

import lejos.robotics.SampleProvider;

public class LsPoller extends Thread {
	private static final long POLLING_PERIOD = 10;
	
	/** Reference to the light sensor's sample provider */
	private SampleProvider lsSampleProvider;
	
	/** The buffer for the color sensor data */
	public float[] lsData;
	
	/** The number of color sensor's readings we should keep in lastCSReadings */
	private final int lastCSReadingsSize = 15;
	
	/** The array containing the previous light sensor readings.
	    Public for the OdometryDisplay */
	public ArrayList<Float> lastCSReadings;
	
	/** The boolean variable that represents robot's state of if it is seeing a black line */
	private boolean seeBlackLine;

	/** The boolean variable that represents whether the robot sees a blue block */
	private boolean seeBlueBlock;
	
	/** The boolean variable that represents whether the robot sees a wooden block */
	private boolean seeWoodBlock;
	
	/** The double for blue/red value taken from RGB mode*/
	private double BRratio;
	
	/** The double representing tested blue styrofoam blue/red ratio*/
	private double blueRatio = 1.4;
	
	/** The double representing tested wooden block blue/red ratio*/
	private double woodRatio = 0.4;
	
	/** The double representing error margin for blue/red ratio*/
	private double errMargin = 0.2;
	
	
	/**
	 * Constructor
	 * 
	 * @param lsSampleProvider
	 */
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
		
		seeBlueBlock = false;
		
		seeWoodBlock = false;
		
	}

	
	/**
	 * Starts polling to get the data from the light sensor. 
	 */
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
			
			BRratio = lsData[2]/lsData[0];
			
			if (((blueRatio - errMargin) < BRratio) && ((blueRatio + errMargin) > BRratio)) {
				seeBlueBlock = true;
			}
			else if (((woodRatio - errMargin) < BRratio) && ((woodRatio + errMargin) > BRratio)) {
				seeWoodBlock = true;
			}
			else {
				seeBlueBlock = false;
				seeWoodBlock = false;
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
	

	/**
	 * 
	 * @return the state of whether the robot is on a black line
	 */
	public boolean isSeeingBlackLine() {
		return seeBlackLine;
	}
	
	/**
	 * 
	 * @return the state of whether the robot sees a blue block
	 */
	public boolean isSeeingBlueBlock() {
		return seeBlueBlock;
	}
	
	/**
	 * 
	 * @return the state of whether the robot sees a wooden block
	 */
	public boolean isSeeingWoodenBlock() {
		return seeWoodBlock;
	}
}
