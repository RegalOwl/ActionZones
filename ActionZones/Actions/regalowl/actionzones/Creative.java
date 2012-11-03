package regalowl.actionzones;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Creative {

	public void giveCreative(Player p) {
		p.setGameMode(GameMode.CREATIVE);
	}
	
	public void removeCreative(Player p) {
		p.setGameMode(GameMode.SURVIVAL);
	}
	
	
}
