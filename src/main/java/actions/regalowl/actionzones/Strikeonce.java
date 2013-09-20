package regalowl.actionzones;


public class Strikeonce extends Action {
	public void runAction() {
		player.getWorld().strikeLightning(player.getLocation());
	}
}
