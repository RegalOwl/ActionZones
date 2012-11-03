package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class Activate {
	
	public void ActivateZone(ActionZones az, String zonename) {
		
		FileConfiguration zonesyaml = az.getYaml().getZones();	
		String attachedzone = zonesyaml.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support zone activation.", "actionzones.admin");
			return;
		}
		boolean zoneactive = zonesyaml.getBoolean(attachedzone + ".active");
		if (!zoneactive) {
			zonesyaml.set(attachedzone + ".active", true);
		}
	}

	public void DeactivateZone(ActionZones az, String zonename) {
		FileConfiguration zonesyaml = az.getYaml().getZones();
		String attachedzone = zonesyaml.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support zone deactivation.", "actionzones.admin");
			return;
		}
		boolean zoneactive = zonesyaml.getBoolean(attachedzone + ".active");
		if (zoneactive) {
			zonesyaml.set(attachedzone + ".active", false);
		}
	}
	
}
