package regalowl.actionzones;


public class Strikeeffect extends Action {
	public void runAction() {
		player.getWorld().strikeLightningEffect(player.getLocation());
	}
}
