package regalowl.actionzones;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Restoreinventory extends Action {
	public void runAction() {
		FileConfiguration inv = az.getYamlHandler().gFC("inventory");
		if (inv.getKeys(false).contains(player.getName())) {

			if (emptyInventory(player)) {
				int c = 0;
				ItemStack[] itemslots = new ItemStack[36];
				while (c < player.getInventory().getSize()) {
					if (inv.get(player.getName() + ".inventory.s" + c) != null) {
						ItemStack itemslot = new ItemStack(inv.getInt(player.getName() + ".inventory.s" + c + ".type"));
						itemslot.setAmount(inv.getInt(player.getName() + ".inventory.s" + c + ".amount"));
						itemslot.getData().setData((byte) inv.getInt(player.getName() + ".inventory.s" + c + ".data"));
						itemslot.setDurability((short) inv.getInt(player.getName() + ".inventory.s" + c + ".durability"));
						String enchantments = inv.getString(player.getName() + ".inventory.s" + c + ".enchantments");

						while (enchantments.length() > 2) {
							int eid = Integer.parseInt(enchantments.substring(enchantments.indexOf("[") + 1, enchantments.indexOf(",", enchantments.indexOf("["))));
							int lvl = Integer.parseInt(enchantments.substring(enchantments.indexOf("=") + 1, enchantments.indexOf("=") + 2));
							enchantments = enchantments.substring(enchantments.indexOf("=") + 2, enchantments.length());
							Enchantment enchant = Enchantment.getById(eid);
							itemslot.addUnsafeEnchantment(enchant, lvl);
						}

						itemslots[c] = itemslot;
					}
					c++;
				}
				c = 0;
				ItemStack[] armorslots = new ItemStack[4];
				while (c < player.getInventory().getArmorContents().length) {
					if (inv.get(player.getName() + ".armor.s" + c) != null) {
						ItemStack armorslot = new ItemStack(inv.getInt(player.getName() + ".armor.s" + c + ".type"));
						armorslot.setAmount(inv.getInt(player.getName() + ".armor.s" + c + ".amount"));
						armorslot.getData().setData((byte) inv.getInt(player.getName() + ".armor.s" + c + ".data"));
						armorslot.setDurability((short) inv.getInt(player.getName() + ".armor.s" + c + ".durability"));
						String enchantments = inv.getString(player.getName() + ".armor.s" + c + ".enchantments");

						while (enchantments.length() > 2) {
							int eid = Integer.parseInt(enchantments.substring(enchantments.indexOf("[") + 1, enchantments.indexOf(",", enchantments.indexOf("["))));
							int lvl = Integer.parseInt(enchantments.substring(enchantments.indexOf("=") + 1, enchantments.indexOf("=") + 2));
							enchantments = enchantments.substring(enchantments.indexOf("=") + 2, enchantments.length());
							Enchantment enchant = Enchantment.getById(eid);
							armorslot.addUnsafeEnchantment(enchant, lvl);
						}
						armorslots[c] = armorslot;
					}
					c++;
				}
				
				
				
				player.getInventory().setContents(itemslots);
				player.getInventory().setArmorContents(armorslots);
				player.setLevel(inv.getInt(player.getName() + ".level"));
				player.setExp((float) inv.getDouble(player.getName() + ".exp"));
				inv.set(player.getName(), null);
				az.getYamlHandler().saveYaml("inventory");
				
			} else {
				player.sendMessage(ChatColor.RED + "You must empty your current inventory before you can restore your previous inventory!");
			}
			

		}
	}
	
	private boolean emptyInventory(Player p){
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
