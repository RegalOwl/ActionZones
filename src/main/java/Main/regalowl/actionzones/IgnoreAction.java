package regalowl.actionzones;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;




public class IgnoreAction {
	
	private HashMap <Player, ArrayList<Integer>> ignore = new HashMap<Player, ArrayList<Integer>>();
	private Action a;
	
	
	
	public void addIgnoreAction(Player player, int id) {
		
		
		if (!ignore.containsKey(player)) {
			ArrayList<Integer> ignoreids = new ArrayList<Integer>();
			ignoreids.add(id);
			ignore.put(player, ignoreids);
			
		} else {
			ArrayList<Integer> ignoreids = ignore.get(player);
			ignoreids.add(id);
			ignore.put(player, ignoreids);
		}
	}
	
	public void removeIgnoreAction(Player player, int id) {
		if (!ignore.containsKey(player)) {
			return;
			
		} else {
			ArrayList<Integer> ignoreids = ignore.get(player);
			ignoreids.remove(ignoreids.indexOf(id));
			ignore.put(player, ignoreids);
			if (ignoreids.size() == 0) {
				ignore.remove(player);
			}
		}
	}
	
	public void removeAllIgnoreAction(Player player) {
		if (ignore.containsKey(player)) {
			ignore.remove(player);
		}
	}
	
	public void addAllIgnoreAction(Action act, Player player) {
		a = act;
		int numactions = a.getActions().size();
		ArrayList<Integer> ignoreactions = new ArrayList<Integer>();
		int n = 0;
		while (n < numactions) {
			ignoreactions.add(n);
			n++;
		}
		ignore.put(player, ignoreactions);
	}
	
	public boolean ignoreAction(Player player, int id) {
		boolean ignoreaction = false;
		
		if (!ignore.containsKey(player)) {
			return false;
		}
		
		ArrayList<Integer> ignoreids = ignore.get(player);
		if (ignoreids.contains(id)) {
			ignoreaction = true;
		}
		
		return ignoreaction;
	}
	
	public boolean ignoreAllAction(Action act, Player player) {
		boolean ignoreaction = false;
		
		if (!ignore.containsKey(player)) {
			return false;
		}
		
		ArrayList<Integer> ignoreids = ignore.get(player);
		if (ignoreids.size() == act.getActions().keySet().size()) {
			ignoreaction = true;
		}
		
		return ignoreaction;
	}
	
	
	
	
}
