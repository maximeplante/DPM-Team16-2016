package dpmCompetition;

import lejos.hardware.lcd.TextLCD;

public class Display extends Thread {
	
	private static final long DISPLAY_PERIOD = 250;
	private TextLCD lcd;
	
	static private String[] lines = {"", "", "", "", ""};
	
	Display(TextLCD lcd) {
		this.lcd = lcd;
	}
	
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
	
	public void print(String msg, int line) throws Exception {
		if (line >= lines.length) {
			throw new Exception("Invalid line number");
		}
		msg = lines[line];
	}
	
	public void print(double msg, int line) throws Exception {
		print(String.valueOf(msg), line);
	}
	
}
