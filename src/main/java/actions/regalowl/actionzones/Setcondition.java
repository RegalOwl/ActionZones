package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Setcondition extends Action {
	
	public void runAction() {


		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		String condition = zone.getString("condition");
		if (condition == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a condition attached in order to support condition setting.", "actionzones.admin");
			return;
		}
		
		World w = Bukkit.getServer().getWorld(zones.getString(zone.getName() + ".world"));
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

