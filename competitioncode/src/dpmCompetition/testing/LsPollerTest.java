package dpmCompetition.testing;

import dpmCompetition.*;

/** 
 * For testing lsPoller
 *
 */
public class LsPollerTest extends Thread{
	/** Reference to right lsPoller */
	private final LsPoller rightLsPoller;
	/** Reference to left lsPoller */
	private final LsPoller leftLsPoller;
	
	/**
	 * Constructor
	 * 
	 * @param rightLsPoller
	 * @param leftLsPoller
	 */
	public LsPollerTest(LsPoller rightLsPoller, LsPoller leftLsPoller){
		this.rightLsPoller = rightLsPoller;
		this.leftLsPoller = leftLsPoller;
	}
	
	public void run(){
		while(true){
			if (rightLsPoller.isSeeingBlackLine()){
				Display.print("right", 3);
			}
			
			if (leftLsPoller.isSeeingBlackLine()){
				Display.print("left", 4);
			}
			
			Helper.sleep(50);
		}
	}
}
