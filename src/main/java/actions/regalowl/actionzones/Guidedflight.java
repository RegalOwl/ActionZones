package regalowl.actionzones;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Guidedflight extends Action {

	private Vector v;
	private int launchthreadid;
	private double value;

	
	public void runAction() {

		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		String testduration = zones.getString(zone.getName() + ".duration");
		long duration = 200L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zone.getName() + ".duration"));
			if (duration < 2L) {
				duration = 2L;
			}
		}
		

		String testvalue = zones.getString(zone.getName() + ".value");
		value = 1.0;
		if (testvalue != null) {
			value = Double.parseDouble(zones.getString(zone.getName() + ".value"));
		}
		
		
		v = new Vector();

		
		
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				Location loc = player.getLocation();
				v = loc.getDirection();
				v.setX((v.getX()) * value);
				v.setY((v.getY()) * value);
				v.setZ((v.getZ()) * value);
				player.setVelocity(v);
				
			}
		}, 1L, 1L);
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getServer().getScheduler().cancelTask(launchthreadid);
			}
		}, duration);
		
	}

	

}