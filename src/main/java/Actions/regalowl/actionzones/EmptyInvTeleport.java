package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EmptyInvTeleport {

	
	public void emptyInvTeleport(ActionZones az, Player p, String zonename) {

		if (zonename == null) {
			return;
		}

		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support teleportation.", "actionzones.admin");
			return;
		}
		
		if (emptyInventory(p)) {

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
		} else {
			p.sendMessage(ChatColor.RED + "You must have an empty inventory in order to use this teleport.");
		}

	}
	
	
	/**
	 * 
	 * 
	 * This function determines if a player has an empty inventory including armor slots.
	 * 
	 */
	
	public boolean emptyInventory(Player p){
			int availablespace = 0;
			int slot = 0;
			while (slot < 36) {
				if (p.getInventory().getItem(slot) == null) {
					availablespace++;
				}
			slot++;
			}
			ItemStack[] armor = p.getInventory().getArmorContents();
			slot = 0;
			while (slot < 4) {
				if (armor[slot].getType() == Material.AIR) {
					availablespace++;
				}
				slot++;
			}
			if (availablespace == 40) {
				return true;
			} else {
				return false;
			}
	}
	
	
}
