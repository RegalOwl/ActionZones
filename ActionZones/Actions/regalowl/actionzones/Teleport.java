package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Teleport {


	
	public void teleportPlayer(ActionZones az, Player p, String zonename) {

		if (zonename == null) {
			return;
		}

		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support teleportation.", "actionzones.admin");
			return;
		}
		
		//Gets new location.
		FileConfiguration locs = az.getYaml().getLocations();
		double x = locs.getDouble(location + ".x");
		double y = locs.getDouble(location + ".y");
		double z = locs.getDouble(location + ".z");
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		World w = Bukkit.getWorld(locs.getString(location + ".world"));

		//Sets location to new location.
		Location newloc = new Location(w, x, y, z);
		
		
		if (!newloc.getChunk().isLoaded()) {
			newloc.getChunk().load(true);
		}
		
		newloc.setPitch(pitch);
		newloc.setYaw(yaw);
		p.teleport(newloc);
	}
	

	
	
}
