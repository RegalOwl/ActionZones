package regalowl.actionzones;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpaceLaunch {

	private Vector v;
	private Player p;
	private ActionZones az;
	private int launchthreadid;
	
	
	public void LaunchSpace(ActionZones acz, String zonename, Player player) {
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
		
		
		v = new Vector();

		
		
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				v.setX(p.getVelocity().getX());
				v.setZ(p.getVelocity().getZ());
				v.setY(1000);
				p.setVelocity(v);
				
			}
		}, 5L, 5L);
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getServer().getScheduler().cancelTask(launchthreadid);
			}
		}, duration);
		
	}
	

}