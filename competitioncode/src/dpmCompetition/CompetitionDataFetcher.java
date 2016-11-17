package dpmCompetition;

import java.io.IOException;
import java.util.HashMap;

import dpmCompetition.wifi.WifiConnection;
/**
 * Waits for the Wifi connection and receives the competition data
 *
 */
public abstract class CompetitionDataFetcher {

	public static CompetitionData fetch() {
		
		WifiConnection conn = null;
		try {
			System.out.println("Connecting...");
			conn = new WifiConnection(Main.SERVER_IP, Main.TEAM_NUMBER, true);
		} catch (IOException e) {
			System.out.println("Connection failed");
		}
		
		if (conn == null) {
			System.out.println("Connection was cancelled");
		}
		
		HashMap<String, Integer> data = conn.StartData;
		
		if (data == null) {
			System.out.println("No data received");
		}
		
		CompetitionData competitionData = new CompetitionData();
		
		competitionData.builderTeamNumber = data.get("BTN");
		competitionData.collectorTeamNumber = data.get("CTN");
		competitionData.builderStaringCorner = data.get("BSC");
		competitionData.collectorStartingCorner = data.get("CSC");
		
		competitionData.greenZone.lowerLeft.x = data.get("LGZx");
		competitionData.greenZone.lowerLeft.y = data.get("LGZy");
		competitionData.greenZone.upperRight.x = data.get("UGZx");
		competitionData.greenZone.upperRight.y = data.get("UGZy");
		
		competitionData.redZone.lowerLeft.x = data.get("LRZx");
		competitionData.redZone.lowerLeft.y = data.get("LRZy");
		competitionData.redZone.upperRight.x = data.get("URZx");
		competitionData.redZone.upperRight.y = data.get("URZy");
		
		return competitionData;
	}
	
}
