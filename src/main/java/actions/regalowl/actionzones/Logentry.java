package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

public class Logentry extends Action{

	
	public void runAction() {
		String name = player.getName();
		if (zone == null) {
			return;
		}
		FileConfiguration elist = az.getYamlHandler().gFC("entrancelist");
		
		String testzone = elist.getString(zone.getName());
		if (testzone == null) {
			elist.set(zone.getName(), name + ",");
			az.getYamlHandler().saveYaml("entrancelist");
		} else {
			ArrayList<String> pnames = az.getCommonFunctions().explode(elist.getString(zone.getName()), ",");
			if (!pnames.contains(name)) {
				pnames.add(name);
				elist.set(zone.getName(), az.getCommonFunctions().implode(pnames, ","));
				az.getYamlHandler().saveYaml("entrancelist");
			}
		}
	}
}
