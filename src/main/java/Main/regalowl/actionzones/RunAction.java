package regalowl.actionzones;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class RunAction {
	private Action a;
	private int actionid;
	private String zonename;
	private ActionZones az;
	private Player p;

	public void runAction(ActionZones actz, Action actio, String zn, Player player) {
		a = actio;
		zonename = zn;
		az = actz;
		p = player;
		
		HashMap <String, Integer> actions = a.getActions();
		String action = az.getYaml().getZones().getString(zonename + ".action").toLowerCase();		
		actionid = -1;	
		if (actions.containsKey(action)) {			
			actionid = actions.get(action);	
    		long delay = 0L;
    		String testdelay = az.getYaml().getZones().getString(zonename + ".delay");
    		if (testdelay != null) {
    			delay = az.getYaml().getZones().getLong(zonename + ".delay");
    		}
    		az.getServer().getScheduler().scheduleSyncDelayedTask(az, new Runnable() {
				public void run() {
					a.selectAction(actionid, zonename, p);
				}
			}, delay);
			return;
		} else {
			//Bukkit.broadcast("§cZone " + zonename + " does not have any actions attached to it.", "actionzones.admin");
			return;
		} 
	}	
}
