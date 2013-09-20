package regalowl.actionzones;

import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class Removeitems extends Action {

	
	public void runAction() {
		if (zone.getName() == null) {
			return;
		}

		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		Zone attachedzone = az.getZoneHandler().getZone(zone.getString("zone"));
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a zone attached in order to support item removal.", "actionzones.admin");
			return;
		}
		
		World w = Bukkit.getWorld(zones.getString(attachedzone.getName() + ".world"));
		List<Entity> entities = w.getEntities();
		for (Entity entity:entities) {
			Location loc = entity.getLocation();
			if (attachedzone.inZone(loc)) {
				if (entity instanceof Item) {
					entity.remove();
				}
			}
		}

		
		
	}
	
	
}
