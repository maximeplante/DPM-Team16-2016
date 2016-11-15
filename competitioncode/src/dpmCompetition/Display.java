package dpmCompetition;

import lejos.hardware.lcd.TextLCD;

public class Display extends Thread {
	
	/** The delay in ms between the screen refreshes */
	private static final long DISPLAY_PERIOD = 250;
	
	/** Reference to the EV3's LCD screen */
	private TextLCD lcd;
	
	/** The lines do display during the next screen refresh */
	static private String[] lines = {"", "", "", "", ""};
	
	/**
	 * Constructor
	 * 
	 * @param lcd reference to EV3's LCD screen
	 */
	Display(TextLCD lcd) {
		this.lcd = lcd;
	}
	
	/**
	 * The thread code.
	 */
	public void run() {
		lcd.clear();
		
		while (true) {
			
			for (int i = 0; i < lines.length; i++) {
				lcd.drawString("                ", 0, i);
				lcd.drawString(lines[i], 0, i);
			}
			
			try {
				Thread.sleep(DISPLAY_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Sets a message to display on a certain lines of the display.
	 * 
	 * The message will appear during the next screen refresh. It is a static
	 * method, so it can be called from anywhere in the code.
	 * 
	 * @param msg the message to show
	 * @param line the line of the screen on which the message should be printed
	 */
	static public void print(String msg, int line){
		lines[line % lines.length] = msg;
	}
	
	/**
	 * Sets a double to display on a certain lines of the display.
	 * 
	 * The double will appear during the next screen refresh. It is a static
	 * method, so it can be called from anywhere in the code.
	 * 
	 * @param msg the double to show
	 * @param line the line of the screen on which the double should be printed
	 */
	static public void print(double msg, int line) {
		print(String.valueOf(msg), line);
	}
	
}
