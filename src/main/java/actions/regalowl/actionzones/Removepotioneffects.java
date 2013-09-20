package regalowl.actionzones;

import java.util.ArrayList;
import org.bukkit.potion.PotionEffectType;

public class Removepotioneffects extends Action {
	
	ArrayList<PotionEffectType> pets = new ArrayList<PotionEffectType>();
	
	
	Removepotioneffects() {
		pets.add(PotionEffectType.BLINDNESS);
		pets.add(PotionEffectType.CONFUSION);
		pets.add(PotionEffectType.DAMAGE_RESISTANCE);
		pets.add(PotionEffectType.FAST_DIGGING);
		pets.add(PotionEffectType.FIRE_RESISTANCE);
		pets.add(PotionEffectType.HARM);
		pets.add(PotionEffectType.HEAL);
		pets.add(PotionEffectType.HUNGER);
		pets.add(PotionEffectType.INCREASE_DAMAGE);
		pets.add(PotionEffectType.INVISIBILITY);
		pets.add(PotionEffectType.JUMP);
		pets.add(PotionEffectType.NIGHT_VISION);
		pets.add(PotionEffectType.POISON);
		pets.add(PotionEffectType.REGENERATION);
		pets.add(PotionEffectType.SLOW);
		pets.add(PotionEffectType.SLOW_DIGGING);
		pets.add(PotionEffectType.SPEED);
		pets.add(PotionEffectType.WATER_BREATHING);
		pets.add(PotionEffectType.WEAKNESS);
	}
	
	
	
	public void runAction() {
		for (int i = 0; i < pets.size(); i++) {
			player.removePotionEffect(pets.get(i));
		}
	}
	
}
