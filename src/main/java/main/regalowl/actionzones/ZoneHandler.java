package regalowl.actionzones;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;


public class ZoneHandler {

	private BukkitTask task1;
	private BukkitTask task2;
	private BukkitTask task3;
	private boolean zoneCheckActive;
	private ActionZones az;
	private ConcurrentHashMap<String,ActionZone> aZones = new ConcurrentHashMap<String,ActionZone>();
	private ConcurrentHashMap<String,Zone> zones = new ConcurrentHashMap<String,Zone>();

	ZoneHandler() {
		az = ActionZones.az;
		FileConfiguration zonesConfig = az.getYamlHandler().gFC("zones");
		Iterator<String> it = az.getYamlHandler().gFC("zones").getKeys(false).iterator();
		while (it.hasNext()) { 
			String name = it.next().toString();
			String type = zonesConfig.getString(name + ".type");
			if (type != null && type.equalsIgnoreCase("action")) {
				ActionZone actionZone = new ActionZone(name);
				aZones.put(name, actionZone);
				zones.put(name, actionZone);
			} else {
				zones.put(name, new Zone(name));
			}
		}
		startZoneCheck();
	}
	

    public void startZoneCheck() {
    	zoneCheckActive = true;
		task1 = az.getServer().getScheduler().runTaskTimer(az, new Runnable() {
		    public void run() {
				for (ActionZone z:aZones.values()) {			
					z.update(1);
				}
		    }
		}, 3L, 3L);
		task2 = az.getServer().getScheduler().runTaskTimer(az, new Runnable() {
		    public void run() {
				for (ActionZone z:aZones.values()) {			
					z.update(2);
				}
		    }
		}, 4L, 3L);
		task3 = az.getServer().getScheduler().runTaskTimer(az, new Runnable() {
		    public void run() {
				for (ActionZone z:aZones.values()) {			
					z.update(3);
				}
		    }
		}, 5L, 3L);
    }
    
    
    public void stopZoneCheck() {
    	task1.cancel();
    	task2.cancel();
    	task3.cancel();
    	zoneCheckActive = false;
    }
    
    public boolean zonesActive() {return zoneCheckActive;}
    public ActionZone getActionZone(String name) {
    	if (name != null && aZones.containsKey(name)) {return aZones.get(name);}
    	return null;
    }
    public Zone getZone(String name) {
    	if (name != null && zones.containsKey(name)) {return zones.get(name);}
    	return null;
    }
    public void deleteZone(String name) {
    	if (aZones.containsKey(name)) {
    		ActionZone z = aZones.get(name);
    		z.removeZone();
    		aZones.remove(z.getName());
    	}
    	if (zones.containsKey(name)) {
    		Zone z = zones.get(name);
    		z.removeZone();
    		zones.remove(z.getName());
    	}
    }
    public ArrayList<String> getZoneNames() {
    	ArrayList<String> names = new ArrayList<String>();
    	for (ActionZone z:aZones.values()) {
    		names.add(z.getName());
    	}
    	return names;
    }
    
    public Collection<ActionZone> getZones() {
    	return aZones.values();
    }

    public void addActionZone(ActionZone zone) {
    	zones.put(zone.getName(), zone);
    	aZones.put(zone.getName(), zone);
    }
    public void addZone(Zone zone) {
    	zones.put(zone.getName(), zone);
    }

}
