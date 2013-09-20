package regalowl.actionzones;

import org.bukkit.Location;


public class Strikekill extends Action {
	public void runAction() {
		int x = player.getLocation().getBlockX() - 1;
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ() - 1;
		
		Location l = player.getLocation();
		
		int xc = 0;
		int zc = 0;
		while (zc < 3) {
			while (xc < 3) {
				l.setX(x);
				l.setY(y);
				l.setY(z);
				player.getWorld().strikeLightning(l);
				x++;
				xc++;
			}
			z++;
			xc = 0;
			zc++;
		}
		


		
		player.getWorld().strikeLightning(player.getLocation());

		player.setHealth(0);
	}
}
