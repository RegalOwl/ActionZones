package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

public class Placeblocks extends Action {

	
	private ArrayList<Integer> xvals = new ArrayList<Integer>();
	private ArrayList<Integer> yvals = new ArrayList<Integer>();
	private ArrayList<Integer> zvals = new ArrayList<Integer>();
	
	private String zonename;

	
	private String attachedzone;
	
	private ArrayList<Integer> fpid = new ArrayList<Integer>();
	private ArrayList<Block> cblocks = new ArrayList<Block>();
	
	public void runAction() {
		zone.setLock(true);
		zonename = zone.getName();

		fpid.clear();
    	fpid.add(0);
		fpid.add(1);
		fpid.add(2);
		fpid.add(3);
		fpid.add(4);
		fpid.add(5);
		fpid.add(7);
		fpid.add(12);
		fpid.add(13);
		fpid.add(14);
		fpid.add(15);
		fpid.add(16);
		fpid.add(17);
		fpid.add(19);
		fpid.add(20);
		fpid.add(21);
		fpid.add(22);
		fpid.add(23);
		fpid.add(24);
		fpid.add(29);
		fpid.add(30);
		fpid.add(33);
		fpid.add(35);
		fpid.add(41);
		fpid.add(42);
		fpid.add(45);
		fpid.add(46);
		fpid.add(47);
		fpid.add(48);
		fpid.add(49);
		fpid.add(52);
		fpid.add(53);
		fpid.add(54);
		fpid.add(56);
		fpid.add(57);
		fpid.add(58);
		fpid.add(61);
		fpid.add(67);
		fpid.add(73);
		fpid.add(79);
		fpid.add(80);
		fpid.add(82);
		fpid.add(84);
		fpid.add(85);
		fpid.add(86);
		fpid.add(87);
		fpid.add(88);
		fpid.add(89);
		fpid.add(91);
		fpid.add(98);
		fpid.add(99);
		fpid.add(100);
		fpid.add(101);
		fpid.add(102);
		fpid.add(103);
		fpid.add(107);
		fpid.add(108);
		fpid.add(109);
		fpid.add(110);
		fpid.add(112);
		fpid.add(113);
		fpid.add(114);
		fpid.add(116);
		fpid.add(121);
		fpid.add(123);
		fpid.add(124);
		fpid.add(126);
		fpid.add(128);
		fpid.add(129);
		fpid.add(130);
		
		
		if (zonename == null) {
			return;
		}

		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		//Returns if there is no attached zone.
		attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zonename + " needs to have a zone attached in order to support block creation.", "actionzones.admin");
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
		
		FileConfiguration blocksyaml = az.getYamlHandler().gFC("blocks");
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
				az.getYamlHandler().saveYaml("blocks");
			}
		}, 1L);
		

		Restore(duration);

	}
	
	public void Restore(Long duration) {
		fpid.clear();
    	fpid.add(0);
		fpid.add(1);
		fpid.add(2);
		fpid.add(3);
		fpid.add(4);
		fpid.add(5);
		fpid.add(7);
		fpid.add(12);
		fpid.add(13);
		fpid.add(14);
		fpid.add(15);
		fpid.add(16);
		fpid.add(17);
		fpid.add(19);
		fpid.add(20);
		fpid.add(21);
		fpid.add(22);
		fpid.add(23);
		fpid.add(24);
		fpid.add(29);
		fpid.add(30);
		fpid.add(33);
		fpid.add(35);
		fpid.add(41);
		fpid.add(42);
		fpid.add(45);
		fpid.add(46);
		fpid.add(47);
		fpid.add(48);
		fpid.add(49);
		fpid.add(52);
		fpid.add(53);
		fpid.add(54);
		fpid.add(56);
		fpid.add(57);
		fpid.add(58);
		fpid.add(61);
		fpid.add(67);
		fpid.add(73);
		fpid.add(79);
		fpid.add(80);
		fpid.add(82);
		fpid.add(84);
		fpid.add(85);
		fpid.add(86);
		fpid.add(87);
		fpid.add(88);
		fpid.add(89);
		fpid.add(91);
		fpid.add(98);
		fpid.add(99);
		fpid.add(100);
		fpid.add(101);
		fpid.add(102);
		fpid.add(103);
		fpid.add(107);
		fpid.add(108);
		fpid.add(109);
		fpid.add(110);
		fpid.add(112);
		fpid.add(113);
		fpid.add(114);
		fpid.add(116);
		fpid.add(121);
		fpid.add(123);
		fpid.add(124);
		fpid.add(126);
		fpid.add(128);
		fpid.add(129);
		fpid.add(130);
		if (duration >= 0) {
			az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
				public void run() {

					
					
					World w = Bukkit.getWorld(az.getYamlHandler().gFC("zones").getString(attachedzone + ".world"));
					FileConfiguration blocksyaml = az.getYamlHandler().gFC("blocks");
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
					zone.setLock(false);
					
					
				}
			}, duration);
		}
	}
	
	
	
	public void forceRestore() {
		Iterator<String> it = az.getYamlHandler().gFC("blocks").getKeys(false).iterator();
		while (it.hasNext()) {
			attachedzone = it.next().toString();
			zonename = az.getYamlHandler().gFC("blocks").getString(attachedzone + ".parentzone");
			Restore(0L);		
			zone.setLock(false);
		}  
		
	}
	
}
