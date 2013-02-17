package regalowl.actionzones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class Zone {

	private HashMap<Player, ArrayList<Integer>> zh;
	
	private ArrayList<String> zonekeys = new ArrayList<String>();
	private int nzones;
	
	private ArrayList<Integer> p1x = new ArrayList<Integer>();
	private ArrayList<Integer> p1y = new ArrayList<Integer>();
	private ArrayList<Integer> p1z = new ArrayList<Integer>();
	private ArrayList<Integer> p2x = new ArrayList<Integer>();
	private ArrayList<Integer> p2y = new ArrayList<Integer>();
	private ArrayList<Integer> p2z = new ArrayList<Integer>();
	private ArrayList<String> zoneworld = new ArrayList<String>();
	
	
	private long zoneinterval;
	private int zonetaskid1;
	private int zonetaskid2;
	private int zonetaskid3;
	private boolean zonecheckactive;
	
	private String name;
	private String newname;
	
	private Player p;
	private ActionZones az;
	private Action a;

	private OnlinePlayers op;
	
	public int getzonekeysSize() {
		return zonekeys.size();
	}
	
	public String getzonekeys(int index) {
		return zonekeys.get(index);
	}
	

	
	public void clearAll() {
		zh.clear();
    	zonekeys.clear();
    	zoneworld.clear();
    	p1x.clear();
    	p1y.clear();
    	p1z.clear();
    	p2x.clear();
    	p2y.clear();
    	p2z.clear();
	}

	/**
	 * 
	 * 
	 * Setter for inZone()
	 * 
	 */
	public void setinZone(Player player) {
		p = player;
	}
	
	
	/**
	 * 
	 * 
	 * Setter for setP1() and setP2()
	 * 
	 */
	public void setsPoint(String n, Player player) {
		name = n;
		p = player;
	}
	
	
	/**
	 * 
	 * 
	 * Setter for renameZone()
	 * 
	 */
	public void setrenZone(String n, String nn) {
		name = n;
		newname = nn;
	}
	
	/**
	 * 
	 * 
	 * Setter for removeZone()
	 * 
	 */
	public void setrZone(String n) {
		name = n;
	}
	
	
	
	/**
	 * 
	 * 
	 * Constructor for a new zone on server start.
	 * 
	 */
	Zone(ActionZones actionz, Action action, OnlinePlayers onp) {
		az = actionz;
		a = action;
		op = onp;

		zh = new HashMap<Player, ArrayList<Integer>>();
		
		zoneinterval = 3L;
		
		Iterator<String> it = az.getYaml().getZones().getKeys(false).iterator();
		int counter = 0;
		while (it.hasNext()) {   			
			Object element2 = it.next();
			String ele = element2.toString(); 
			zonekeys.add(ele);
			counter++;
		}
		
		nzones = zonekeys.size();

		
		counter = 0;
		FileConfiguration sh = az.getYaml().getZones();
		while (counter < nzones) {
			String nameshop = zonekeys.get(counter);
			zoneworld.add(sh.getString(nameshop + ".world"));
			p1x.add(sh.getInt(nameshop + ".p1.x"));
			p1y.add(sh.getInt(nameshop + ".p1.y"));
			p1z.add(sh.getInt(nameshop + ".p1.z"));
			p2x.add(sh.getInt(nameshop + ".p2.x"));
			p2y.add(sh.getInt(nameshop + ".p2.y"));
			p2z.add(sh.getInt(nameshop + ".p2.z"));
			counter++;
		}
		
		startzoneCheck();

	}
	

	
	
	/**
	 * 
	 * 
	 * This function is run every few seconds to check which players are in which zones.
	 * 
	 */
	public void zoneThread(int thread) {
		ArrayList<Player> players = op.getOnlinePlayers(thread);
		int c = 0;
		while (c < players.size()) {			
			p = players.get(c);

			ArrayList<Integer> pzones = zh.get(p);
			if (pzones == null) {
				pzones = new ArrayList<Integer>();
			}
			
			ArrayList<Integer> znum = inZones();

			//Actions
				int count = 0;
				while (count < znum.size()) {
					int cz = znum.get(count);

					//Adds the previous zones to zonehistory storage zh.
					if (!pzones.contains(cz)) {
						pzones.add(cz);
						a.runAction(zonekeys.get(cz), p);
					}
					count++;
				}
				zh.put(p, pzones);
			
			//Exit Actions
				count = 0;
				while (count < pzones.size()) {
					int cpz = pzones.get(count);
					if (!znum.contains(cpz)) {
						pzones.remove(count);
						a.runExitAction(zonekeys.get(cpz), p);
					}
					count++;
				}
				zh.put(p, pzones);
			c++;
		}
	}



	
	
	/**
	 * 
	 * 
	 * This function checks if the player is in the specified range of zones.  Oh yeah.
	 * 
	 */
	
	public ArrayList<Integer> inZones() {
		ArrayList<Integer> inzone = new ArrayList<Integer>();
		for (int counter = 0; counter < nzones; counter++) {	
			if (p.getWorld().getName().equalsIgnoreCase(zoneworld.get(counter))) {
				int locx = p.getLocation().getBlockX();
				int rangex = Math.abs(p1x.get(counter) - p2x.get(counter));
				if (Math.abs(locx - p1x.get(counter)) <= rangex && Math.abs(locx - p2x.get(counter)) <= rangex) {	
					int locz = p.getLocation().getBlockZ();
					int rangez = Math.abs(p1z.get(counter) - p2z.get(counter));
					if (Math.abs(locz - p1z.get(counter)) <= rangez && Math.abs(locz - p2z.get(counter)) <= rangez) {					
						int locy = p.getLocation().getBlockY();
						int rangey = Math.abs(p1y.get(counter) - p2y.get(counter));
						if (Math.abs(locy - p1y.get(counter)) <= rangey && Math.abs(locy - p2y.get(counter)) <= rangey) {
							inzone.add(counter);
						}
					}			
				}
			}
		}	
		return inzone;
	}
	
	
	
	/**
	 * 
	 * 
	 * This function checks if something other than a player is in the zone.  Oh yeah.
	 * 
	 */
	
	public ArrayList<Integer> inZonesOther(int locx, int locy, int locz, World w) {
		ArrayList<Integer> inzone = new ArrayList<Integer>();
		int counter = 0;
		while (counter < nzones) {	
			if (w.getName().equalsIgnoreCase(zoneworld.get(counter))) {
				int rangex = Math.abs(p1x.get(counter) - p2x.get(counter));
				if (Math.abs(locx - p1x.get(counter)) <= rangex && Math.abs(locx - p2x.get(counter)) <= rangex) {	
					int rangez = Math.abs(p1z.get(counter) - p2z.get(counter));
					if (Math.abs(locz - p1z.get(counter)) <= rangez && Math.abs(locz - p2z.get(counter)) <= rangez) {					
						int rangey = Math.abs(p1y.get(counter) - p2y.get(counter));
						if (Math.abs(locy - p1y.get(counter)) <= rangey && Math.abs(locy - p2y.get(counter)) <= rangey) {
							inzone.add(counter);
						}
					}			
				}
			}
			counter++;
		}	
		return inzone;
	}

	

	
	/**
	 * 
	 * 
	 * This function sets the zone's first point.
	 * 
	 */
	public void setP1(){
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		String w = p.getWorld().getName();
		FileConfiguration zone = az.getYaml().getZones();
		zone.set(name + ".world", w);
		zone.set(name + ".p1.x", x);
		zone.set(name + ".p1.y", y);
		zone.set(name + ".p1.z", z);
		
		if (zonekeys.indexOf(name) == -1) {
			zonekeys.add(name);
		}
		
		nzones = zonekeys.size();
		int zonenumber = zonekeys.indexOf(name);

			if (p1x.size() < nzones) {
				zoneworld.add(zonenumber, w);
				p1x.add(zonenumber, x);
				p1y.add(zonenumber, y);
				p1z.add(zonenumber, z);
				p2x.add(zonenumber, x);
				p2y.add(zonenumber, y);
				p2z.add(zonenumber, z);
				zone.set(name + ".p2.x", x);
				zone.set(name + ".p2.y", y);
				zone.set(name + ".p2.z", z);
				zone.set(name + ".action", "null");
				zone.set(name + ".exit-action", "null");
				zone.set(name + ".active", true);
			} else {
				zoneworld.set(zonenumber, w);
				p1x.set(zonenumber, x);
				p1y.set(zonenumber, y);
				p1z.set(zonenumber, z);
			}
	}
	/**
	 * 
	 * 
	 * This function sets the zone's second point.
	 * 
	 */
	public void setP2(){
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		String w = p.getWorld().getName();
		FileConfiguration zone = az.getYaml().getZones();
		zone.set(name + ".world", w);
		zone.set(name + ".p2.x", x);
		zone.set(name + ".p2.y", y);
		zone.set(name + ".p2.z", z);
		if (zonekeys.indexOf(name) == -1) {
			zonekeys.add(name);
		}
		
		nzones = zonekeys.size();
		int zonenumber = zonekeys.indexOf(name);

		
		if (p2x.size() < nzones) {
			zoneworld.add(zonenumber, w);
			p2x.add(zonenumber, x);
			p2y.add(zonenumber, y);
			p2z.add(zonenumber, z);
			p1x.add(zonenumber, x);
			p1y.add(zonenumber, y);
			p1z.add(zonenumber, z);
			zone.set(name + ".p1.x", x);
			zone.set(name + ".p1.y", y);
			zone.set(name + ".p1.z", z);
			zone.set(name + ".action", "null");
			zone.set(name + ".exit-action", "null");
			zone.set(name + ".active", true);
		} else {
			zoneworld.set(zonenumber, w);
			p2x.set(zonenumber, x);
			p2y.set(zonenumber, y);
			p2z.set(zonenumber, z);
		}
		


	}
	
	public void removeZone() {
		
		az.getYaml().getZones().set(name, null);
		int zonenumber = zonekeys.indexOf(name);
		zonekeys.remove(zonenumber);
			p1x.remove(zonenumber);
			p1y.remove(zonenumber);
			p1z.remove(zonenumber);
			p2x.remove(zonenumber);
			p2y.remove(zonenumber);
			p2z.remove(zonenumber);
			zoneworld.remove(zonenumber);


		nzones = nzones - 1;
	}
	
	
	public void renameZone() {
		FileConfiguration zonesfile = az.getYaml().getZones();
		zonesfile.set(newname + ".world", zonesfile.get(name + ".world"));
		zonesfile.set(newname + ".p1.x", zonesfile.get(name + ".p1.x"));
		zonesfile.set(newname + ".p1.y", zonesfile.get(name + ".p1.y"));
		zonesfile.set(newname + ".p1.z", zonesfile.get(name + ".p1.z"));
		zonesfile.set(newname + ".p2.x", zonesfile.get(name + ".p2.x"));
		zonesfile.set(newname + ".p2.y", zonesfile.get(name + ".p2.y"));
		zonesfile.set(newname + ".p2.z", zonesfile.get(name + ".p2.z"));
		zonesfile.set(name, null);

		zonekeys.clear();
		Iterator<String> it = zonesfile.getKeys(false).iterator();
		while (it.hasNext()) {   			
			Object element2 = it.next();
			String ele = element2.toString(); 
			zonekeys.add(ele);
		}

	}
	
	//public ArrayList<String> listZones() {
	//	return zonekeys;
	//}
	
	public int getZoneID(String zonename) {
		return zonekeys.indexOf(zonename);
	}
	
	public String getZone(int zonenumber) {
		String zonename = null;
		if (zonenumber > -1) {
			zonename = zonekeys.get(zonenumber);
		}
		return zonename;
	}
	
	public String getZoneNames() {
		return zonekeys.toString();
	}
	
	
	public ArrayList<String> getZoneKeys() {
		return zonekeys;
	}
	
	
    //Threading related functions.
    public void startzoneCheck() {
    	
		zonetaskid1 = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
		    public void run() {
		    	zoneThread(1);
		    	zonecheckactive = true;
		    	
		    }
		}, zoneinterval, zoneinterval);
    	
		
		zonetaskid2 = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
		    public void run() {
		    	zoneThread(2);
		    	zonecheckactive = true;
		    	
		    }
		}, zoneinterval + 1L, zoneinterval);
		
		zonetaskid3 = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
		    public void run() {
		    	zoneThread(3);
		    	zonecheckactive = true;
		    	
		    }
		}, zoneinterval + 2L, zoneinterval);
		
    }
    
    
    public void stopzoneCheck() {
    	az.getServer().getScheduler().cancelTask(zonetaskid1);
    	az.getServer().getScheduler().cancelTask(zonetaskid2);
    	az.getServer().getScheduler().cancelTask(zonetaskid3);
    	zonecheckactive = false;
    }
    
    public boolean zonesActive() {
    	return zonecheckactive;
    }
    
    
    public long getzoneInterval() {
    	return zoneinterval;
    }
	
    public void setzoneInterval(long interval) {
    	
    	if (interval < 3) {
    		interval = 3L;
    	}
    	if (zonecheckactive) {
        	stopzoneCheck();
        	zoneinterval = interval;
        	startzoneCheck();
    	} else {
        	zoneinterval = interval;
    	}

    }
	
	
	
	
	

	
	
	
	
	
	
	
	
}
