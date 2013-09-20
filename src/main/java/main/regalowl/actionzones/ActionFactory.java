package regalowl.actionzones;

import org.bukkit.Bukkit;

public class ActionFactory {

	
	public Action getAction(ActionType actionType) {
		try {
			String actionString = actionType.toString().replace("_", "");
			String className = "regalowl.actionzones." + actionString.substring(0, 1).toUpperCase() + actionString.substring(1).toLowerCase();
			Class<?> cls = Class.forName(className);
			Object classInstance = (Object)cls.newInstance();
			if (classInstance instanceof Action) {
				return (Action)classInstance;
			}
		} catch (Exception e) {
			Bukkit.broadcast(ActionZones.az.getCommonFunctions().fM("&cInvalid action error! (" + actionType.toString() + ")"), "actionzones.admin");
			e.printStackTrace();
		}
		return null;
	}
	

}
