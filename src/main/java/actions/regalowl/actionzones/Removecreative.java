package regalowl.actionzones;

import org.bukkit.GameMode;

public class Removecreative extends Action {
	public void runAction() {
		player.setGameMode(GameMode.SURVIVAL);
	}
}
