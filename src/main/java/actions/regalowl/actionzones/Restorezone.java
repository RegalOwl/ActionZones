package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import regalowl.databukkit.DataBukkit;
import regalowl.databukkit.QueryResult;
import regalowl.databukkit.SQLRead;
import regalowl.databukkit.SQLWrite;

public class Restorezone extends Action {

	
	private String zonename;
	private String attachedzone;
	private DataBukkit db;
	private SQLWrite sw;
	private SQLRead sr;
	
	private ArrayList<Integer> fpid = new ArrayList<Integer>();
	private ArrayList<Integer> spn = new ArrayList<Integer>();
	
	
	private ArrayList<Integer> xvals = new ArrayList<Integer>();
	private ArrayList<Integer> yvals = new ArrayList<Integer>();
	private ArrayList<Integer> zvals = new ArrayList<Integer>();
	
	
	private ArrayList<Integer> ida = new ArrayList<Integer>();
	private ArrayList<Integer> dataa = new ArrayList<Integer>();
	private ArrayList<Integer> xa = new ArrayList<Integer>();
	private ArrayList<Integer> ya = new ArrayList<Integer>();
	private ArrayList<Integer> za = new ArrayList<Integer>();
	

	
	public void storeZone(ActionZones acz, String zone, Player player) {
		db = az.getDataBukkit();
		sw = db.getSQLWrite();
		sr = db.getSQLRead();
		sw.executeSQL("CREATE TABLE IF NOT EXISTS zones (ZONENAME TEXT, WORLD TEXT, ID INT, DATA INT, X INT, Y INT, Z INT)");
        removeFromStorage(az, zone);
		FileConfiguration zones = az.getYamlHandler().gFC("zones");

		String testzone = zones.getString(zone);
		if (testzone == null) {return;}
		
		int x1 = zones.getInt(zone + ".p1.x");
		int y1 = zones.getInt(zone + ".p1.y");
		int z1 = zones.getInt(zone + ".p1.z");
		int x2 = zones.getInt(zone + ".p2.x");
		int y2 = zones.getInt(zone + ".p2.y");
		int z2 = zones.getInt(zone + ".p2.z");
		World w = Bukkit.getWorld(zones.getString(zone + ".world"));
		
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
		

		int x = 0;
		int y = 0;
		int z = 0;
		int a = 0;
		int b = 0;
		c = 0;

		ArrayList<String> statements = new ArrayList<String>();
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
					statements.add("INSERT INTO zones (ZONENAME, WORLD, ID, DATA, X, Y, Z)"
				            + " VALUES ('" + zone + "','" + zones.getString(zone + ".world") + "','"
				        + block.getTypeId() + "','" + block.getData() + "','" + block.getX() + "','" + block.getY()
				        + "','" + block.getZ() + "')");
					
					a++;
				}
				b++;
				a = 0;
			}
			c++;
			b = 0;
		}
		sw.executeSQL(statements);
	}
	
	
	public void Restore(Long duration, ActionZones acz, String zonen) {
		db = az.getDataBukkit();
		sw = db.getSQLWrite();
		sr = db.getSQLRead();
		
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
		
		
		
		
		
		az = acz;
		zonename = zonen;

		spn.clear();
		
		

		

			az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
				public void run() {
	
						QueryResult result = sr.aSyncSelect("SELECT * FROM zones WHERE ZONENAME = " + "'" + zonename + "'");
						while (result.next()) {
				                ida.add(result.getInt("ID"));
				                dataa.add(result.getInt("DATA"));
				                xa.add(result.getInt("X"));
				                ya.add(result.getInt("Y"));
				                za.add(result.getInt("Z"));  
						}
						result.close();
					World w = Bukkit.getWorld(az.getYamlHandler().gFC("zones").getString(zonename + ".world"));
					int size = ida.size();
					
					
					//Deletes things that might drop in advance.
					int d = 0;
					while (d < size) {
						int x = xa.get(d);
						int y = ya.get(d);
						int z = za.get(d);
						Block block = w.getBlockAt(x, y, z);
						Chunk cc = block.getChunk();
						if (!cc.isLoaded()) {
							cc.load();
						}
						if (!fpid.contains(block.getTypeId())) {
							block.setTypeId(0);
						}
						d++;
					}
					
					
					
					d = 0;
					while (d < size) {
						int bid = ida.get(d);
						if (fpid.contains(bid)) {
							int x = xa.get(d);
							int y = ya.get(d);
							int z = za.get(d);
							int data = dataa.get(d);
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) data);
						} else {
							spn.add(d);
						}
						d++;
					}
				
					d = 0;
					while (d < size) {
						if (spn.contains(d)) {
							int x = xa.get(d);
							int y = ya.get(d);
							int z = za.get(d);
							int bid = ida.get(d);
							int data = dataa.get(d);
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) data);
						}
						d++;
					}
				
				}
			}, duration);
		
	}
	
	
	
	public void runAction() {
		spn.clear();
		
		FileConfiguration zones = az.getYamlHandler().gFC("zones");
		
		//Returns if there is no attached zone.
		attachedzone = zones.getString(zonename + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("�cZone " + zonename + " needs to have a zone attached in order to support zone restoration.", "actionzones.admin");
			return;
		}



			QueryResult result = sr.aSyncSelect("SELECT * FROM zones WHERE ZONENAME = " + "'" + attachedzone + "'");
						while (result.next()) {
				                ida.add(result.getInt("ID"));
				                dataa.add(result.getInt("DATA"));
				                xa.add(result.getInt("X"));
				                ya.add(result.getInt("Y"));
				                za.add(result.getInt("Z"));  
						}
			            result.close();

					
					

					World w = Bukkit.getWorld(az.getYamlHandler().gFC("zones").getString(attachedzone + ".world"));
					int size = ida.size();
					
					
					//Deletes things that might drop in advance.
					int d = 0;
					while (d < size) {
						int x = xa.get(d);
						int y = ya.get(d);
						int z = za.get(d);
						Block block = w.getBlockAt(x, y, z);
						Chunk cc = block.getChunk();
						if (!cc.isLoaded()) {
							cc.load();
						}
						if (!fpid.contains(block.getTypeId())) {
							block.setTypeId(0);
						}
						d++;
					}
					
					
					d = 0;
					while (d < size) {
						int bid = ida.get(d);
						if (fpid.contains(bid)) {
							int x = xa.get(d);
							int y = ya.get(d);
							int z = za.get(d);
							int data = dataa.get(d);
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) data);
						} else {
							spn.add(d);
						}
						d++;
					}
					
					d = 0;
					while (d < size) {
						if (spn.contains(d)) {
							int x = xa.get(d);
							int y = ya.get(d);
							int z = za.get(d);
							int bid = ida.get(d);
							int data = dataa.get(d);
							Block block = w.getBlockAt(x, y, z);
							Chunk cc = block.getChunk();
							if (!cc.isLoaded()) {
								cc.load();
							}
							block.setTypeId(bid);
							block.setData((byte) data);
						}
						d++;
					}
				
	}
	
	
	
	
	public void removeFromStorage(ActionZones atz, String zonename) {
		az = atz;
		sw.executeSQL("DELETE FROM zones WHERE ZONENAME = " + "'" + zonename + "'");
	}

	
}
