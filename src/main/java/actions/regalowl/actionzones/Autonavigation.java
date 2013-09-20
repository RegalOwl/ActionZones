package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Autonavigation extends Action {

	
	private Vector v;
	private Player p;
	private int navid;
	private int landthreadid;
	
	private double x;
	private double y;
	private double z;
	
	private double px;
	private double py;
	private double pz;
	
	
	private long duration;
	
	
	
	private ArrayList<Block> bup = new ArrayList<Block>();
	private ArrayList<Block> bdown = new ArrayList<Block>();
	private ArrayList<Block> bforward = new ArrayList<Block>();
	private ArrayList<Block> bleft = new ArrayList<Block>();
	private ArrayList<Block> bright = new ArrayList<Block>();
	
	public void runAction() {

		az = ActionZones.az;
		
		forceCancel();
		
		String location = az.getYamlHandler().gFC("zones").getString(zone.getName() + ".location");
		if (location == null) {
			Bukkit.broadcast(cf.fM("&cZone " + zone.getName() + " needs to have a location attached in order to support directional launching."), "actionzones.admin");
			return;
		}

		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		String testduration = zones.getString(zone.getName() + ".duration");
		duration = 400L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zone.getName() + ".duration"));
		}
		
		FileConfiguration locs = az.getYamlHandler().gFC("locations");
		x = locs.getInt(location + ".x");
		y = locs.getInt(location + ".y");
		z = locs.getInt(location + ".z");
		

		
		
		navid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
			
				Location pl = p.getLocation();
				px = pl.getX();
				py = pl.getY();
				pz = pl.getZ();
				
				
				double xd = x - px;
				double zd = z - pz;
				double yd = y - py;
				
				Double d = Math.sqrt(Math.pow(xd, 2) + Math.pow(zd, 2) + Math.pow(yd, 2));

				Location loc = p.getLocation();
				loc.setPitch(0); // ignore whether the player is looking up or down - maybe you have to set this to 90 or Math.PI/2
				Vector dir = loc.getDirection();
				 
				// if you don't want diaginal blocks add these 4 lines:
				if (dir.getX() > dir.getZ())
				  dir.setZ(0);
				else
				  dir.setX(0);
				 
				dir.normalize();
				Block b = loc.add(dir.multiply(1)).getBlock(); // distance can be any number, e.g. 2.5
				// b should now be the block in front of the player
				loc.getBlock().getFace(b);
				for (int i = 0; i < 20; i++) {
					if (i % 2 == 0) {
						bup.add(p.getWorld().getBlockAt(p.getLocation().getBlockX(), p.getLocation().getBlockY() + i, p.getLocation().getBlockZ()));
					}
				}
				
				
				
				
			}
		}, 1L, 1L);
	}
	
	
	
	private void forceCancel() {
		
		landthreadid = az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getServer().getScheduler().cancelTask(navid);
			}
		}, duration);
	}
	
}
