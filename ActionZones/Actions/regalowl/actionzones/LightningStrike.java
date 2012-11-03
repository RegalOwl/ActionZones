package regalowl.actionzones;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LightningStrike {
	
	
	public void strikeEffect(Player p) {
		p.getWorld().strikeLightningEffect(p.getLocation());
	}
	
	public void strikeOnce(Player p) {
		p.getWorld().strikeLightning(p.getLocation());
	}
	
	
	public void strikeKill(Player p) {
		int x = p.getLocation().getBlockX() - 1;
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ() - 1;
		
		Location l = p.getLocation();
		
		int xc = 0;
		int zc = 0;
		while (zc < 3) {
			while (xc < 3) {
				l.setX(x);
				l.setY(y);
				l.setY(z);
				p.getWorld().strikeLightning(l);
				x++;
				xc++;
			}
			z++;
			xc = 0;
			zc++;
		}
		


		
		p.getWorld().strikeLightning(p.getLocation());

		p.setHealth(0);
	}

}
