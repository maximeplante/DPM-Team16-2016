package dpmCompetition;

abstract public class Helper {
	/**
	 * Helper method that hides the complexity of Thread.sleep() and simplifies the code.
	 * 
	 * @param time the sleep delay in milliseconds
	 */
	static public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
