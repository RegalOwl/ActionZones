package regalowl.actionzones;



import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LaunchLocation {

	private Vector v;
	private Player p;
	private ActionZones az;
	private int launchthreadid;
	private int landthreadid;
	
	private double x;
	private double y;
	private double z;
	
	private double px;
	private double py;
	private double pz;
	
	private int counter = 0;
	
	private long duration;
	
	ArrayList<Block> blocks = new ArrayList<Block>();
	
	public void LaunchtoLocation(ActionZones acz, String zonename, Player player) {
		p = player;
		az = acz;
		
		
		
		
		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support directional launching.", "actionzones.admin");
			return;
		}
		
		
		FileConfiguration zones = az.getYaml().getZones();
		String testduration = zones.getString(zonename + ".duration");
		duration = 400L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zonename + ".duration"));
		}
		
		FileConfiguration locs = az.getYaml().getLocations();
		x = locs.getInt(location + ".x");
		y = locs.getInt(location + ".y");
		z = locs.getInt(location + ".z");
		
		Location pl = p.getLocation();
		px = pl.getBlockX();
		py = pl.getBlockY();
		pz = pl.getBlockZ();
		
		
		v = new Vector();
		v.setX(0);
		v.setY(0);
		v.setZ(0);
		p.setVelocity(v);
		

		navigateXZ();

	}
	
	
	private void navigateXZ() {
		forceCancel();
		counter = 0;
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				
				
				if (Math.abs(Math.abs(x) - Math.abs(px)) + Math.abs(Math.abs(z) - Math.abs(pz)) < .1) {
					land();
					v.setY(p.getVelocity().getY());
					v.setX(0);
					v.setZ(0);
					cancelNavigateXZ();
					return;
				}

				
				Location pl = p.getLocation();
				px = pl.getX();
				py = pl.getY();
				pz = pl.getZ();
				
				
				double xd = x - px;
				double zd = z - pz;
				double yd = y - py;
				
				if (counter == 0) {
					v.setY(2);
					v.setX(p.getVelocity().getX());
					v.setZ(p.getVelocity().getZ());
					p.setVelocity(v);
				}

				
				
				Double d = Math.sqrt(Math.pow(xd, 2) + Math.pow(zd, 2));
				if (d > 300 || yd > 0) {
					int m = 1;
					if (yd > 20) {
						m = 10;
					} else if (py > 400) {
						m = 0;
					}
				
					
					
					v.setY(3 * m);
					v.setX(xd/10);
					v.setZ(zd/10);
					p.setVelocity(v);
				} else {
					v.setY(p.getVelocity().getY());
					v.setX(xd/10);
					v.setZ(zd/10);
					p.setVelocity(v);
					
				}
				p.setFallDistance(0);
				counter++;
				
			}
		}, 1L, 1L);
	}
	
	
	
	private void forceCancel() {
		
		landthreadid = az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				cancelNavigateXZ();
			}
		}, duration);
	}
	
	private void cancelNavigateXZ() {
		az.getServer().getScheduler().cancelTask(launchthreadid);
	}
	
	
	private void land() {
		counter = 0;
		
		landthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {

				p.setFallDistance(0);
				counter++;
				
				if (counter > 200) {
					cancelLand();
				}
			}
		}, 1L, 1L);
	}
	
	private void cancelLand() {
		az.getServer().getScheduler().cancelTask(landthreadid);
	}
	

}
