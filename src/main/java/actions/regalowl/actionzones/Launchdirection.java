package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.util.Vector;

public class Launchdirection extends Action {
	
	public void runAction() {
		//Returns if there is no attached location.
		String location = zone.getString("location");
		if (location == null) {
			Bukkit.broadcast("�cZone " + zone.getName() + " needs to have a location attached in order to support directional launching.", "actionzones.admin");
			return;
		}

		String testvalue = zone.getString("value");
		double value = 1.0;
		if (testvalue != null) {
			value = zone.getDouble("value");
		}
		
		
		FileConfiguration locs = az.getYamlHandler().gFC("locations");
		double x = locs.getDouble(location + ".x");
		double y = locs.getDouble(location + ".y");
		double z = locs.getDouble(location + ".z");
		
		Location pl = player.getLocation();
		double px = pl.getX();
		double py = pl.getY();
		double pz = pl.getZ();
		
		
		Vector v = new Vector();
		v.setX(0);
		v.setY(0);
		v.setZ(0);
		player.setVelocity(v);
		
		v.setX((x - px) * value);
		v.setY((y - py) * value);
		v.setZ((z - pz) * value);
		player.setVelocity(v);
		
	}

}
