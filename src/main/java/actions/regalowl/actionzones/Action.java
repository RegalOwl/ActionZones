package regalowl.actionzones;

import org.bukkit.entity.Player;

import regalowl.databukkit.CommonFunctions;

public abstract class Action {

	abstract void runAction();
	
	public void queue(long delay) {
		ActionZones.az.getServer().getScheduler().runTaskLater(ActionZones.az, new Runnable() {
			public void run() {
				if (zone.locked()) {return;}
				runAction();
			}
		}, delay);
	}
	
	protected ActionZones az = ActionZones.az;
	protected CommonFunctions cf = az.getCommonFunctions();
	
	protected Player player;
	protected ActionPlayer aPlayer;
	public void setPlayer(ActionPlayer player) {
		this.aPlayer = player;
		this.player = aPlayer.getPlayer();
	}
	public Player getPlayer() {return player;}
	
	protected ActionZone zone;
	public void setZone(ActionZone zone) {this.zone = zone;}
	public ActionZone getZone() {return zone;}
	
}
