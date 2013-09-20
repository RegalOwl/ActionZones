package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Spawnitem extends Action {


	
	public void runAction() {

		
		String testid = zone.getString("blockid");
		int blockid = 367;
		if (testid != null) {
			blockid = zone.getInt("blockid");
		}
		
		String testdamage = zone.getString("damagevalue");
		byte damagevalue = 0;
		if (testdamage != null) {
			damagevalue = (byte) zone.getInt("damagevalue");
		}
		
		int durability = 0;
		boolean durable = false;
		String testdurability = zone.getString("durability");
		if (testdurability != null) {
			durability = zone.getInt("durability");
			durable = true;
		}
		
		String location = zone.getString("location");
		if (location == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a location attached in order to support item spawning.", "actionzones.admin");
			return;
		}

		
		String quant = zone.getString("quantity");
		int quantity = 1;
		if (quant != null) {
			quantity = zone.getInt("quantity");
		}
		
		ItemStack item = new ItemStack(blockid);
		item.getData().setData(damagevalue);
		item.setAmount(quantity);
		if (durable) {
			item.setDurability((short) durability);
		}
		
		
		FileConfiguration locs = az.getYamlHandler().gFC("locations");
		double x = locs.getDouble(location + ".x");
		double y = locs.getDouble(location + ".y");
		double z = locs.getDouble(location + ".z");
		World w = Bukkit.getWorld(locs.getString(location + ".world"));
		Location l = new Location(w, x, y, z);
		if (!l.getChunk().isLoaded()) {
			l.getChunk().load(true);
		}
		w.dropItemNaturally(l, item);
	}

	
	
}
