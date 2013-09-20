package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Pay extends Action{

	public void runAction() {
		Account acc = az.getAccount();
		String testmoney = zone.getString("money");
		if (testmoney != null) {
			double money = zone.getDouble("money");

			acc.deposit(player, money);
			player.sendMessage(ChatColor.GREEN + "You've earned $" + money + ".");
			az.getYamlHandler().gFC("players").set(player.getName() + "." + zone.getName() + ".paid", true);
			

		} else {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a monetary amount attached in order to allow payments.", "actionzones.admin");
			return;
		}


	}
	
}

