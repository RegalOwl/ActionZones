package regalowl.actionzones;



public enum ActionType {
	INVALID, MESSAGE, TELEPORT, STRIKE_EFFECT, STRIKE_ONCE, STRIKE_KILL, STORE_INVENTORY, RESTORE_INVENTORY,
	PAY_TELEPORT, HEAL, LAUNCH_DIRECTION, PLACE_BLOCKS, DEACTIVATE, ACTIVATE, CLEAR_INVENTORY, SPAWN_MOB,
	SPAWN_ITEM, SPAWN_TNT, REPLACE_BLOCKS, KILL_MOBS, LOG_ENTRY, GIVE_CREATIVE, REMOVE_CREATIVE, REMOVE_ITEMS, RETURN_TELEPORT,
	EMPTY_INV_TELEPORT, RESTORE_ZONE, LAUNCH_LOCATION, GUIDED_FLIGHT, SET_CONDITION, SPACE_LAUNCH, PAY_ONCE, PAY, OXYGEN, SMOKE,
	POTION_EFFECT, REMOVE_POTION_EFFECT, REMOVE_POTION_EFFECTS;

	public static ActionType fromString(String type) {
		for (ActionType actionType:ActionType.values()) {
			if (type.equalsIgnoreCase(actionType.toString()) || type.equalsIgnoreCase(actionType.toString().replace("_", ""))) {return actionType;}
		}
		return ActionType.INVALID;
	}
	
	public static String getString(ActionType actionType) {
		return actionType.toString().toLowerCase().replace("_", "");
	}
}