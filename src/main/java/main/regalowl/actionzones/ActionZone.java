package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collection;


public class ActionZone extends Zone {


	private ActionFactory af;
	private ActionPlayerHandler op;

	private ArrayList<ActionType> actions = new ArrayList<ActionType>();
	private ArrayList<ActionType> exitActions = new ArrayList<ActionType>();

	private long delay;
	private long exitDelay;
	private boolean active;
	
	private boolean lock;
	
	private ArrayList<ActionPlayer> inZone = new ArrayList<ActionPlayer>();

	
	ActionZone(String name) {
		super(name);
		af = az.getActionFactory();
		op = az.getOnlinePlayer();
		actions = convertActions(getString("actions"));
		exitActions = convertActions(getString("exitactions"));
		active = getBoolean("active");
		delay = getLong("delay");
		exitDelay = getLong("exit-delay");
		lock = false;
	}
	
	ActionZone(String name, int x, int y, int z, String world) {
		super(name, x, y, z, world);
		af = az.getActionFactory();
		op = az.getOnlinePlayer();
		zonesConfig.set(name + ".type", "action");
		setActive(true);
		setDelay(0L);
		setExitDelay(0L);
		lock = false;
	}

	
	
	public void update(int thread) {
		if (lock) {return;}
		if (!active) {return;}
		Collection<ActionPlayer> players = op.getPlayers(thread);
		for (ActionPlayer p:players) {
			if (inZone.contains(p) && !inZone(p.getLocation())) {
				if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Exited &5[Zone]&f" + name));}
				for (ActionType at:exitActions) {
					if (p.isIgnoringAction(at)) {
						if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Ignored &5[Action]&f" + at.toString()));}
						continue;
					}
					Action action = af.getAction(at);
					if (action == null) {continue;}
					action.setPlayer(p);
					action.setZone(this);
					action.queue(exitDelay);
					if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Triggered &5[Action]&f" + at.toString()));}
					sendMonitorAlert(cf.fM("&5[Player]&f" + p.getPlayer().getName() + " &5[Zone]&f" + name + " &5[ExitAction]&f" + at.toString()));
				}
				inZone.remove(p);
			} else if (!inZone.contains(p) && inZone(p.getLocation())) {
				if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Entered &5[Zone]&f" + name));}
				for (ActionType at:actions) {
					if (p.isIgnoringAction(at)) {
						if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Ignored &5[Action]&f" + at.toString()));}
						continue;
					}
					Action action = af.getAction(at);
					if (action == null) {continue;}
					action.setPlayer(p);
					action.setZone(this);
					action.queue(delay);
					if (p.isDebug()) {p.getPlayer().sendMessage(cf.fM("Triggered &5[Action]&f" + at.toString()));}
					sendMonitorAlert(cf.fM("&5[Player]&f" + p.getPlayer().getName() + " &5[Zone]&f" + name + " &5[Action]&f" + at.toString()));
				}
				inZone.add(p);
			}
		}
	}
	
	
	public void sendMonitorAlert(String message) {
		for (ActionPlayer ap:op.getPlayers(1)) {ap.sendMonitorAlert(message);}
		for (ActionPlayer ap:op.getPlayers(2)) {ap.sendMonitorAlert(message);}
		for (ActionPlayer ap:op.getPlayers(3)) {ap.sendMonitorAlert(message);}
	}

	

	
	public void setDelay(long delay) {
		this.delay = delay;
		zonesConfig.set(name + ".delay", delay);
	}
	public long getDelay() {return delay;}
	public void setExitDelay(long delay) {
		this.exitDelay = delay;
		zonesConfig.set(name + ".exit-delay", delay);
	}
	public long getExitDelay() {return exitDelay;}
	
	public void setActions(ArrayList<ActionType> actions) {
		this.actions = actions;
		zonesConfig.set(name + ".actions", convertActions(actions));
	}
	public void setExitActions(ArrayList<ActionType> actions) {
		this.exitActions = actions;
		zonesConfig.set(name + ".exitactions", convertActions(actions));
	}
	
	public ArrayList<ActionType> getActions() {
		return actions;
	}
	public ArrayList<ActionType> getExitActions() {
		return exitActions;
	}
	
	public boolean hasAction(ActionType actionType) {
		if (actions.contains(actionType)) {return true;}
		return false;
	}
	public boolean hasExitAction(ActionType actionType) {
		if (exitActions.contains(actionType)) {return true;}
		return false;
	}
	
	public void addAction(ActionType actionType) {
		if (!hasAction(actionType)) {
			actions.add(actionType);
			setActions(actions);
		}
	}
	public void addExitAction(ActionType actionType) {
		if (!hasExitAction(actionType)) {
			exitActions.add(actionType);
			setExitActions(exitActions);
		}
	}
	
	public boolean isActive() {return active;}
	public void setLock(boolean lock) {this.lock = lock;}
	public boolean locked() {return lock;}
	
	public void setActive(boolean active) {
		this.active = active;
		zonesConfig.set(name + ".active", active);
	}
	

	
	public String convertActions(ArrayList<ActionType> actions) {
		String actionString = "";
		for (ActionType actionType:actions) {
			actionString += (actionType.toString() + ",");
		}
		return actionString;
	}
	public ArrayList<ActionType> convertActions(String actions) {
		ArrayList<ActionType> actionTypes = new ArrayList<ActionType>();
		ArrayList<String> strings = az.getCommonFunctions().explode(actions, ",");
		for (String string:strings) {
			actionTypes.add(ActionType.fromString(string));
		}
		return actionTypes;
	}

}
