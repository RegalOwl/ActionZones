package regalowl.actionzones;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.sql.*;

public class RestoreZones {

	
	private ActionZones az;
	private String zone;
	private String attachedzone;
	
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
	
	
	private ArrayList<ArrayList<String>> statements = new ArrayList<ArrayList<String>>();
	private Connection connect;
	private int scounter;
	
	private int savecompletetaskid;
	private int totalblocks;
	private Player p;
	private ArrayList<Integer> blocksprocessed = new ArrayList<Integer>();
	
	private String username;
	private String password;
	private int port;
	private String host;
	private String database;
	
	private int threadcount;
	private boolean abort;
	
	public void storeZone(ActionZones acz, String zone, Player player) {
		p = player;
		az = acz;
		abort = false;
		
		FileConfiguration config = az.getYaml().getConfig();
		username = config.getString("sql-connection.username");
		password = config.getString("sql-connection.password");
		port = config.getInt("sql-connection.port");
		host = config.getString("sql-connection.host");
		database = config.getString("sql-connection.database");
		threadcount = config.getInt("swapzones.sql-threads");
		
		
        try {
			connect = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement state = connect.createStatement();
			state.execute("CREATE TABLE IF NOT EXISTS zones (ZONENAME TEXT, WORLD TEXT, ID INT, DATA INT, X INT, Y INT, Z INT)");
            state.close();
            connect.close();
		} catch (SQLException e) {
			Bukkit.broadcast(ChatColor.RED + "SQL connection failed.  Check your config settings.", "actionzones.admin");
			e.printStackTrace();
			return;
		}
		
        removeFromStorage(az, zone);
		
		

		

		FileConfiguration zones = az.getYaml().getZones();

		String testzone = zones.getString(zone);
		if (testzone == null) {
			return;
		}
		
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
		
		int co = 0;
		while (co < threadcount) {
			ArrayList<String> sarray = new ArrayList<String>();
			statements.add(co, sarray);
			co++;
		}

		int x = 0;
		int y = 0;
		int z = 0;
		int a = 0;
		int b = 0;
		totalblocks = 0;
		c = 0;
		int threadselect = 0;
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
					
					ArrayList<String> cstatement = statements.get(threadselect);
					if (cstatement == null) {
						cstatement = new ArrayList<String>();
					}
					cstatement.add("Insert Into zones (ZONENAME, WORLD, ID, DATA, X, Y, Z)"
				            + " Values ('" + zone + "','" + zones.getString(zone + ".world") + "','"
				        + block.getTypeId() + "','" + block.getData() + "','" + block.getX() + "','" + block.getY()
				        + "','" + block.getZ() + "')");
					
					statements.set(threadselect, cstatement);
					
					totalblocks++;
					a++;
					threadselect++;
					if (threadselect >= statements.size()) {
						threadselect = 0;
					}
				}
				b++;
				a = 0;
			}
			c++;
			b = 0;
		}

		blocksprocessed.clear();
		scounter = 0;
		while (scounter < statements.size()) {		
			RestoreZonesThread szt = new RestoreZonesThread();
			szt.swapZonesThread(az, this, statements.get(scounter), scounter);
			blocksprocessed.add(0);
			scounter++;
		}

		
		savecompletetaskid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				if (abort) {
					if (p != null) {
						p.sendMessage(ChatColor.RED + "An error has occurred!  Save aborted.");
						p.sendMessage(ChatColor.RED + "Lower the SQL thread count and try again.");
					}
					saveComplete();
				}
				int totalprocessed = 0;
				for (int i = 0; i < blocksprocessed.size(); i++) {
					totalprocessed = totalprocessed + blocksprocessed.get(i);
				}
				int blocksremaining = totalblocks - totalprocessed;
				
				if (blocksremaining == 0) {
					saveComplete();
					if (p != null) {
						p.sendMessage(ChatColor.GREEN + "Save complete!");
					}
				} else {
					if (p != null) {
						p.sendMessage(ChatColor.GREEN + "Saving Zone: " + blocksremaining + " blocks remaining.");
					}
				}
			}
		}, 0L, 200L);
		
		
	}
	
	
	public void Restore(Long duration, ActionZones acz, String zonen) {

		az = acz;
		zone = zonen;
		fpid = az.getFpid();
		spn.clear();
		
		
		FileConfiguration config = az.getYaml().getConfig();
		username = config.getString("sql-connection.username");
		password = config.getString("sql-connection.password");
		port = config.getInt("sql-connection.port");
		host = config.getString("sql-connection.host");
		database = config.getString("sql-connection.database");
		
		if (duration >= 0) {
			az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
				public void run() {
					
					
					try {
						connect = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
						Statement state = connect.createStatement();	
						ResultSet result = state.executeQuery("SELECT * FROM zones WHERE ZONENAME = " + "'" + zone + "'");
						while (result.next()) {
				                ida.add(result.getInt("ID"));
				                dataa.add(result.getInt("DATA"));
				                xa.add(result.getInt("X"));
				                ya.add(result.getInt("Y"));
				                za.add(result.getInt("Z"));  
						}
			            result.close();
			            state.close();
			            connect.close();
					} catch (SQLException e) {
						Bukkit.broadcast(ChatColor.RED + "SQL connection failed.  Check your config settings.", "actionzones.admin");
						e.printStackTrace();
						return;
					}
					
					

					World w = Bukkit.getWorld(az.getYaml().getZones().getString(zone + ".world"));
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
	}
	
	
	
	public void RestoreAction(ActionZones acz, String zonen) {

		az = acz;
		zone = zonen;
		fpid = az.getFpid();
		spn.clear();
		
		FileConfiguration zones = az.getYaml().getZones();
		
		//Returns if there is no attached zone.
		attachedzone = zones.getString(zone + ".zone");
		if (attachedzone == null) {
			Bukkit.broadcast("§cZone " + zone + " needs to have a zone attached in order to support zone restoration.", "actionzones.admin");
			return;
		}
		
		FileConfiguration config = az.getYaml().getConfig();
		username = config.getString("sql-connection.username");
		password = config.getString("sql-connection.password");
		port = config.getInt("sql-connection.port");
		host = config.getString("sql-connection.host");
		database = config.getString("sql-connection.database");

					
					
					try {
						connect = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
						Statement state = connect.createStatement();	
						ResultSet result = state.executeQuery("SELECT * FROM zones WHERE ZONENAME = " + "'" + attachedzone + "'");
						while (result.next()) {
				                ida.add(result.getInt("ID"));
				                dataa.add(result.getInt("DATA"));
				                xa.add(result.getInt("X"));
				                ya.add(result.getInt("Y"));
				                za.add(result.getInt("Z"));  
						}
			            result.close();
			            state.close();
			            connect.close();
					} catch (SQLException e) {
						Bukkit.broadcast(ChatColor.RED + "SQL connection failed.  Check your config settings.", "actionzones.admin");
						e.printStackTrace();
						return;
					}
					
					

					World w = Bukkit.getWorld(az.getYaml().getZones().getString(attachedzone + ".world"));
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
		
		FileConfiguration config = az.getYaml().getConfig();
		username = config.getString("sql-connection.username");
		password = config.getString("sql-connection.password");
		port = config.getInt("sql-connection.port");
		host = config.getString("sql-connection.host");
		database = config.getString("sql-connection.database");
		
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement state = connect.createStatement();	
			state.executeUpdate("DELETE FROM zones WHERE ZONENAME = " + "'" + zonename + "'");
            state.close();
            connect.close();
		} catch (SQLException e) {
			Bukkit.broadcast(ChatColor.RED + "SQL connection failed.  Check your config settings.", "actionzones.admin");
			e.printStackTrace();
			return;
		}
	}
	
	public void saveComplete() {
		az.getServer().getScheduler().cancelTask(savecompletetaskid);
	}

	public void setBlocksProcessed(int threadid, int blocksp) {
		blocksprocessed.set(threadid, blocksp);
	}
	
	public void abortSave(){
		abort = true;
	}
	
}
