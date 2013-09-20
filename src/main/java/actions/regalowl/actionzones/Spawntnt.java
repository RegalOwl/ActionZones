package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.TNTPrimed;


public class Spawntnt extends Action {

	public void runAction() {

		String location = zone.getString("location");
		if (location == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a location attached in order to support TNT spawning.", "actionzones.admin");
			return;
		}
		
		
		String quant = zone.getString("quantity");
		int quantity = 1;
		if (quant != null) {
			quantity = zone.getInt("quantity");
		}
		
		FileConfiguration locs = az.getYamlHandler().gFC("locations");
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
