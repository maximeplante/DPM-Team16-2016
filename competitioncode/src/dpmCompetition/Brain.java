package dpmCompetition;

public class Brain extends Thread {
	
	private Localizer localizer;
	
	Brain(Localizer localizer) {
		this.localizer = localizer;
	}
	
	public void run() {
		
		localizer.localize();
		
	}
	
}
