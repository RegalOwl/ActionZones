package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnItem {


	
	public void spawnItem(ActionZones az, Player p, String zonename) {
		Zone zone = az.getZone();
		zone.setinZone(p);
		
		if (zonename == null) {
			return;
		}
		FileConfiguration zones = az.getYaml().getZones();
		
		String testid = zones.getString(zonename + ".blockid");
		int blockid = 367;
		if (testid != null) {
			blockid = Integer.parseInt(zones.getString(zonename + ".blockid"));
		}
		
		String testdamage = zones.getString(zonename + ".damagevalue");
		byte damagevalue = 0;
		if (testdamage != null) {
			damagevalue = (byte) Integer.parseInt(zones.getString(zonename + ".damagevalue"));
		}
		
		int durability = 0;
		boolean durable = false;
		String testdurability = zones.getString(zonename + ".durability");
		if (testdurability != null) {
			durability = Integer.parseInt(zones.getString(zonename + ".durability"));
			durable = true;
		}
		
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support mob spawning.", "actionzones.admin");
			return;
		}

		
		String quant = az.getYaml().getZones().getString(zonename + ".quantity");
		int quantity = 1;
		if (quant != null) {
			quantity = az.getYaml().getZones().getInt(zonename + ".quantity");
		}
		
		ItemStack item = new ItemStack(blockid);
		item.getData().setData(damagevalue);
		item.setAmount(quantity);
		if (durable) {
			item.setDurability((short) durability);
		}
		
		
		FileConfiguration locs = az.getYaml().getLocations();
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
