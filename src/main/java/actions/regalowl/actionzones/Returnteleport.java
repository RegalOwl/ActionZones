package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Returnteleport extends Action {

	
	
	public void runAction() {


		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		//Returns if there is no attached zone.
		String attachedzone = zone.getString("zone");
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a zone attached in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		String testaction = zones.getString(attachedzone + ".actions");
		if (testaction == null || !(testaction.replace("_", "").contains("logentry") || testaction.replace("_", "").contains("LOGENTRY"))) {
			Bukkit.broadcast("�cZone " + attachedzone + " needs to have its action set to logentry in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		//Returns if there is no attached location.
		String location = zone.getString("location");
		if (location == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a location attached in order to support return teleportation.", "actionzones.admin");
			return;
		}
		
		
		FileConfiguration entrylist = az.getYamlHandler().gFC("entrancelist");   							
		String testlist = entrylist.getString(attachedzone);
			
		if (testlist != null) {
			ArrayList<String> enterednames = az.getCommonFunctions().explode(entrylist.getString(attachedzone), ","); 
			if (enterednames.contains(player.getName())) {
				//Gets new location.
				FileConfiguration locs = az.getYamlHandler().gFC("locations");
				double x = locs.getDouble(location + ".x");
				double y = locs.getDouble(location + ".y");
				double z = locs.getDouble(location + ".z");
				float yaw = player.getLocation().getYaw();
				float pitch = player.getLocation().getPitch();
				World w = Bukkit.getWorld(locs.getString(location + ".world"));

				//Sets location to new location.
				Location newloc = new Location(w, x, y, z);
				
				
				if (!newloc.getChunk().isLoaded()) {
					newloc.getChunk().load(true);
				}
				
				newloc.setPitch(pitch);
				newloc.setYaw(yaw);
				player.teleport(newloc);
				
				player.sendMessage(ChatColor.GREEN + "Welcome Back!");
			} else {
				player.sendMessage(ChatColor.RED + "Access Denied!");
			}
			
			

		} else {
			player.sendMessage(ChatColor.RED + "Access Denied!");
		}

	}
	
	
}
