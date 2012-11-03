package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class SetCondition {
	
	public void Set(ActionZones az, String zonename) {


		FileConfiguration zones = az.getYaml().getZones();
		String condition = zones.getString(zonename + ".condition");
		if (condition == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a condition attached in order to support condition setting.", "actionzones.admin");
			return;
		}
		
		World w = Bukkit.getServer().getWorld(zones.getString(zonename + ".world"));
		if (condition.equalsIgnoreCase("day")) {
			w.setTime(0);
		} else if (condition.equalsIgnoreCase("night")) {
			w.setTime(14000);
		} else if (condition.equalsIgnoreCase("dusk")) {
			w.setTime(12000);
		} else if (condition.equalsIgnoreCase("dawn")) {
			w.setTime(23000);
		} else if (condition.equalsIgnoreCase("storm")) {
			w.setStorm(true);
		} else if (condition.equalsIgnoreCase("sun")) {
			w.setStorm(false);
		}
		
		
	}
}

