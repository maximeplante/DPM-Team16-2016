package dpmCompetition.testing;

import dpmCompetition.BlockManipulator;

public class BlockCatchingTest extends Thread {
	
	BlockManipulator blockManipulator;
	
	public BlockCatchingTest(BlockManipulator blockManipulator) {
		this.blockManipulator = blockManipulator;
	}
	
	public void run() {
		
		// Catch three blocks one after the other
		for(int i = 0; i < 3; i++) {
			blockManipulator.catchBlock();
		}
		
	}
	
}
