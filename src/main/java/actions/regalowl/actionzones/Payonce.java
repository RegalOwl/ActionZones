package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Payonce extends Action {

	
	
	public void runAction() {
		Account acc = az.getAccount();
		String testmoney = zone.getString("money");
		if (testmoney != null) {
			double money = zone.getDouble("money");

			boolean paid = false;
			String testpaid = az.getYamlHandler().gFC("players").getString(player.getName() + "." + zone.getName() + ".paid");
			if (testpaid != null) {
				paid = az.getYamlHandler().gFC("players").getBoolean(player.getName() + "." + zone.getName() + ".paid");
			}
			
			if (!paid) {
				acc.deposit(player, money);
				player.sendMessage(ChatColor.GREEN + "You've earned $" + money + ".");
				az.getYamlHandler().gFC("players").set(player.getName() + "." + zone.getName() + ".paid", true);
			}

		} else {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a monetary amount attached in order to allow payments.", "actionzones.admin");
			return;
		}
		
		



	}
	
}

