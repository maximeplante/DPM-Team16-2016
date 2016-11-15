package dpmCompetition;

import java.lang.reflect.Field;

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

class Zone extends Stringer {
	Coordinate lowerLeft;
	Coordinate upperRight;
	
	Zone() {
		lowerLeft = new Coordinate();
		upperRight = new Coordinate();
	}
}

class Coordinate extends Stringer {
	int x, y;
}

class Stringer {
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
}
