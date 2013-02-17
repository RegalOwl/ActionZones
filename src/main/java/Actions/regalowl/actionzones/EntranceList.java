package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EntranceList {

	
	public void logEntry(ActionZones az, String zone, Player p) {
		String name = p.getName();
		if (zone == null) {
			return;
		}
		FileConfiguration elist = az.getYaml().getEntranceList();
		
		String testzone = elist.getString(zone);
		if (testzone == null) {
			elist.set(zone, name + ",");
			az.getYaml().saveEntranceList();
		} else {
			SerializeArrayList sal = new SerializeArrayList();
			ArrayList<String> pnames = sal.stringToArray(elist.getString(zone));
			if (!pnames.contains(name)) {
				pnames.add(name);
				elist.set(zone, sal.arrayToString(pnames));
				az.getYaml().saveEntranceList();
			}
		}
	}
}
