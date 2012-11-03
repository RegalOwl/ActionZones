package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LaunchDirection {
	
	public void LaunchinDirection(ActionZones az, String zonename, Player p) {
		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support directional launching.", "actionzones.admin");
			return;
		}
		
		
		FileConfiguration zones = az.getYaml().getZones();
		String testvalue = zones.getString(zonename + ".value");
		double value = 1.0;
		if (testvalue != null) {
			value = Double.parseDouble(zones.getString(zonename + ".value"));
		}
		
		
		FileConfiguration locs = az.getYaml().getLocations();
		double x = locs.getDouble(location + ".x");
		double y = locs.getDouble(location + ".y");
		double z = locs.getDouble(location + ".z");
		
		Location pl = p.getLocation();
		double px = pl.getX();
		double py = pl.getY();
		double pz = pl.getZ();
		
		
		Vector v = new Vector();
		v.setX(0);
		v.setY(0);
		v.setZ(0);
		p.setVelocity(v);
		
		v.setX((x - px) * value);
		v.setY((y - py) * value);
		v.setZ((z - pz) * value);
		p.setVelocity(v);
		
	}

}
