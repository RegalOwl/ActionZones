package regalowl.actionzones;

public class Oxygen extends Action {

	@Override
	void runAction() {
		player.setRemainingAir(300);
	}

}
