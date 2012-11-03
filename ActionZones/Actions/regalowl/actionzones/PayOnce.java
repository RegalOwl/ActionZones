package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PayOnce {

	
	
	public void payOnce(ActionZones az, Player p, String zonename, Account acc) {
		String testmoney = az.getYaml().getZones().getString(zonename + ".money");
		if (testmoney != null) {
			double money = az.getYaml().getZones().getDouble(zonename + ".money");
			if (zonename == null) {
				return;
			}
			
			boolean paid = false;
			String testpaid = az.getYaml().getPlayers().getString(p.getName() + "." + zonename + ".paid");
			if (testpaid != null) {
				paid = az.getYaml().getPlayers().getBoolean(p.getName() + "." + zonename + ".paid");
			}
			
			if (!paid) {
				acc.deposit(p, money);
				p.sendMessage(ChatColor.GREEN + "You've earned $" + money + ".");
				az.getYaml().getPlayers().set(p.getName() + "." + zonename + ".paid", true);
			}

		} else {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a monetary amount attached in order to allow payments.", "actionzones.admin");
			return;
		}
		
		



	}
	
}

