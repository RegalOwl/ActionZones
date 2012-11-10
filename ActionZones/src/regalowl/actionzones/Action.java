package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Action {
	
	
	
	private ActionZones az;
	private Account acc;
	private Teleport tele;
	private PayTeleport ptele;
	private LightningStrike strike;
	private InventoryStorage invstore;
	private IgnoreAction iga;
	private MonitorActions mona;
	private LaunchDirection l;
	private HashMap <String, Integer> actions = new HashMap<String, Integer>();
	private HashMap <String, Boolean> blockplaceactive = new HashMap<String, Boolean>();


	
	Action(ActionZones actionzones, Account account) {
		
		
		az = actionzones;
		acc = account;
		
		actions.put("message", 0);
		actions.put("teleport", 1);
		actions.put("strikeeffect", 2);
		actions.put("strikeonce", 3);
		actions.put("strikekill", 4);
		actions.put("storeinventory", 5);
		actions.put("restoreinventory", 6);
		actions.put("payteleport", 7);
		actions.put("heal", 8);
		actions.put("launchdirection", 9);
		actions.put("placeblocks", 10);
		actions.put("deactivate", 11);
		actions.put("activate", 12);
		actions.put("clearinventory", 13);
		actions.put("spawnmob", 14);
		actions.put("spawnitem", 15);
		actions.put("spawntnt", 16);
		actions.put("replaceblocks", 17);
		actions.put("killmobs", 18);
		actions.put("logentry", 19);
		actions.put("givecreative", 20);
		actions.put("removecreative", 21);
		actions.put("removeitems", 22);
		actions.put("returnteleport", 23);
		actions.put("emptyinvteleport", 24);
		actions.put("restorezone", 25);
		actions.put("launchlocation", 26);
		actions.put("guidedflight", 27);
		actions.put("setcondition", 28);
		actions.put("spacelaunch", 29);
		actions.put("payonce", 30);
		actions.put("pay", 31);
		actions.put("oxygen", 32);
		actions.put("smoke", 33);
		actions.put("potioneffect", 34);
		actions.put("removepotioneffect", 35);
		actions.put("removepotioneffects", 36);
		actions.put("bounce", 37);
		
		tele = new Teleport();
		ptele = new PayTeleport();
		strike = new LightningStrike();
		invstore = new InventoryStorage();
		l = new LaunchDirection();
		iga = new IgnoreAction();
		mona = new MonitorActions();
		
	}

	public void runAction(String zone, Player player) {
		if (az.useDebug(player)) {
			String action = az.getYaml().getZones().getString(zone + ".action");
			if (action.equalsIgnoreCase("null")) {
				action = "none";
			}
			player.sendMessage(ChatColor.DARK_PURPLE + "[Zone]" + ChatColor.WHITE + "" + zone + ChatColor.DARK_PURPLE + " [Type]" + ChatColor.WHITE + "Action" + ChatColor.DARK_PURPLE + " [Action]" + ChatColor.WHITE + "" + action);
			
			
		}
		//Remove after plugin has all zones updated.
		FileConfiguration zonesyaml = az.getYaml().getZones();
		if (zonesyaml.getString(zone + ".active") == null) {
			zonesyaml.set(zone + ".active", true);
		}
		if (zonesyaml.getBoolean(zone + ".active")) {
			RunAction ra = new RunAction();
			ra.runAction(az, this, zone, player);
		}
		String action = zonesyaml.getString(zone + ".action");
		if (!action.equalsIgnoreCase("null")) {
			mona.monitorActions("§5[Player]§f" + player.getName() + " §5[Zone]§f" + zone + " §5[Action]§f" + action);
		}
		
	}
	
	
	public void runExitAction(String zone, Player player) {	
		if (az.useDebug(player)) {
			String action = az.getYaml().getZones().getString(zone + ".exit-action");	
			if (action.equalsIgnoreCase("null")) {
				action = "none";
			}
			player.sendMessage(ChatColor.DARK_PURPLE + "[Zone]" + ChatColor.WHITE + "" + zone + ChatColor.DARK_PURPLE + " [Type]" + ChatColor.WHITE + "ExitAction" + ChatColor.DARK_PURPLE + " [Action]" + ChatColor.WHITE + "" + action);
			
			
		}
		//Remove after plugin has all zones updated.
		FileConfiguration zonesyaml = az.getYaml().getZones();
		if (zonesyaml.getString(zone + ".active") == null) {
			zonesyaml.set(zone + ".active", true);
		}
		if (zonesyaml.getBoolean(zone + ".active")) {
			RunExitAction ra = new RunExitAction();
			ra.runAction(az, this, zone, player);	
		}
		String action = zonesyaml.getString(zone + ".exit-action");
		if (!action.equalsIgnoreCase("null")) {
			mona.monitorActions("§5[Player]§f" + player.getName() + " §5[Zone]§f" + zone + " §5[Exit Action]§f" + action);
		}
		
	}
	

	public void selectAction(int actionid, String zone, Player p) {
		if (iga.ignoreAction(p, actionid)) {
			return;
		}
		
	    switch (actionid) {
		    
	    	case 0: 
	    		sendMessage(p, zone);
	    		break;  			
	        case 1: 
	        	tele.teleportPlayer(az, p, zone);
	            break;
	        case 2: 
	        	strike.strikeEffect(p);
	            break;
	        case 3: 
	    		strike.strikeOnce(p);
	         	break;
	        case 4: 
	    		strike.strikeKill(p);
	         	break;
	        case 5: 
	    		invstore.takeInventory(az, p);
	         	break;
	        case 6: 
	        	invstore.returnInventory(az, p);
	         	break;
	        case 7:
	        	ptele.payTeleportPlayer(az, p, zone, acc);
	        	break;
	        case 8:
	        	p.setHealth(20);
	        	p.setFoodLevel(20);
	        	break;
	        case 9:
	        	l.LaunchinDirection(az, zone, p);
	        	break;
	        case 10:
	        	if (blockplaceactive.get(zone) == null) {
	        		setBlockPlaceInactive(zone);
	        	}
	        	if (!blockplaceactive.get(zone)) {
	        		setBlockPlaceActive(zone);
		        	SpawnBlocks sb = new SpawnBlocks();
		        	sb.spawnBlocks(az, zone, this);
	        	}
	        	break;
	        case 11:
	        	Activate activate = new Activate();
	        	activate.DeactivateZone(az, zone);
	        	break;
	        case 12:
	        	Activate activate2 = new Activate();
	        	activate2.ActivateZone(az, zone);
	        	break;
	        case 13:
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.setLevel(0);
				p.setExp(0);
	        	break;
	        case 14:
	        	SpawnMob sm = new SpawnMob();
	        	sm.spawn(az, p, zone);
	        	break;
	        case 15:
	        	SpawnItem si = new SpawnItem();
	        	si.spawnItem(az, p, zone);
	        	break;
	        case 16:
	        	SpawnTNT st = new SpawnTNT();
	        	st.spawn(az, p, zone);
	        	break;
	        case 17:
	        	if (blockplaceactive.get(zone) == null) {
	        		setBlockPlaceInactive(zone);
	        	}
	        	if (!blockplaceactive.get(zone)) {
	        		setBlockPlaceActive(zone);
		        	ReplaceBlocks rb = new ReplaceBlocks();
		        	rb.replaceBlocks(az, zone, this);
	        	}
	        	break;
	        case 18:
	        	KillMobs km = new KillMobs();
	        	km.killMobs(az, zone);
	        	break;
	        case 19:
	        	EntranceList el = new EntranceList();
	        	el.logEntry(az, zone, p);
	        	break;
	        case 20:
	        	Creative cr = new Creative();
	        	cr.giveCreative(p);
	        	break;
	        case 21:
	        	Creative cr2 = new Creative();
	        	cr2.removeCreative(p);
	        	break;
	        case 22:
	        	RemoveItems ri = new RemoveItems();
	        	ri.removeItems(az, zone);
	        	break;
	        case 23:
	        	ReturnTeleport rt = new ReturnTeleport();
	        	rt.returnTeleportPlayer(az, p, zone);
	        	break;
	        case 24:
	        	EmptyInvTeleport eit = new EmptyInvTeleport();
	        	eit.emptyInvTeleport(az, p, zone);
	        	break;
	        case 25:
	        	RestoreZones sz = new RestoreZones();
	        	sz.RestoreAction(az, zone);
	        	break;
	        case 26:
	        	LaunchLocation ll = new LaunchLocation();
	        	ll.LaunchtoLocation(az, zone, p);
	        	break;
	        case 27:
	        	GuidedFlight gf = new GuidedFlight();
	        	gf.Fly(az, zone, p);
	        	break;
	        case 28:
	        	SetCondition sd = new SetCondition();
	        	sd.Set(az, zone);
	        	break;
	        case 29:
	        	SpaceLaunch sl = new SpaceLaunch();
	        	sl.LaunchSpace(az, zone, p);
	        	break;
	        case 30:
	        	PayOnce po = new PayOnce();
	        	po.payOnce(az, p, zone, acc);
	        	break;
	        case 31:
	        	Pay pay = new Pay();
	        	pay.pay(az, p, zone, acc);
	        	break;	        	
	        case 32:
	        	p.setRemainingAir(300);
	        	break;	 
	        case 33:
	        	Smoke smo = new Smoke();
	        	smo.makeSmoke(az, zone);
	        	break;	 	  
	        case 34:
	        	PotEffect pe = new PotEffect();
	        	pe.giveEffect(az, p, zone);
	        	break;	 
	        case 35:
	        	RemovePotEffect rpe = new RemovePotEffect();
	        	rpe.removeEffect(az, p, zone);
	        	break;	
	        case 36:
	        	RemoveAllPotEffect rae = new RemoveAllPotEffect();
	        	rae.removeEffects(az, p, zone);
	        	break;	
	        case 37:
	        	Bounce bce = new Bounce();
	        	bce.startBounce(az, zone, p);
	        	break;
	        default: 
				Bukkit.broadcast("§cInvalid action error!", "actionzones.admin");
	            break;
	    }
	}
	
	
	
	

	public void sendMessage(Player p, String zone) {
		String testmessage = az.getYaml().getZones().getString(zone + ".message");
		if (testmessage != null) {
			String message = az.getYaml().getZones().getString(zone + ".message").replace("%s", " ").replace("&", "§");
			p.sendMessage(message);
		} else {
			p.sendMessage("§bYou have entered the " + zone + " zone!");
		}
		
	}
	
	
	
	
	
	public boolean checkAction(String action) {
		boolean actiongood = false;
		if (actions.containsKey(action)) {
			actiongood = true;
		}
		return actiongood;
	}
	
	public HashMap<String, Integer> getActions() {
		return actions;
	}
	
	public void setBlockPlaceInactive(String zone) {
		blockplaceactive.put(zone, false);
	}
	
	public void setBlockPlaceActive(String zone) {
		blockplaceactive.put(zone, true);
	}
	
	
	
	public IgnoreAction getIgnoreActions() {
		return iga;
	}

	public MonitorActions getMonitorActions() {
		return mona;
	}
	
	public ArrayList<String> getActionsArray() {
		String act[] = actions.keySet().toArray(new String[0]);
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < actions.size(); i++) {
			a.add(act[i]);
		}
		Collections.sort(a, String.CASE_INSENSITIVE_ORDER);
		return a;
	}

}
