package regalowl.actionzones;


import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActionPlayerHandler implements Listener {

	private HashMap<String,ActionPlayer> op1 = new HashMap<String,ActionPlayer>();
	private HashMap<String,ActionPlayer> op2 = new HashMap<String,ActionPlayer>();
	private HashMap<String,ActionPlayer> op3 = new HashMap<String,ActionPlayer>();

	public ActionPlayerHandler(){
    	ActionZones.az.getServer().getPluginManager().registerEvents(this, ActionZones.az);
    	initialize();
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {addPlayer(event.getPlayer());}
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {removePlayer(event.getPlayer().getName());}
	
	public void addPlayer(Player p) {
		ActionPlayer ap = new ActionPlayer(p);
		if (op1.size() <= op2.size() && op1.size() <= op3.size()) {
			op1.put(p.getName(), ap);
		} else if (op2.size() <= op1.size() && op2.size() <= op3.size()) {
			op2.put(p.getName(), ap);
		} else if (op3.size() <= op1.size() && op3.size() <= op2.size()) {
			op3.put(p.getName(), ap);
		}
	}
	
	public ActionPlayer getActionPlayer(String name) {
		if (op1.containsKey(name)) {return op1.get(name);} 
		if (op2.containsKey(name)) {return op2.get(name);}
		if (op3.containsKey(name)) {return op3.get(name);}
		return null;
	}

	
	public void removePlayer(String name) {
		if (op1.containsKey(name)) {op1.remove(name);} 
		if (op2.containsKey(name)) {op2.remove(name);}
		if (op3.containsKey(name)) {op3.remove(name);}
	}

	public Collection<ActionPlayer> getPlayers(int thread) {
		switch (thread) {
			case 1:return op1.values();
			case 2:return op2.values();
			case 3:return op3.values();
			default:return null;
		}
	}
	
	public void initialize() {
		op1.clear();
		op2.clear();
		op3.clear();
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player p:players) {
			addPlayer(p);
		}
	}
	
}
