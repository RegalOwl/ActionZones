package regalowl.actionzones;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Storeinventory extends Action {

	public void runAction() {
		FileConfiguration inv = az.getYamlHandler().gFC("inventory");
		if (!inv.getKeys(false).contains(player.getName())) {
			
			int c = 0;
			ItemStack[] itemslots = player.getInventory().getContents();
			while (c < itemslots.length) {
				if (itemslots[c] != null) {
					inv.set(player.getName() + ".inventory.s" + c + ".type", itemslots[c].getTypeId());
					inv.set(player.getName() + ".inventory.s" + c + ".data", itemslots[c].getData().getData());
					inv.set(player.getName() + ".inventory.s" + c + ".durability", itemslots[c].getDurability());
					inv.set(player.getName() + ".inventory.s" + c + ".amount", itemslots[c].getAmount());
					inv.set(player.getName() + ".inventory.s" + c + ".enchantments", itemslots[c].getEnchantments().toString());
				}
				c++;
			}
			c = 0;
			ItemStack[] armorslots = player.getInventory().getArmorContents();
			while (c < armorslots.length) {
				if (armorslots[c] != null) {
					inv.set(player.getName() + ".armor.s" + c + ".type", armorslots[c].getTypeId());
					inv.set(player.getName() + ".armor.s" + c + ".data", armorslots[c].getData().getData());
					inv.set(player.getName() + ".armor.s" + c + ".durability", armorslots[c].getDurability());
					inv.set(player.getName() + ".armor.s" + c + ".amount", armorslots[c].getAmount());
					inv.set(player.getName() + ".armor.s" + c + ".enchantments", armorslots[c].getEnchantments().toString());
				}
				c++;
			}	
			inv.set(player.getName() + ".level", player.getLevel());
			inv.set(player.getName() + ".exp", player.getExp());
			
			
			
			

			
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setLevel(0);
			player.setExp(0);
			
			az.getYamlHandler().saveYaml("inventory");
			
			player.sendMessage(ChatColor.GREEN + "Your inventory has been saved.");
			
		} else {
			player.sendMessage(ChatColor.RED + "You must claim your previous inventory before storing a new one!");
		}

	}
	
}
