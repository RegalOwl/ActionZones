package regalowl.actionzones;

import java.util.ArrayList;

import org.bukkit.entity.Player;


public class MonitorActions {

	private ArrayList<Player> monitor = new ArrayList<Player>();
	
	
	public void monitorActions(String message) {
		int c = 0;
		
		while (c < monitor.size()) {
			monitor.get(c).sendMessage(message);
			c++;
		}
	}
	
	public void add(Player p) {
		monitor.add(p);
	}
	
	public void remove(Player p){
		monitor.remove(p);
	}
	
	public boolean active(Player p) {
		if (monitor.contains(p)) {
			return true;
		} else {
			return false;
		}
	}
	
}
