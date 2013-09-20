package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class Activate extends Action {
	
	public void runAction() {
		
		FileConfiguration zonesyaml = az.getYamlHandler().gFC("zones");	
		String attachedzone = zonesyaml.getString(zone.getName() + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast(cf.fM("&cZone " + zone.getName() + " needs to have a zone attached in order to support zone activation."), "actionzones.admin");
			return;
		}
		ActionZone zone = ActionZones.az.getZoneHandler().getActionZone(attachedzone);
		if (zone != null) {
			zone.setActive(true);
		}
	}


	
}
