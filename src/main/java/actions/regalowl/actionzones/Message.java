package regalowl.actionzones;


public class Message extends Action {

	
	public void runAction() {

		String testmessage = zone.getString("message");
		if (testmessage != null) {
			String message = cf.fM(zone.getString("message").replace("%s", " "));
			player.sendMessage(message);
		} else {
			player.sendMessage(cf.fM("&bYou have entered the " + zone.getName() + " zone!"));
		}

	}

}
