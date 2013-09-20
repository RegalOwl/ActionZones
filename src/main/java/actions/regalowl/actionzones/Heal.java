package regalowl.actionzones;

public class Heal extends Action {

	@Override
	void runAction() {
		player.setHealth(20);
		player.setFoodLevel(20);
	}

}
