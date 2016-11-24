package dpmCompetition;

/**
 * Block object that is defined by three coordinates: center, left and right coordinates.
 */
public class Block {

	public Coordinate center;
	public Coordinate left;
	public Coordinate right;
	
	/**
	 * Constructor
	 */
	Block() {
		center = new Coordinate();
		left = new Coordinate();
		right = new Coordinate();
	}
	
}
