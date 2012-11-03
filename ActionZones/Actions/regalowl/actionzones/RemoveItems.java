package regalowl.actionzones;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class RemoveItems {

	
	public void removeItems(ActionZones az, String zonename) {
		
		Zone z = az.getZone();
		
		if (zonename == null) {
			return;
		}

		FileConfiguration zones = az.getYaml().getZones();
		//Returns if there is no attached zone.
		String attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support item removal.", "actionzones.admin");
			return;
		}
		World w = Bukkit.getWorld(zones.getString(attachedzone + ".world"));
		List<Entity> entities = w.getEntities();
		int counter = 0;
		int size = entities.size();
		while (counter < size) {
			Entity entity = entities.get(counter);
			Location loc = entity.getLocation();
			ArrayList<Integer> zonesentity = z.inZonesOther(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), w);
			//Bukkit.broadcastMessage("zonesmob: " + zonesmob);
			int c = 0;
			while (c < zonesentity.size()) {
				if (z.getZone(zonesentity.get(c)).equalsIgnoreCase(attachedzone)) {
					if (entity instanceof Item) {
						entity.remove();
					}
					break;
				}
				c++;
			}
			counter++;
		}

		
		
	}
	
	
}
