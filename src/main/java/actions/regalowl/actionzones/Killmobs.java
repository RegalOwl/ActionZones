package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Killmobs extends Action {
	
	public void runAction() {
		if (zone == null) {
			return;
		}
		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		String attachedzone = zones.getString(zone.getName() + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a zone attached in order to support mob killing.", "actionzones.admin");
			return;
		}
		World w = Bukkit.getWorld(zones.getString(attachedzone + ".world"));
		for (LivingEntity entity: w.getLivingEntities()) {
			if (zone.inZone(entity.getLocation()) && !(entity instanceof Player)) {
				entity.setHealth(0);
			}
		}
	}

}
