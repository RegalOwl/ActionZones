package regalowl.actionzones;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

public class Removepotioneffect extends Action {
	
	HashMap<String, PotionEffectType> pets = new HashMap<String, PotionEffectType>();
	
	Removepotioneffect() {
		pets.put("blindness", PotionEffectType.BLINDNESS);
		pets.put("confusion", PotionEffectType.CONFUSION);
		pets.put("damageresistance", PotionEffectType.DAMAGE_RESISTANCE);
		pets.put("fastdigging", PotionEffectType.FAST_DIGGING);
		pets.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
		pets.put("harm", PotionEffectType.HARM);
		pets.put("heal", PotionEffectType.HEAL);
		pets.put("hunger", PotionEffectType.HUNGER);
		pets.put("increasedamage", PotionEffectType.INCREASE_DAMAGE);
		pets.put("invisibility", PotionEffectType.INVISIBILITY);
		pets.put("jump", PotionEffectType.JUMP);
		pets.put("nightvision", PotionEffectType.NIGHT_VISION);
		pets.put("poison", PotionEffectType.POISON);
		pets.put("regeneration", PotionEffectType.REGENERATION);
		pets.put("slow", PotionEffectType.SLOW);
		pets.put("slowdigging", PotionEffectType.SLOW_DIGGING);
		pets.put("speed", PotionEffectType.SPEED);
		pets.put("waterbreathing", PotionEffectType.WATER_BREATHING);
		pets.put("weakness", PotionEffectType.WEAKNESS);
	}
	
	
	
	public void runAction() {
		
		String effect = zone.getString("effect");
		if (effect == null) {
			Bukkit.broadcast("§cZone " + zone.getName() + " needs to have a potion effect attached to be usable.", "actionzones.admin");
			return;
		}

		player.removePotionEffect(pets.get(effect));
	}
	
}
