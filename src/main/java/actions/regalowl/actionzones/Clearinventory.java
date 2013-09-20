package regalowl.actionzones;

public class Clearinventory extends Action {

	@Override
	void runAction() {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setLevel(0);
		player.setExp(0);
	}

}
