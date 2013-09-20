package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import regalowl.databukkit.CommonFunctions;

public class Zone {
	
	protected ActionZones az;
	
	protected Integer x1;
	protected Integer y1;
	protected Integer z1;
	protected Integer x2;
	protected Integer y2;
	protected Integer z2;
	protected String world;
	
	protected String name;
	
	protected FileConfiguration zonesConfig;
	protected CommonFunctions cf;


	Zone(String name) {
		az = ActionZones.az;
		cf = az.getCommonFunctions();
		this.name = name;
		zonesConfig = az.getYamlHandler().gFC("zones");
		world = getString("world");
		x1 = getInt("p1.x");
		y1 = getInt("p1.y");
		z1 = getInt("p1.z");
		x2 = getInt("p2.x");
		y2 = getInt("p2.y");
		z2 = getInt("p2.z");
	}
	
	Zone(String name, int x, int y, int z, String world) {
		az = ActionZones.az;
		cf = az.getCommonFunctions();
		zonesConfig = az.getYamlHandler().gFC("zones");
		this.name = name;
		this.world = world;
		this.x1 = x;
		this.y1 = y;
		this.z1 = z;
		this.x2 = x;
		this.y2 = y;
		this.z2 = z;
		zonesConfig.set(name + ".world", world);
		zonesConfig.set(name + ".p1.x", x1);
		zonesConfig.set(name + ".p1.y", y1);
		zonesConfig.set(name + ".p1.z", z1);
		zonesConfig.set(name + ".p2.x", x2);
		zonesConfig.set(name + ".p2.y", y2);
		zonesConfig.set(name + ".p2.z", z2);
	}
	
	
	public final String getString(String value) {
		return zonesConfig.getString(name + "." + value);
	}
	public final double getDouble(String value) {
		return zonesConfig.getDouble(name + "." + value);
	}
	public final int getInt(String value) {
		return zonesConfig.getInt(name + "." + value);
	}
	public final long getLong(String value) {
		return zonesConfig.getLong(name + "." + value);
	}
	public final boolean getBoolean(String value) {
		return zonesConfig.getBoolean(name + "." + value);
	}
	public final boolean testData(String value) {
		if (zonesConfig.getString(name + "." + value) == null) {return false;}
		return true;
	}
	
	
	public final String getName() {return name;}
	
	
	
	public final void setP1(Player p){
		x1 = p.getLocation().getBlockX();
		y1 = p.getLocation().getBlockY();
		z1 = p.getLocation().getBlockZ();
		world = p.getWorld().getName();
		zonesConfig.set(name + ".world", world);
		zonesConfig.set(name + ".p1.x", x1);
		zonesConfig.set(name + ".p1.y", y1);
		zonesConfig.set(name + ".p1.z", z1);
	}

	public final void setP2(Player p){
		int x2 = p.getLocation().getBlockX();
		int y2 = p.getLocation().getBlockY();
		int z2 = p.getLocation().getBlockZ();
		String w = p.getWorld().getName();
		zonesConfig.set(name + ".world", w);
		zonesConfig.set(name + ".p2.x", x2);
		zonesConfig.set(name + ".p2.y", y2);
		zonesConfig.set(name + ".p2.z", z2);
	}
	
	
	
	public boolean inZone(Location location) {
		if (location.getWorld().equals(Bukkit.getWorld(world))) {
			int locx = location.getBlockX();
			int rangex = Math.abs(x1 - x2);
			if (Math.abs(locx - x1) <= rangex && Math.abs(locx - x2) <= rangex) {
				int locz = location.getBlockZ();
				int rangez = Math.abs(z1 - z2);
				if (Math.abs(locz - z1) <= rangez && Math.abs(locz - z2) <= rangez) {
					int locy = location.getBlockY();
					int rangey = Math.abs(y1 - y2);
					if (Math.abs(locy - y1) <= rangey && Math.abs(locy - y2) <= rangey) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	public void removeZone() {
		zonesConfig.set(name, null);
	}
	
	
	/*
	public void renameZone(String newName) {
		FileConfiguration zonesfile = az.getYaml().getZones();
		zonesConfig.set(newName + ".world", zonesfile.get(name + ".world"));
		zonesConfig.set(newName + ".p1.x", zonesfile.get(name + ".p1.x"));
		zonesConfig.set(newName + ".p1.y", zonesfile.get(name + ".p1.y"));
		zonesConfig.set(newName + ".p1.z", zonesfile.get(name + ".p1.z"));
		zonesConfig.set(newName + ".p2.x", zonesfile.get(name + ".p2.x"));
		zonesConfig.set(newName + ".p2.y", zonesfile.get(name + ".p2.y"));
		zonesConfig.set(newName + ".p2.z", zonesfile.get(name + ".p2.z"));
		zonesConfig.set(name, null);

	}
	*/
	
	
}
