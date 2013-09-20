package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;


public class Payteleport extends Action {

	
	
	public void runAction() {
		Account acc = az.getAccount();
		String testmoney = zone.getString("money");
		if (testmoney != null) {
			double money = zone.getDouble("money");

			//Returns if there is no attached location.
			String location = zone.getString("location");
			if (location == null) {
				Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a location attached in order to support teleportation.", "actionzones.admin");
				return;
			}
			
			if (acc.checkFunds(player, money)) {
				acc.withdraw(player, money);
				player.sendMessage(ChatColor.GREEN + "You paid $" + money + ".");
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
			} else {
				player.sendMessage(ChatColor.DARK_RED + "In order to use this teleport you must have $" + money + ".");
			}

		} else {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a monetary amount attached in order to support pay teleportation.", "actionzones.admin");
			return;
		}
		
		



	}
	
}

