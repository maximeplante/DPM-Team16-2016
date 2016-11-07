package tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class USTest {
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final Port usPort = LocalEV3.get().getPort("S1");	
	private static double radius = 2.15;
	private static double tileLength = 30.48;
	
	public static void main(String[] args) throws InterruptedException  ,FileNotFoundException, UnsupportedEncodingException{
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);
		PrintWriter writer = null;
		writer = new PrintWriter ("data.csv", "UTF-8");
//		EV3UltrasonicSensor sensor = new EV3UltrasonicSensor (SensorPort.S1);
		
		@SuppressWarnings("resource")
		EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(usPort);
		SampleProvider usValue = usSensor.getMode("Distance");			
		float[] usData = new float[usValue.sampleSize()];
		
		
		UltrasonicPoller usPoller =   new UltrasonicPoller(usValue) ;
		usPoller.start() ;
		int tachoCount = leftMotor.getTachoCount();
		int tachoRot = convertDistance(radius, 6*tileLength);
		leftMotor.rotate(tachoRot, true);
		rightMotor.rotate(tachoRot, true);
		try{
			for (int i=0;i<450;i++){
				int distance = usPoller.getDistance();
//				int distTo = (int) (3*tileLength - convertRot(radius, (leftMotor.getTachoCount() - tachoCount)));
//				System.out.print(String.format("%d: %d%n", System.currentTimeMillis(), distTo));
				System.out.print(String.format("%d: %d%n", System.currentTimeMillis(), distance));
				
//				writer.write(distTo + "\t");
//				System.out.println(distTo);
				writer.write(distance + "\n");
				Thread.sleep(100);
			}
		}finally{
				writer.close();
				usSensor.close();
			}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	private static int convertRot(double radius, double degRot){
		return (int) (degRot*Math.PI*radius/180);
	}

}
