package dpmCompetition;

/**
 * 
 * Declares competition's data
 *
 */
public class CompetitionData {
	/** Team number for builder */
	int builderTeamNumber;
	/** Starting corner for builder */
	int builderStaringCorner;
	/** Team number for collector */
	int collectorTeamNumber;
	/** Staring corner for garbage collector */
	int collectorStartingCorner;
	
	/** Green Zone */
	Zone greenZone;
	/** Red Zone */
	Zone redZone;
	
	/**
	 * Constructor 
	 */
	public CompetitionData() {
		greenZone = new Zone();
		redZone = new Zone();
	}
	
}
