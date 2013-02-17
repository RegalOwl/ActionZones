package regalowl.actionzones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class InvisibleRollerCoaster {

	private Vector v;
	private Player p;
	private ActionZones az;
	private int launchthreadid;
	
	private int x;
	private int y;
	private int z;
	
	private int px;
	private int py;
	private int pz;
	
	public void CoastertoLocation(ActionZones acz, String zonename, Player player) {
		p = player;
		az = acz;
		//Returns if there is no attached location.
		String location = az.getYaml().getZones().getString(zonename + ".location");
		if (location == null) {
			Bukkit.broadcast("§cZone " + zonename + " needs to have a location attached in order to support directional launching.", "actionzones.admin");
			return;
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

		
		
		launchthreadid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				
				if (x == px && z == pz) {
					v.setX(0);
					v.setZ(0);
					finish();
				}
				
				Location pl = p.getLocation();
				px = pl.getBlockX();
				py = pl.getBlockY();
				pz = pl.getBlockZ();
				
				v.setX(x - px);
				v.setY(y - py);
				v.setZ(z - pz);
				p.setVelocity(v);
				
			}
		}, 5L, 5L);
		
	}
	
	private void finish() {
		az.getServer().getScheduler().cancelTask(launchthreadid);
	}
	

}
