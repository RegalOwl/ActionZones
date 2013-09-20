package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Smoke extends Action {


	private ArrayList<Double> xvals = new ArrayList<Double>();
	private ArrayList<Double> yvals = new ArrayList<Double>();
	private ArrayList<Double> zvals = new ArrayList<Double>();
	
	private ActionZones az;
	private String zonename;
	
	private String attachedzone;
	private ArrayList<Location> locs = new ArrayList<Location>();
	private int effectthreadid;
	private World w;
	
	
	public void runAction() {
		zonename = zone.getName();


		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		//Returns if there is no attached zone.
		attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zonename + " needs to have a zone attached in order to support smoking.", "actionzones.admin");
			return;
		}
		
		String testduration = zones.getString(zonename + ".duration");
		long duration = 200L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zonename + ".duration"));
		}
		
		double x1 = zones.getDouble(attachedzone + ".p1.x");
		double y1 = zones.getDouble(attachedzone + ".p1.y");
		double z1 = zones.getDouble(attachedzone + ".p1.z");
		double x2 = zones.getDouble(attachedzone + ".p2.x");
		double y2 = zones.getDouble(attachedzone + ".p2.y");
		double z2 = zones.getDouble(attachedzone + ".p2.z");
		w = Bukkit.getWorld(zones.getString(attachedzone + ".world"));
		
		
		
		int c = 0;
		if (x1 <= x2) {
			while (c < (x2 - x1 + 1)) {
				xvals.add(x1 + c);
				c++;
			}
		} else if (x1 > x2) {
			while (c < (x1 - x2 + 1)) {
				xvals.add(x1 - c);
				c++;
			}
		}
		
		c = 0;
		if (y1 <= y2) {
			while (c < (y2 - y1 + 1)) {
				yvals.add(y1 + c);
				c++;
			}
		} else if (y1 > y2) {
			while (c < (y1 - y2 + 1)) {
				yvals.add(y1 - c);
				c++;
			}
		}
		
		
		c = 0;
		if (z1 <= z2) {
			while (c < (z2 - z1 + 1)) {
				zvals.add(z1 + c);
				c++;
			}
		} else if (z1 > z2) {
			while (c < (z1 - z2 + 1)) {
				zvals.add(z1 - c);
				c++;
			}
		}

		double x = 0;
		double y = 0;
		double z = 0;
		int a = 0;
		int b = 0;
		c = 0;
		while (c < zvals.size()) {
			z = zvals.get(c);
			while (b < xvals.size()) {
				x = xvals.get(b);
				while (a < yvals.size()) {
					y = yvals.get(a);
					Location l = new Location(w, x + 1, y, z + 1);
					locs.add(l);

					a++;
				}
				b++;
				a = 0;
			}
			c++;
			b = 0;
		}


		
		
		effectthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				for (int i = 0; i < locs.size(); i++) {
					w.playEffect(locs.get(i), Effect.SMOKE, 0);
				}
				
			}
		}, 1L, 1L);
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				stop();
			}
		}, duration);
		
		
		
	}
	
	
	
	
	
	private void stop() {
		az.getServer().getScheduler().cancelTask(effectthreadid);
	}

	

	

}