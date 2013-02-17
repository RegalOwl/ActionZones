package regalowl.actionzones;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryStorage {

	
	private ActionZones az;
	
	
	
	public void takeInventory(ActionZones atz, Player p) {
		az = atz;
		FileConfiguration inv = az.getYaml().getInventory();
		if (!inv.getKeys(false).contains(p.getName())) {
			
			int c = 0;
			ItemStack[] itemslots = p.getInventory().getContents();
			while (c < itemslots.length) {
				if (itemslots[c] != null) {
					inv.set(p.getName() + ".inventory.s" + c + ".type", itemslots[c].getTypeId());
					inv.set(p.getName() + ".inventory.s" + c + ".data", itemslots[c].getData().getData());
					inv.set(p.getName() + ".inventory.s" + c + ".durability", itemslots[c].getDurability());
					inv.set(p.getName() + ".inventory.s" + c + ".amount", itemslots[c].getAmount());
					inv.set(p.getName() + ".inventory.s" + c + ".enchantments", itemslots[c].getEnchantments().toString());
				}
				c++;
			}
			c = 0;
			ItemStack[] armorslots = p.getInventory().getArmorContents();
			while (c < armorslots.length) {
				if (armorslots[c] != null) {
					inv.set(p.getName() + ".armor.s" + c + ".type", armorslots[c].getTypeId());
					inv.set(p.getName() + ".armor.s" + c + ".data", armorslots[c].getData().getData());
					inv.set(p.getName() + ".armor.s" + c + ".durability", armorslots[c].getDurability());
					inv.set(p.getName() + ".armor.s" + c + ".amount", armorslots[c].getAmount());
					inv.set(p.getName() + ".armor.s" + c + ".enchantments", armorslots[c].getEnchantments().toString());
				}
				c++;
			}	
			inv.set(p.getName() + ".level", p.getLevel());
			inv.set(p.getName() + ".exp", p.getExp());
			
			
			
			

			
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setLevel(0);
			p.setExp(0);
			
			az.getYaml().saveInventories();
			
			p.sendMessage(ChatColor.GREEN + "Your inventory has been saved.");
			
		} else {
			p.sendMessage(ChatColor.RED + "You must claim your previous inventory before storing a new one!");
		}

	}
	
	
	public void returnInventory(ActionZones az, Player p) {
		FileConfiguration inv = az.getYaml().getInventory();
		if (inv.getKeys(false).contains(p.getName())) {

			if (emptyInventory(p)) {
				int c = 0;
				ItemStack[] itemslots = new ItemStack[36];
				while (c < p.getInventory().getSize()) {
					if (inv.get(p.getName() + ".inventory.s" + c) != null) {
						ItemStack itemslot = new ItemStack(inv.getInt(p.getName() + ".inventory.s" + c + ".type"));
						itemslot.setAmount(inv.getInt(p.getName() + ".inventory.s" + c + ".amount"));
						itemslot.getData().setData((byte) inv.getInt(p.getName() + ".inventory.s" + c + ".data"));
						itemslot.setDurability((short) inv.getInt(p.getName() + ".inventory.s" + c + ".durability"));
						String enchantments = inv.getString(p.getName() + ".inventory.s" + c + ".enchantments");

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
				while (c < p.getInventory().getArmorContents().length) {
					if (inv.get(p.getName() + ".armor.s" + c) != null) {
						ItemStack armorslot = new ItemStack(inv.getInt(p.getName() + ".armor.s" + c + ".type"));
						armorslot.setAmount(inv.getInt(p.getName() + ".armor.s" + c + ".amount"));
						armorslot.getData().setData((byte) inv.getInt(p.getName() + ".armor.s" + c + ".data"));
						armorslot.setDurability((short) inv.getInt(p.getName() + ".armor.s" + c + ".durability"));
						String enchantments = inv.getString(p.getName() + ".armor.s" + c + ".enchantments");

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
				
				
				
				p.getInventory().setContents(itemslots);
				p.getInventory().setArmorContents(armorslots);
				p.setLevel(inv.getInt(p.getName() + ".level"));
				p.setExp((float) inv.getDouble(p.getName() + ".exp"));
				inv.set(p.getName(), null);
				az.getYaml().saveInventories();
				
			} else {
				p.sendMessage(ChatColor.RED + "You must empty your current inventory before you can restore your previous inventory!");
			}
			

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
