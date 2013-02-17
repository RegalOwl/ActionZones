package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ReturnTeleport {

	
	
	public void returnTeleportPlayer(ActionZones az, Player p, String zonename) {

		if (zonename == null) {
			return;
		}

		FileConfiguration zones = az.getYaml().getZones();
		//Returns if there is no attached zone.
		String attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		String testaction = zones.getString(attachedzone + ".action");
		if (testaction == null || !testaction.equalsIgnoreCase("logentry")) {
			Bukkit.broadcast("§cZone " + attachedzone + " needs to have its action set to entrylog in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		
		FileConfiguration entrylist = az.getYaml().getEntranceList();   							
		String testlist = entrylist.getString(attachedzone);
			
		if (testlist != null) {
			SerializeArrayList sal = new SerializeArrayList();
			ArrayList<String> enterednames = sal.stringToArray(entrylist.getString(attachedzone)); 
			if (enterednames.contains(p.getName())) {
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
				
				p.sendMessage(ChatColor.GREEN + "Welcome Back!");
			} else {
				p.sendMessage(ChatColor.RED + "Access Denied!");
			}
			
			

		} else {
			p.sendMessage(ChatColor.RED + "Access Denied!");
		}

	}
	
	
}
