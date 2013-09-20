package regalowl.actionzones;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActionPlayer {

	ActionPlayer(Player p) {name = p.getName();}
	
	public Location getLocation() {
		Player p = Bukkit.getPlayer(name);
		if (p != null) {return p.getLocation();}
		return null;
	}
	
	private String name;
	public Player getPlayer() {return Bukkit.getPlayer(name);}
	public void setPlayer(Player p) {name = p.getName();}
	
	private boolean monitorActions;
	public boolean isMonitoring() {return monitorActions;}
	public void setMonitoring(boolean monitorActions) {this.monitorActions = monitorActions;}
	public void sendMonitorAlert(String message) {
		if (isMonitoring()) {getPlayer().sendMessage(message);}
	}
	
	private boolean debugMode;
	public boolean isDebug() {return debugMode;}
	public void setDebug(boolean debugMode) {this.debugMode = debugMode;}
	
	private ArrayList<ActionType> ignoredActions = new ArrayList<ActionType>();
	public void ignoreAction(ActionType actionType) {
		if (!ignoredActions.contains(actionType)) {
			ignoredActions.add(actionType);
		}
	}
	public void unignoreAction(ActionType actionType) {
		if (ignoredActions.contains(actionType)) {
			ignoredActions.remove(actionType);
		}
	}
	public void unignoreAll() {ignoredActions.clear();}
	public void ignoreAll() {
		ignoredActions.clear();
		for (ActionType actionType:ActionType.values()) {ignoredActions.add(actionType);}
	}
	public boolean isIgnoringAction(ActionType actionType) {return ignoredActions.contains(actionType);}
	public boolean isIgnoringAll() {
		for (ActionType type:ActionType.values()) {
			if (!ignoredActions.contains(type)) {return false;}
		}
		return true;
	}
	
}
