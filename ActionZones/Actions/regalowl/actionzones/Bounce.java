package regalowl.actionzones;


import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Bounce {

	

	private Player p;
	private ActionZones az;
	private int bounceid;
	

	
	private int px;
	private int py;
	private int pz;
	
	private long duration;
	
	private ArrayList<Integer> xhits = new ArrayList<Integer>();
	private ArrayList<Integer> yhits = new ArrayList<Integer>();
	private ArrayList<Integer> zhits = new ArrayList<Integer>();

	
	public void startBounce(ActionZones acz, String zonename, Player player) {
		p = player;
		az = acz;
		

		FileConfiguration zones = az.getYaml().getZones();
		String testduration = zones.getString(zonename + ".duration");
		duration = 400L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zonename + ".duration"));
		}
		

		
		bounceid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
			
				Location pl = p.getLocation();
				px = pl.getBlockX();
				py = pl.getBlockY();
				pz = pl.getBlockZ();
				int xrad = 4;
				int yrad = 4;
				int zrad = 4;
				World w = p.getWorld();
				
				
				
				boolean alterCourse = false;
				for (int x = (px - xrad); x <= (px + xrad); x++) {
					for (int z = (pz - zrad); z <= (pz + zrad); z++) {
						for (int y = (py - yrad); y <= (py + yrad); y++) {
							Block b = w.getBlockAt(x, y, z);
							if (b.getType() != Material.AIR) {
								//Location bl = b.getLocation();
								//int bx = b.getX();
								//int by = b.getY();
								//int bz = b.getZ();
								xhits.add(b.getX());
								yhits.add(b.getY());
								zhits.add(b.getZ());
								alterCourse = true;
							}
						}
					}
				}
				
				if (alterCourse) {
					p.sendMessage("alter");
					double bx = averageArrayList(xhits);
					double by = averageArrayList(yhits);
					double bz = averageArrayList(zhits);
					
					Vector v = new Vector();
					v.setX((px - bx)/3);
					v.setY((py - by)/3);
					v.setZ((pz - bz)/3);
					p.setVelocity(v);
				}
				
			}
		}, 3L, 3L);
		
		
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getServer().getScheduler().cancelTask(bounceid);
			}
		}, duration);
		
	}

	
	private double averageArrayList(ArrayList<Integer> array) {
		double total = 0;
		for (double i:array) {
			total += i;
		}
		double average = total/array.size();
		return average;
	}
	
}
