package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnBlocks {

	
	private ArrayList<Integer> xvals = new ArrayList<Integer>();
	private ArrayList<Integer> yvals = new ArrayList<Integer>();
	private ArrayList<Integer> zvals = new ArrayList<Integer>();
	
	private ActionZones az;
	private String zonename;
	private Action action;
	
	private String attachedzone;
	
	private ArrayList<Integer> fpid = new ArrayList<Integer>();
	private ArrayList<Block> cblocks = new ArrayList<Block>();
	
	public void spawnBlocks(ActionZones acz, String zone, Action act) {
		az = acz;
		zonename = zone;
		action = act;
		fpid = az.getFpid();
		
		
		if (zonename == null) {
			return;
		}

		FileConfiguration zones = az.getYaml().getZones();
		//Returns if there is no attached zone.
		attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a zone attached in order to support block creation.", "actionzones.admin");
			return;
		}
		
		String testduration = zones.getString(zonename + ".duration");
		long duration = 200L;
		if (testduration != null) {
			duration = Integer.parseInt(zones.getString(zonename + ".duration"));
			if (duration < 2L) {
				duration = 2L;
			}
		}
		
		String testid = zones.getString(zonename + ".blockid");
		int blockid = 1;
		if (testid != null) {
			blockid = Integer.parseInt(zones.getString(zonename + ".blockid"));
		}
		
		String testdamage = zones.getString(zonename + ".damagevalue");
		byte damagevalue = 1;
		if (testdamage != null) {
			damagevalue = (byte) Integer.parseInt(zones.getString(zonename + ".damagevalue"));
		}
		
		int x1 = zones.getInt(attachedzone + ".p1.x");
		int y1 = zones.getInt(attachedzone + ".p1.y");
		int z1 = zones.getInt(attachedzone + ".p1.z");
		int x2 = zones.getInt(attachedzone + ".p2.x");
		int y2 = zones.getInt(attachedzone + ".p2.y");
		int z2 = zones.getInt(attachedzone + ".p2.z");
		World w = Bukkit.getWorld(zones.getString(attachedzone + ".world"));
		
		Location l = new Location(w, 0, 0, 0);
		
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
		
		FileConfiguration blocksyaml = az.getYaml().getBlocks();
		int x = 0;
		int y = 0;
		int z = 0;
		int a = 0;
		int b = 0;
		c = 0;
		int counter = 0;
		while (c < zvals.size()) {
			z = zvals.get(c);
			while (b < xvals.size()) {
				x = xvals.get(b);
				while (a < yvals.size()) {
					y = yvals.get(a);
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					Block block = w.getBlockAt(l);
					
					
					
					blocksyaml.set(attachedzone + ".block" + counter + ".x", block.getX());
					blocksyaml.set(attachedzone + ".block" + counter + ".y", block.getY());
					blocksyaml.set(attachedzone + ".block" + counter + ".z", block.getZ());
					blocksyaml.set(attachedzone + ".block" + counter + ".id", block.getTypeId());
					blocksyaml.set(attachedzone + ".block" + counter + ".data", block.getData());
					
					cblocks.add(block);
					
					
					a++;
					counter++;
				}
				b++;
				a = 0;
			}
			c++;
			b = 0;
		}
		
		blocksyaml.set(attachedzone + ".size", counter);
		blocksyaml.set(attachedzone + ".parentzone", zonename);
		
		int count = 0;
		while (count < cblocks.size()) {
			Block block = cblocks.get(count);
			if (!fpid.contains(block.getTypeId())) {
				block.setTypeId(blockid);
				block.setData(damagevalue);
				cblocks.remove(count);
			}
			count++;
		}
		
		count = 0;
		while (count < cblocks.size()) {
			Block block = cblocks.get(count);
			block.setTypeId(blockid);
			block.setData(damagevalue);
			count++;
		}
		
		
		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
			public void run() {
				az.getYaml().saveBlocks();
			}
		}, 1L);
		

		Restore(duration);

	}
	
	public void Restore(Long duration) {
		fpid = az.getFpid();
		if (duration >= 0) {
			az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
				public void run() {

					/*
					World w = Bukkit.getWorld(az.getYaml().getZones().getString(attachedzone + ".world"));
					FileConfiguration blocksyaml = az.getYaml().getBlocks();
					int size = blocksyaml.getInt(attachedzone + ".size");
					int d = 0;
					while (d < size) {
						int x = blocksyaml.getInt(attachedzone + ".block" + d + ".x");
						int y = blocksyaml.getInt(attachedzone + ".block" + d + ".y");
						int z = blocksyaml.getInt(attachedzone + ".block" + d + ".z");
						Block block = w.getBlockAt(x, y, z);
						block.setTypeId(blocksyaml.getInt(attachedzone + ".block" + d + ".id"));
						block.setData((byte) blocksyaml.getInt(attachedzone + ".block" + d + ".data"));
						d++;
					}
					 */

					
					
					
					World w = Bukkit.getWorld(az.getYaml().getZones().getString(attachedzone + ".world"));
					FileConfiguration blocksyaml = az.getYaml().getBlocks();
					int size = blocksyaml.getInt(attachedzone + ".size");
					int d = 0;
					while (d < size) {
						int bid = blocksyaml.getInt(attachedzone + ".block" + d + ".id");
						if (fpid.contains(bid)) {
							int x = blocksyaml.getInt(attachedzone + ".block" + d + ".x");
							int y = blocksyaml.getInt(attachedzone + ".block" + d + ".y");
							int z = blocksyaml.getInt(attachedzone + ".block" + d + ".z");
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) blocksyaml.getInt(attachedzone + ".block" + d + ".data"));
						}
						d++;
					}
					
					d = 0;
					while (d < size) {
						int bid = blocksyaml.getInt(attachedzone + ".block" + d + ".id");
						if (!fpid.contains(bid)) {
							int x = blocksyaml.getInt(attachedzone + ".block" + d + ".x");
							int y = blocksyaml.getInt(attachedzone + ".block" + d + ".y");
							int z = blocksyaml.getInt(attachedzone + ".block" + d + ".z");
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) blocksyaml.getInt(attachedzone + ".block" + d + ".data"));
						}
						d++;
					}
					
					blocksyaml.set(attachedzone, null);					
					action.setBlockPlaceInactive(zonename);
					
					
				}
			}, duration);
		}
	}
	
	
	
	public void forceRestore(ActionZones acz, Action a) {
		az = acz;
		action = a;
		
		Iterator<String> it = az.getYaml().getBlocks().getKeys(false).iterator();
		while (it.hasNext()) {
			attachedzone = it.next().toString();
			zonename = az.getYaml().getBlocks().getString(attachedzone + ".parentzone");
			Restore(0L);		
			action.setBlockPlaceInactive(zonename);
		}  
		
	}
	
}
