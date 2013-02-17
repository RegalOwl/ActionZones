package regalowl.actionzones;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KillMobs {
	
	public void killMobs(ActionZones az, String zonename) {
		
		Zone z = az.getZone();
		
		if (zonename == null) {
			return;
		}

		FileConfiguration zones = az.getYaml().getZones();
		//Returns if there is no attached zone.
		String attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support mob killing.", "actionzones.admin");
			return;
		}
		World w = Bukkit.getWorld(zones.getString(attachedzone + ".world"));
		List<LivingEntity> entities = w.getLivingEntities();
		int counter = 0;
		int size = entities.size();
		while (counter < size) {
			LivingEntity entity = entities.get(counter);
			Location loc = entity.getLocation();
			ArrayList<Integer> zonesmob = z.inZonesOther(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), w);
			//Bukkit.broadcastMessage("zonesmob: " + zonesmob);
			int c = 0;
			while (c < zonesmob.size()) {
				if (z.getZone(zonesmob.get(c)).equalsIgnoreCase(attachedzone)) {
					if (!(entity instanceof Player)) {
						entity.setHealth(0);
					}
					break;
				}
				c++;
			}
			counter++;
		}

		
		
	}

}
