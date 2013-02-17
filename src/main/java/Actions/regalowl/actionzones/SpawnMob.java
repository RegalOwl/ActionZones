package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMob {
	
	private HashMap<String, EntityType> mobs = new HashMap<String, EntityType>();
	
	
	
	
	
	SpawnMob() {
		mobs.put("blaze", EntityType.BLAZE);
		mobs.put("cavespider", EntityType.CAVE_SPIDER);
		mobs.put("chicken", EntityType.CHICKEN);
		mobs.put("cow", EntityType.COW);
		mobs.put("creeper", EntityType.CREEPER);
		mobs.put("enderdragon", EntityType.ENDER_DRAGON);
		mobs.put("enderman", EntityType.ENDERMAN);
		mobs.put("ghast", EntityType.GHAST);
		mobs.put("giant", EntityType.GIANT);
		mobs.put("irongolem", EntityType.IRON_GOLEM);
		mobs.put("magmacube", EntityType.MAGMA_CUBE);
		mobs.put("mooshroom", EntityType.MUSHROOM_COW);
		mobs.put("ocelot", EntityType.OCELOT);
		mobs.put("pig", EntityType.PIG);
		mobs.put("pigzombie", EntityType.PIG_ZOMBIE);
		mobs.put("sheep", EntityType.SHEEP);
		mobs.put("silverfish", EntityType.SILVERFISH);
		mobs.put("skeleton", EntityType.SKELETON);
		mobs.put("slime", EntityType.SLIME);
		mobs.put("snowman", EntityType.SNOWMAN);
		mobs.put("spider", EntityType.SPIDER);
		mobs.put("squid", EntityType.SQUID);
		mobs.put("villager", EntityType.VILLAGER);
		mobs.put("wolf", EntityType.WOLF);
		mobs.put("zombie", EntityType.ZOMBIE);
	}
	
	
	
	
	
	
	
	public void spawn(ActionZones az, Player p, String zonename) {
		Zone zone = az.getZone();
		zone.setinZone(p);
		
		if (zonename == null) {
			return;
		}

		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support mob spawning.", "actionzones.admin");
			return;
		}
		
		String mob = az.getYaml().getZones().getString(zonename + ".mob");
		if (mob == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a mob attached in order to support mob spawning.", "actionzones.admin");
			return;
		}
		
		String quant = az.getYaml().getZones().getString(zonename + ".quantity");
		int quantity = 1;
		if (quant != null) {
			quantity = az.getYaml().getZones().getInt(zonename + ".quantity");
		}
		
		FileConfiguration locs = az.getYaml().getLocations();
		Double x = locs.getDouble(location + ".x");
		Double y = locs.getDouble(location + ".y");
		Double z = locs.getDouble(location + ".z");
		World w = Bukkit.getWorld(locs.getString(location + ".world"));
		Location loc = new Location(w, x, y, z);
		if (!loc.getChunk().isLoaded()) {
			loc.getChunk().load(true);
		}
		
		int c = 0;
		while (c < quantity) {
			w.spawnCreature(loc, getEntityType(mob));
			c++;
		}
		
	}
	
	
	public EntityType getEntityType(String name) {
		if (mobs.containsKey(name)) {
			return mobs.get(name);
		} else {
			return null;
		}
	}
	
	
	public ArrayList<String> getMobs() {
		String mo[] = mobs.keySet().toArray(new String[0]);
		ArrayList<String> m = new ArrayList<String>();
		for (int i = 0; i < mobs.size(); i++) {
			m.add(mo[i]);
		}
		Collections.sort(m, String.CASE_INSENSITIVE_ORDER);
		return m;
	}
	
	

}
