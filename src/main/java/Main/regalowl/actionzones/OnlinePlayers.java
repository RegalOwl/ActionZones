package regalowl.actionzones;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;



public class OnlinePlayers implements Listener {

	private ArrayList<Player> onlineplayers1 = new ArrayList<Player>();
	private ArrayList<Player> onlineplayers2 = new ArrayList<Player>();
	private ArrayList<Player> onlineplayers3 = new ArrayList<Player>();
	
	private int s1;
	private int s2;
	private int s3;
	
	public OnlinePlayers(ActionZones az){
		s1 = 0;
		s2 = 0;
		s3 = 0;
    	az.getServer().getPluginManager().registerEvents(this, az);
    }

    
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
		if (s1 <= s2 && s1 <= s3) {
			onlineplayers1.add(event.getPlayer());
			s1++;
		} else if (s2 <= s1 && s2 <= s3) {
			onlineplayers2.add(event.getPlayer());
			s2++;
		} else if (s3 <= s1 && s3 <= s2) {
			onlineplayers3.add(event.getPlayer());
			s3++;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (onlineplayers1.contains(p)) {
			onlineplayers1.remove(p);
			s1--;
		} else if (onlineplayers2.contains(p)) {
			onlineplayers2.remove(p);
			s2--;
		} else if (onlineplayers3.contains(p)) {
			onlineplayers3.remove(p);
			s3--;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onEnable(PluginEnableEvent event) {
		resetList();
	}
	
	public ArrayList<Player> getOnlinePlayers(int thread) {
		switch (thread) {
			case 1:
				return onlineplayers1;
			case 2:
				return onlineplayers2;
			case 3:
				return onlineplayers3;
			default:
				return null;
		}
	}
	
	public void resetList() {
		onlineplayers1.clear();
		onlineplayers2.clear();
		onlineplayers3.clear();
		s1 = 0;
		s2 = 0;
		s3 = 0;
		Player[] player = Bukkit.getOnlinePlayers();
		int c = 0;
		while (c < player.length) {
			if (s1 <= s2 && s1 <= s3) {
				onlineplayers1.add(player[c]);
				s1++;
			} else if (s2 <= s1 && s2 <= s3) {
				onlineplayers2.add(player[c]);
				s2++;
			} else if (s3 <= s1 && s3 <= s2) {
				onlineplayers3.add(player[c]);
				s3++;
			}	
			c++;
		}
	}
	
}
