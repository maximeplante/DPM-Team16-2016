package dpmCompetition;

public class CompetitionData extends Stringer {

	int builderTeamNumber;
	int builderStaringCorner;
	
	int collectorTeamNumber;
	int collectorStartingCorner;
	
	Zone greenZone;
	Zone redZone;
	
	public CompetitionData() {
		greenZone = new Zone();
		redZone = new Zone();
	}
	
}
