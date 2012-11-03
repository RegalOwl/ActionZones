package regalowl.actionzones;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GuidedFlight {

	private Vector v;
	private Player p;
	private ActionZones az;
	private int launchthreadid;
	private double value;

	
	public void Fly(ActionZones acz, String zonename, Player player) {
		p = player;
		az = acz;

		FileConfiguration zones = az.getYaml().getZones();
		String testduration = zones.getString(zonename + ".duration");
		long duration = 200L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zonename + ".duration"));
			if (duration < 2L) {
				duration = 2L;
			}
		}
		

		String testvalue = zones.getString(zonename + ".value");
		value = 1.0;
		if (testvalue != null) {
			value = Double.parseDouble(zones.getString(zonename + ".value"));
		}
		
		
		v = new Vector();

		
		
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				Location loc = p.getLocation();
				v = loc.getDirection();
				v.setX((v.getX()) * value);
				v.setY((v.getY()) * value);
				v.setZ((v.getZ()) * value);
				p.setVelocity(v);
				
			}
		}, 1L, 1L);
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				finish();
			}
		}, duration);
		
	}
	
	private void finish() {
		az.getServer().getScheduler().cancelTask(launchthreadid);
	}
	

}