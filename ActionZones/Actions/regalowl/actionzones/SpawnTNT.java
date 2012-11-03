package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import regalowl.actionzones.ActionZones;
import regalowl.actionzones.Zone;


public class SpawnTNT {

	public void spawn(ActionZones az, Player p, String zonename) {
		Zone zone = az.getZone();
		zone.setinZone(p);
		
		if (zonename == null) {
			return;
		}

		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support TNT spawning.", "actionzones.admin");
			return;
		}
		
		
		String quant = az.getYaml().getZones().getString(zonename + ".quantity");
		int quantity = 1;
		if (quant != null) {
			quantity = az.getYaml().getZones().getInt(zonename + ".quantity");
		}
		
		FileConfiguration locs = az.getYaml().getLocations();
		Double x = locs.getDouble(location + ".x");
		Double y = locs.getDouble(location + ".y");
		Double z = locs.getDouble(location + ".z");
		World w = Bukkit.getWorld(locs.getString(location + ".world"));
		Location loc = new Location(w, x, y, z);
		if (!loc.getChunk().isLoaded()) {
			loc.getChunk().load(true);
		}
		
		int c = 0;
		while (c < quantity) {
			w.spawn(loc, TNTPrimed.class);
			c++;
		}
		
	}
	
	
}
