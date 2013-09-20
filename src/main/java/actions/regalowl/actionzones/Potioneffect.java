package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Potioneffect extends Action {
	
	HashMap<String, PotionEffectType> pets = new HashMap<String, PotionEffectType>();
	
	
	Potioneffect() {
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
		
		String testduration = zone.getString("duration");
		int duration = 200;
		if (testduration != null) {
			duration = zone.getInt("duration");
		}
		
		String testvalue = zone.getString("value");
		double value = 0;
		if (testvalue != null) {
			value = zone.getDouble("value");
		}
				
		PotionEffect pe = new PotionEffect(pets.get(effect), duration, (int) value);	
		player.addPotionEffect(pe);
	}
	
	public boolean testEffect(String effect) {
		if (pets.containsKey(effect)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<String> getEffects() {
		String pef[] = pets.keySet().toArray(new String[0]);
		ArrayList<String> p = new ArrayList<String>();
		for (int i = 0; i < pets.size(); i++) {
			p.add(pef[i]);
		}
		Collections.sort(p, String.CASE_INSENSITIVE_ORDER);
		return p;
	}
	
}
