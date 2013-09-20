package regalowl.actionzones;

import org.bukkit.util.Vector;

public class Spacelaunch extends Action {

	private Vector v;
	private int launchthreadid;
	
	
	public void runAction() {

		String testduration = zone.getString("duration");
		long duration = 200L;
		if (testduration != null) {
			duration = zone.getInt("duration");
			if (duration < 2L) {
				duration = 2L;
			}
		}
		
		
		v = new Vector();

		
		
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				v.setX(player.getVelocity().getX());
				v.setZ(player.getVelocity().getZ());
				v.setY(1000);
				player.setVelocity(v);
				
			}
		}, 5L, 5L);
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getServer().getScheduler().cancelTask(launchthreadid);
			}
		}, duration);
		
	}
	

}