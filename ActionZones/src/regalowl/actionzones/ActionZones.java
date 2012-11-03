package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;





public class ActionZones extends JavaPlugin{
	
	private Zone z;
	private Action a;
	private YamlFile y;
	
	

	private Account acc;
	
	private OnlinePlayers op;

	
	private ArrayList<Player> tn = new ArrayList<Player>();
	
	private ArrayList<Integer> fpid = new ArrayList<Integer>();
	
    //VAULT**********************************************************************
	
    private Logger log = Logger.getLogger("Minecraft");
    private Vault vault = null;
    private Economy economy;
	
    //VAULT**********************************************************************
	
	
	
	@Override
    public void onEnable() {
		
		op = new OnlinePlayers(this);
    	
    	//Stores the new YamlFile as y.
    	YamlFile yam = new YamlFile();	
    	yam.YamlEnable(this);    	
    	y = yam;

        //VAULT**********************************************************************
    	Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
        if(x != null & x instanceof Vault) {
        	
        	this.setupEconomy();
            vault = (Vault) x;
            log.info(String.format("[%s] Hooked %s %s", getDescription().getName(), vault.getDescription().getName(), vault.getDescription().getVersion()));
            log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));
        } else {
            log.warning(String.format("[%s] Vault was _NOT_ found!", getDescription().getName()));
        }
        
       
    	
        //VAULT**********************************************************************
        
        acc = new Account(economy);
        
        
    	Action action = new Action(this, acc);
    	a = action;
    	
    	z = new Zone(this, a, op);
        
    	SpawnBlocks sb = new SpawnBlocks();
    	sb.forceRestore(this, a);
    	ReplaceBlocks rb = new ReplaceBlocks();
    	rb.forceRestore(this, a);
    	
    	
    	fpid.add(0);
		fpid.add(1);
		fpid.add(2);
		fpid.add(3);
		fpid.add(4);
		fpid.add(5);
		fpid.add(7);
		fpid.add(12);
		fpid.add(13);
		fpid.add(14);
		fpid.add(15);
		fpid.add(16);
		fpid.add(17);
		fpid.add(19);
		fpid.add(20);
		fpid.add(21);
		fpid.add(22);
		fpid.add(23);
		fpid.add(24);
		fpid.add(29);
		fpid.add(30);
		fpid.add(33);
		fpid.add(35);
		fpid.add(41);
		fpid.add(42);
		fpid.add(45);
		fpid.add(46);
		fpid.add(47);
		fpid.add(48);
		fpid.add(49);
		fpid.add(52);
		fpid.add(53);
		fpid.add(54);
		fpid.add(56);
		fpid.add(57);
		fpid.add(58);
		fpid.add(61);
		fpid.add(67);
		fpid.add(73);
		fpid.add(79);
		fpid.add(80);
		fpid.add(82);
		fpid.add(84);
		fpid.add(85);
		fpid.add(86);
		fpid.add(87);
		fpid.add(88);
		fpid.add(89);
		fpid.add(91);
		fpid.add(98);
		fpid.add(99);
		fpid.add(100);
		fpid.add(101);
		fpid.add(102);
		fpid.add(103);
		fpid.add(107);
		fpid.add(108);
		fpid.add(109);
		fpid.add(110);
		fpid.add(112);
		fpid.add(113);
		fpid.add(114);
		fpid.add(116);
		fpid.add(121);
		fpid.add(123);
		fpid.add(124);
		fpid.add(126);
		fpid.add(128);
		fpid.add(129);
		fpid.add(130);

    	
    	Logger log = Logger.getLogger("Minecraft");
		log.info("ActionZones has been successfully enabled!");
    }
    
    
    
  //VAULT**********************************************************************
    
    private Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    
  //VAULT**********************************************************************
    
    
    
    
    @Override
    public void onDisable() {

    	//Saves files.
        y.saveYamls();
        Logger log = Logger.getLogger("Minecraft");
        log.info("ActionZones has been disabled!");
    }
	
	
    
    
    
    
    
    
    
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
    		player = (Player) sender;
    	}

    	
    	if (cmd.getName().equalsIgnoreCase("setzone") && player != null) {
    		
    		
    		if (args.length == 2) {
	    			
	    			if (args[0].equalsIgnoreCase("p1")) {
	    				
	    				String name = args[1];
	    				String teststring = y.getZones().getString(name);
	    				if (teststring == null) {
	    					name = fixzName(name);
	    				}
	    				name = name.replace(".", "").replace(":", "");
	        			z.setsPoint(name, player);
	        			z.setP1();
	        			player.sendMessage(ChatColor.GREEN + "Zone location p1 has been set!");	
	    			} else if (args[0].equalsIgnoreCase("p2")) {

	    				String name = args[1];
	    				name = name.replace(".", "").replace(":", "");
	    				String teststring = y.getZones().getString(name);
	    				if (teststring == null) {
	    					name = fixzName(name);
	    				}
	    				z.setsPoint(name, player);
	        			z.setP2();
	        			player.sendMessage(ChatColor.GREEN + "Zone location p2 has been set!");	
	    			}
	    		} else {
	    			player.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setzone ['p1'/'p2'] [name]");
	    		}
	    		return true;	
	    		
	    	}
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("setlocation") && player != null) {
    		
    		
    		if (args.length == 1) {
    					FileConfiguration l = y.getLocations();

	    				String name = args[0];

	    				
	    				
	    				String teststring = l.getString(name);
	    				if (teststring == null) {
	    					name = fixlName(name);
	    				}


	    				name = name.replace(".", "").replace(":", "");
	    				
	    				Location loc = player.getLocation();
	    				l.set(name + ".x", loc.getX());
	    				l.set(name + ".y", loc.getY());
	    				l.set(name + ".z", loc.getZ());
	    				l.set(name + ".world", loc.getWorld().getName());
	        			player.sendMessage(ChatColor.GREEN + "Location set!");	
	    		} else {
	    			player.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setlocation [name]");
	    		}
	    		return true;	
	    	}
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("goto") && player != null) {
    		
    		
    		if (args.length == 1) {
	    				String location = args[0].toLowerCase();
	    				
	    				FileConfiguration l = y.getLocations();
	    				String teststring = l.getString(location);
	    				if (teststring == null) {
	    					location = fixlName(location);
	    					teststring = l.getString(location);
	    				}
	    				if (teststring == null) {
	    					player.sendMessage(ChatColor.RED + "That location doesn't exist!");
	    					return true;
	    				}

	    				
	    				//Gets new location.
	    				FileConfiguration locs = getYaml().getLocations();
	    				Double x = locs.getDouble(location + ".x");
	    				Double y = locs.getDouble(location + ".y");
	    				Double z = locs.getDouble(location + ".z");
	    				float yaw = player.getLocation().getYaw();
	    				float pitch = player.getLocation().getPitch();
	    				World w = Bukkit.getWorld(locs.getString(location + ".world"));

	    				//Sets location to new location.
	    				Location newloc = new Location(w, x, y, z);
	    				
	    				
	    				if (!newloc.getChunk().isLoaded()) {
	    					newloc.getChunk().load(true);
	    				}
	    				
	    				newloc.setPitch(pitch);
	    				newloc.setYaw(yaw);
	    				player.teleport(newloc);
	    		} else {
	    			player.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /goto [location]");
	    		}
	    		return true;	
	    	}
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkaction")) {
    		
    		
    		if (args.length == 2) {
    			String actionname = args[0];
    			
    			if (!a.checkAction(actionname)) {
    				sender.sendMessage(ChatColor.DARK_RED + "That action does not exist!");
    				return true;
    			}
    	    				String zone = args[1];
    	    				FileConfiguration z = y.getZones();
    	    				String testzone = z.getString(zone);
    	    				
    	    				if (testzone != null) {
    	    					
    	    					z.set(zone + ".action", actionname);
    	    					sender.sendMessage(ChatColor.GOLD + "Action set!");
    	    					
    	    				} else {
    	    					sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        						return true;
    	    				}

	    		} else {
	    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkaction [action] [zone]");
	    		}
	    		return true;	
	    	}
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkexitaction")) {
    		
    		
    		if (args.length == 2) {
    			String actionname = args[0];
    			
    			if (!a.checkAction(actionname)) {
    				sender.sendMessage(ChatColor.DARK_RED + "That action does not exist!");
    				return true;
    			}
    	    				String zone = args[1];
    	    				FileConfiguration z = y.getZones();
    	    				String testzone = z.getString(zone);
    	    				
    	    				if (testzone != null) {
    	    					
    	    					z.set(zone + ".exit-action", actionname);
    	    					sender.sendMessage(ChatColor.GOLD + "Exit Action set!");
    	    					
    	    				} else {
    	    					sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        						return true;
    	    				}

	    		} else {
	    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkexitaction [action] [zone]");
	    		}
	    		return true;	
	    	}
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("setzonemessage")) {

    		if (args.length == 2) {
    			String message = args[0];			
    	    				String zone = args[1];

    	    				FileConfiguration z = y.getZones();
    	    				String testzone = z.getString(zone);
    	    				
    	    				if (testzone != null) {
    	    					
    	    					z.set(zone + ".message", message);
    	    					sender.sendMessage(ChatColor.GOLD + "Message set!");
    	    					
    	    				} else {
    	    					sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        						return true;
    	    				}
	    		} else {
	    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setzonemessage [message] [zone]");
	    		}
	    		return true;	
	    	}
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("deletezone")) {

    		if (args.length == 1) {
    			String zone = args[0];			
    	    	FileConfiguration zones = y.getZones();
    	    	String testzone = zones.getString(zone);
    	    				
    	    	if (testzone != null) {	
    	    		
    	    		zones.set(zone, null);
    	    		z.stopzoneCheck();
    	        	Zone zon = new Zone(this, a, op);
    	        	z = zon;
    	        	z.startzoneCheck();
    	    		sender.sendMessage(ChatColor.GOLD + "Zone deleted!");			
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}
	    	} else {
	    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /deletezone [zone]");
	    	}
	    		return true;	
	    	}
    	
    	
    	if (cmd.getName().equalsIgnoreCase("deletelocation")) {

    		if (args.length == 1) {
    			String location = args[0];			
    	    	FileConfiguration l = y.getLocations();
    	    	String testloc = l.getString(location);
    	    				
    	    	if (testloc != null) {				
    	    		l.set(location, null);
    	    		sender.sendMessage(ChatColor.GOLD + "Location deleted!");			
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That location does not exist!");
        			return true;
    	    	}
	    	} else {
	    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /deletelocation [location]");
	    	}
	    		return true;	
	    	}
    	
    	
    	if (cmd.getName().equalsIgnoreCase("actionlist")) {

    		if (args.length == 1) {
    			String type = args[0];
    			if (type.contains("zon")) {
    				ArrayList<String> zo = z.getZoneKeys();
    				sender.sendMessage(ChatColor.AQUA + zo.toString());
    			} else if (type.contains("loc")) {
    				ArrayList<String> lo = new ArrayList<String>();
        			Iterator<String> it = getYaml().getLocations().getKeys(false).iterator();
        			while (it.hasNext()) {   			
        				Object element2 = it.next();
        				String ele = element2.toString();
        				lo.add(ele);
        			}
        			Collections.sort(lo, String.CASE_INSENSITIVE_ORDER);
        			sender.sendMessage(ChatColor.AQUA + lo.toString());
    			} else if (type.contains("mob")) {
    				SpawnMob sm = new SpawnMob();
    				sender.sendMessage(ChatColor.AQUA + sm.getMobs().toString());
    			} else if (type.contains("eff")) {
    				PotEffect pe = new PotEffect();
    				sender.sendMessage(ChatColor.AQUA + pe.getEffects().toString());
    			} else if (type.contains("act")) {
    				sender.sendMessage(ChatColor.AQUA + a.getActionsArray().toString());
    			}
    			
	    	} else {
	    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /actionlist [type]");
	    	}
	    		return true;	
	    	}
    	
    	
    	
 
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("setactiondelay")) {

    		try {
	    		if (args.length == 2) {
	    			int delay = Integer.parseInt(args[0]);			
	
	    	    	String zone = args[1];
	
	    	    				
	    	    	FileConfiguration z = y.getZones();
	    	    	String testzone = z.getString(zone);
	    	    				
	    	    	if (testzone != null) {
	    	    					
	    	    		z.set(zone + ".delay", delay);
	    	    		sender.sendMessage(ChatColor.GOLD + "Delay set!");
	    	    					
	    	    	} else {
	    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
	        			return true;
	    	    	}
	    	    				
	
		    	} else {
		    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setactiondelay [delay] [zone]");
		    	}
	    		return true;	
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setactiondelay [delay] [zone]");
    			return true;
    		}
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("setexitactiondelay")) {

    		try {
	    		if (args.length == 2) {
	    			int delay = Integer.parseInt(args[0]);			
	
	    	    	String zone = args[1];
	
	    	    				
	    	    	FileConfiguration z = y.getZones();
	    	    	String testzone = z.getString(zone);
	    	    				
	    	    	if (testzone != null) {
	    	    					
	    	    		z.set(zone + ".exit-delay", delay);
	    	    		sender.sendMessage(ChatColor.GOLD + "Exit Delay set!");
	    	    					
	    	    	} else {
	    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
	        			return true;
	    	    	}
	    	    				
	
		    	} else {
		    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setexitactiondelay [delay] [zone]");
		    	}
	    		return true;	
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setexitactiondelay [delay] [zone]");
    			return true;
    		}
	    }
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkduration")) {

    		try {
	    		if (args.length == 2) {
	    			int duration = Integer.parseInt(args[0]);			
	
	    	    	String zone = args[1];
	
	    	    				
	    	    	FileConfiguration z = y.getZones();
	    	    	String testzone = z.getString(zone);
	    	    				
	    	    	if (testzone != null) {
	    	    					
	    	    		z.set(zone + ".duration", duration);
	    	    		sender.sendMessage(ChatColor.GOLD + "Duration linked!");
	    	    					
	    	    	} else {
	    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
	        			return true;
	    	    	}
	    	    				
	
		    	} else {
		    		sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkduration [duration] [zone]");
		    	}
	    		return true;	
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkduration [duration] [zone]");
    			return true;
    		}
	    }
    	
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linklocation")) {
    		if (args.length == 2) {
    			String locationname = args[0];
    			FileConfiguration l = y.getLocations();				
    			String testloc = l.getString(locationname + ".x");
    			if (testloc != null) {
    				String zone = args[1];
    	    		FileConfiguration z = y.getZones();
    	    		String testzone = z.getString(zone); 				
    	    		if (testzone != null) {				
    	    			z.set(zone + ".location", locationname);
    	    			sender.sendMessage(ChatColor.GOLD + "Location linked to zone!");  					
    	    		} else {
    	    			sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        				return true;
    	    		}
    			} else {
    				sender.sendMessage(ChatColor.DARK_RED + "That location does not exist!");
    				return true;
    			}		
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linklocation [location] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkzone")) {
    		if (args.length == 2) {
    			String zonename = args[0];
    			FileConfiguration z = y.getZones();
    			String testzone = z.getString(zonename);
    			if (testzone != null) {
    				String zone = args[1];
    	    		String testzone2 = z.getString(zone); 				
    	    		if (testzone2 != null) {				
    	    			z.set(zone + ".zone", zonename);
    	    			sender.sendMessage(ChatColor.GOLD + "Zone linked to zone!");  					
    	    		} else {
    	    			sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        				return true;
    	    		}
    			} else {
    				sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
    				return true;
    			}		
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkzone [zone to link] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkcondition")) {
    		if (args.length == 2) {
    			String condition = args[0];
    			FileConfiguration z = y.getZones();
				String zone = args[1];
	    		String testzone = z.getString(zone); 				
	    		if (testzone != null) {				
	    			z.set(zone + ".condition", condition);
	    			sender.sendMessage(ChatColor.GOLD + "Condition linked to zone!");  					
	    		} else {
	    			sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
    				return true;
	    		}
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkcondition [condition] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkmob")) {
    		if (args.length == 2) {
    			String mob = args[0];
    			FileConfiguration z = y.getZones();
    			SpawnMob sm = new SpawnMob();
    			if (sm.getEntityType(mob) != null) {
    				String zone = args[1];
    	    		String testzone2 = z.getString(zone); 				
    	    		if (testzone2 != null) {				
    	    			z.set(zone + ".mob", mob);
    	    			sender.sendMessage(ChatColor.GOLD + "Mob linked to zone!");  					
    	    		} else {
    	    			sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        				return true;
    	    		}
    			} else {
    				sender.sendMessage(ChatColor.DARK_RED + "That mob does not exist!");
    				return true;
    			}		
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkmob [mob] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkeffect")) {
    		if (args.length == 2) {
    			String effect = args[0];
    			FileConfiguration z = y.getZones();
    			PotEffect pe = new PotEffect();
    			if (pe.testEffect(effect)) {
    				String zone = args[1];
    	    		String testzone2 = z.getString(zone); 				
    	    		if (testzone2 != null) {				
    	    			z.set(zone + ".effect", effect);
    	    			sender.sendMessage(ChatColor.GOLD + "Effect linked to zone!");  					
    	    		} else {
    	    			sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        				return true;
    	    		}
    			} else {
    				sender.sendMessage(ChatColor.DARK_RED + "That effect does not exist!");
    				return true;
    			}		
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkeffect [effect] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkquantity")) {
    		if (args.length == 2) {
    			int quantity = 0;
    			try {
    				quantity = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "That quantity is invalid!");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".quantity", quantity);
    	    		sender.sendMessage(ChatColor.GOLD + "Quantity linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkquantity [quantity] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkvalue")) {
    		if (args.length == 2) {
    			double value = 0;
    			try {
    				value = Double.parseDouble(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "That value is invalid!");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".value", value);
    	    		sender.sendMessage(ChatColor.GOLD + "Value linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkvalue [value] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkblockid")) {
    		if (args.length == 2) {
    			int blockid = 0;
    			try {
    				blockid = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkblockid [block id] [zone]");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".blockid", blockid);
    	    		sender.sendMessage(ChatColor.GOLD + "Block ID linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkblockid [block id] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkdamagevalue")) {
    		if (args.length == 2) {
    			int damagevalue = 0;
    			try {
    				damagevalue = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkdamagevalue [damagevalue] [zone]");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".damagevalue", damagevalue);
    	    		sender.sendMessage(ChatColor.GOLD + "Damage value linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkdamagevalue [damagevalue] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkreplaceblockid")) {
    		if (args.length == 2) {
    			int blockid = 0;
    			try {
    				blockid = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkreplaceblockid [block id] [zone]");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".replaceblockid", blockid);
    	    		sender.sendMessage(ChatColor.GOLD + "Block ID to replace linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkreplaceblockid [block id] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkreplacedamagevalue")) {
    		if (args.length == 2) {
    			int damagev = 0;
    			try {
    				damagev = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkreplacedamagevalue [damage value] [zone]");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".replacedamagevalue", damagev);
    	    		sender.sendMessage(ChatColor.GOLD + "Damage value to replace linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkreplacedamagevalue [damage value] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkdurability")) {
    		if (args.length == 2) {
    			int durability = 0;
    			try {
    				durability = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkdurability [durability] [zone]");
    				return true;
    			}
    			FileConfiguration z = y.getZones();
    			String zone = args[1];
    	    	String testzone2 = z.getString(zone); 				
    	    	if (testzone2 != null) {				
    	    		z.set(zone + ".durability", durability);
    	    		sender.sendMessage(ChatColor.GOLD + "Durability linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}

    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkdurability [durability] [zone]");
	    	}
	    		return true;	
	    }
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkmoney")) {
    		if (args.length == 2) {
    			Double money = 0.0;
    			try {
    				money = Double.parseDouble(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkmoney [money] [zone]");
    				return true;
    			}
    			String zone = args[1];
    	    	FileConfiguration z = y.getZones();
    	    	String testzone = z.getString(zone); 				
    	    	if (testzone != null) {				
    	    		z.set(zone + ".money", money);
    	    		sender.sendMessage(ChatColor.GOLD + "Monetary value linked to zone!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkmoney [money] [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("linkpassword")) {
    		if (args.length == 2) {
    			String password= args[0];
    			String location = args[1];
    	    	FileConfiguration l = y.getLocations();
    	    	String testloc = l.getString(location); 				
    	    	if (testloc != null) {				
    	    		l.set(location + ".password", password);
    	    		sender.sendMessage(ChatColor.GOLD + "Password linked to location!");  					
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That location does not exist!");
        			return true;
    	    	}
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /linkpassword [password] [location]");
	    	}
	    	return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("password")) {
    		if (args.length == 1) {
    			String password = args[0];
    			FileConfiguration locs = getYaml().getLocations();
    			Iterator<String> it = locs.getKeys(false).iterator();
    			while (it.hasNext()) {   			
    				String location = it.next().toString();    				
    				String testpass = locs.getString(location + ".password");
    				
    				if (testpass != null) {
    					if (testpass.equals(password)) {
    						player.sendMessage(ChatColor.GREEN + "Password Accepted!");
    						double x = locs.getDouble(location + ".x");
    		    			double y = locs.getDouble(location + ".y");
    		    			double z = locs.getDouble(location + ".z");
    		    			float yaw = player.getLocation().getYaw();
    		    			float pitch = player.getLocation().getPitch();
    		    			World w = Bukkit.getWorld(locs.getString(location + ".world"));
    		    			Location newloc = new Location(w, x, y, z);
    		    			if (!newloc.getChunk().isLoaded()) {
    		    				newloc.getChunk().load(true);
    		    			}	
    		    			newloc.setPitch(pitch);
    		    			newloc.setYaw(yaw);
    		    			player.teleport(newloc);
    		    			return true;
    					}
    				}
    			}        
    	    	
    			player.sendMessage(ChatColor.RED + "Access Denied!");
        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /password [password]");
	    	}
	    	return true;	
	    }
    	
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("entrylog")) {
    		if (args.length == 1) {
    			String zone = args[0];
    			FileConfiguration zones = getYaml().getEntranceList();   							
    			String testzone = zones.getString(zone);
    				
    			if (testzone != null) {
    				sender.sendMessage(ChatColor.GREEN + "These players have entered the zone: " + zones.getString(zone));

    			} else {
    				sender.sendMessage(ChatColor.BLUE + "That zone either does not exist or has no logged entries.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /entrylog [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("clearentrylog")) {
    		if (args.length == 1) {
    			String zone = args[0];
    			FileConfiguration zones = getYaml().getEntranceList();   							
    			String testzone = zones.getString(zone);
    				
    			if (testzone != null) {
    				zones.set(zone, null);
    				sender.sendMessage(ChatColor.GREEN + "Entry Log Cleared!");

    			} else {
    				sender.sendMessage(ChatColor.BLUE + "That zone either does not exist or has no logged entries.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /clearentrylog [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	
       	if (cmd.getName().equalsIgnoreCase("azmonitor")) {
    		if (args.length == 0) {
    			MonitorActions mona = a.getMonitorActions();
    			if (mona.active(player)) {
    				mona.remove(player);
    				player.sendMessage(ChatColor.BLUE + "You are no longer monitoring action events.");
    			} else {
    				mona.add(player);
    				player.sendMessage(ChatColor.BLUE + "You are now monitoring action events.");
    			}
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /azmonitor");
	    	}
	    	return true;	
	    }

    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("togglebypass")) {
    		if (args.length == 1) {
    			String action = args[0].toLowerCase();
    			if (a.getActions().get(action) == null && !action.equalsIgnoreCase("all")) {
    				sender.sendMessage(ChatColor.RED + "That action does not exist!");
    				return true;
    			}
    			IgnoreAction iga = a.getIgnoreActions();
    			
    			if (action.equalsIgnoreCase("all")) {
    				boolean ignoreall = iga.ignoreAllAction(a, player);
    				
    				if (ignoreall) {
    					iga.removeAllIgnoreAction(player);
    					player.sendMessage(ChatColor.BLUE + "You will now be affected by all actions.");
    				} else {
    					iga.addAllIgnoreAction(a, player);
    					player.sendMessage(ChatColor.BLUE + "You will no longer be affected by any actions.");
    				}
    				
    				return true;
    			}
    			
    			
    			
    			int actionid = a.getActions().get(action);
    			
    			
    			if (iga.ignoreAction(player, actionid)) {
    				iga.removeIgnoreAction(player, actionid);
    				player.sendMessage(ChatColor.BLUE + "You will now be affected by " + action + " actions.");
    			} else {
    				iga.addIgnoreAction(player, actionid);
    				player.sendMessage(ChatColor.BLUE + "You will no longer be affected by " + action + " actions.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /togglebypass [action]");
	    	}
	    	return true;	
	    }

    	
    	if (cmd.getName().equalsIgnoreCase("zstore")) {
    		if (args.length == 1) {
    			String zone = args[0];
    			FileConfiguration zones = getYaml().getZones();   							
    			String testzone = zones.getString(zone);
    				
    			if (testzone != null) {
    				RestoreZones sz = new RestoreZones();
    				sz.storeZone(this, zone, player);
    				sender.sendMessage(ChatColor.GREEN + "Zone save started!");

    			} else {
    				sender.sendMessage(ChatColor.RED + "That zone does not exist.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /zstore [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("zstoreremove")) {
    		if (args.length == 1) {
    			String zone = args[0];
    			FileConfiguration zones = getYaml().getZones();   							
    			String testzone = zones.getString(zone);
    				
    			if (testzone != null) {
    				RestoreZones sz = new RestoreZones();
    				sz.removeFromStorage(this, zone);
    				sender.sendMessage(ChatColor.GREEN + "Zone removed from database!");

    			} else {
    				sender.sendMessage(ChatColor.RED + "That zone does not exist.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /zstoreremove [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("zrestore")) {
    		if (args.length == 1) {
    			String zone = args[0];
    			FileConfiguration zones = getYaml().getZones();   							
    			String testzone = zones.getString(zone);
    				
    			if (testzone != null) {
    				RestoreZones sz = new RestoreZones();
    				sz.Restore(0L, this, zone);
    				sender.sendMessage(ChatColor.GREEN + "Zone restored!");

    			} else {
    				sender.sendMessage(ChatColor.RED + "That zone does not exist.");
    			}

        		return true;
    	    	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /zrestore [zone]");
	    	}
	    	return true;	
	    }
    	
    	
    	if (cmd.getName().equalsIgnoreCase("setsqlthreads")) {
    		if (args.length == 1) {
    			int threads = 0;
    			try {
    				threads = Integer.parseInt(args[0]);
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setsqlthreads [threads]");
    				return true;
    			}
    			FileConfiguration config = getYaml().getConfig();   							
    			config.set("swapzones.sql-threads", threads);		
    			sender.sendMessage(ChatColor.GREEN + "SQL thread count set!");
        		return true;	
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setsqlthreads [threads]");
	    	}
	    	return true;	
	    }
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("actionzones")) {
    		if (args.length == 0) {
    			sender.sendMessage(ChatColor.GREEN + "ActionZones Active: " + z.zonesActive());
    			return true;
    		}
    		if (z == null) {
    			Bukkit.broadcastMessage("NULLZONE");
    			z = new Zone(this, a, op);
    		}
    		if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("0")) {
    			z.stopzoneCheck();
    	    	z.clearAll();
    	        y.saveYamls();
    			sender.sendMessage(ChatColor.GREEN + "ActionZones disabled!");
    		} else if (args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("1")) {
    			op.resetList();
    	    	YamlFile yam = new YamlFile();	
    	    	yam.YamlEnable(this);    	
    	    	y = yam;
    	    	Action action = new Action(this, acc);
    	    	a = action;
    	    	Zone zone = new Zone(this, a, op);
    	    	z = zone;
    	    	SpawnBlocks sb = new SpawnBlocks();
    	    	sb.forceRestore(this, a);
    	    	ReplaceBlocks rb = new ReplaceBlocks();
    	    	rb.forceRestore(this, a);
    	    	z.startzoneCheck();
    			sender.sendMessage(ChatColor.GREEN + "ActionZones enabled!");
    		} else {
    			sender.sendMessage(ChatColor.GREEN + "ActionZones Active: " + z.zonesActive());
    		}
	    		return true;	
	    }
    	
    	if (cmd.getName().equalsIgnoreCase("azstats")) {
    		ArrayList<Player> players1 = op.getOnlinePlayers(1);
    		ArrayList<Player> players2 = op.getOnlinePlayers(2);
    		ArrayList<Player> players3 = op.getOnlinePlayers(3);
    		int c = 0;
    		String names1 = "";
    		while (c < players1.size()) {
    			if (names1 != "") {
    				names1 = names1 + ", " + players1.get(c).getName();
    			} else {
    				names1 = players1.get(c).getName();
    			}
    			c++;
    		}
    		c = 0;
    		String names2 = "";
    		while (c < players2.size()) {
    			if (names2 != "") {
    				names2 = names2 + ", " + players2.get(c).getName();
    			} else {
    				names2 = players2.get(c).getName();
    			}
    			c++;
    		}
    		c = 0;
    		String names3 = "";
    		while (c < players3.size()) {
    			if (names3 != "") {
    				names3 = names3 + ", " + players3.get(c).getName();
    			} else {
    				names3 = players3.get(c).getName();
    			}
    			c++;
    		}
    		sender.sendMessage(ChatColor.BLACK + "-----------------------------------------------------");
    		sender.sendMessage(ChatColor.WHITE + "There are currently: " + ChatColor.DARK_PURPLE + "" + z.getZoneKeys().size() + ChatColor.WHITE + " zones.");
    		sender.sendMessage(ChatColor.WHITE + "There are currently: " + ChatColor.DARK_PURPLE + "" + getYaml().getLocations().getKeys(false).size() + ChatColor.WHITE + " locations.");
    		sender.sendMessage(ChatColor.WHITE + "Thread 1: " + ChatColor.DARK_PURPLE + "" + names1);
    		sender.sendMessage(ChatColor.WHITE + "Thread 2: " + ChatColor.DARK_PURPLE + "" + names2);
    		sender.sendMessage(ChatColor.WHITE + "Thread 3: " + ChatColor.DARK_PURPLE + "" + names3);
    		sender.sendMessage(ChatColor.BLACK + "-----------------------------------------------------");
    		
	    	return true;	
	    }
    	
    	if (cmd.getName().equalsIgnoreCase("forcerestore")) {
    		
        	SpawnBlocks sb = new SpawnBlocks();
        	sb.forceRestore(this, a);
	    	ReplaceBlocks rb = new ReplaceBlocks();
	    	rb.forceRestore(this, a);
        	sender.sendMessage(ChatColor.GOLD + "Restoration complete!");

	    	return true;	
	    }
    	
    	
    	
    	if (cmd.getName().equalsIgnoreCase("togglezone")) {
    		
    		if (args.length == 1) {
    			String zone = args[0];
    	    	FileConfiguration z = y.getZones();
    	    	String testzone = z.getString(zone); 				
    	    	if (testzone != null) {		
    	    		//delete after updating all zones
    	    		if (z.getString(zone + ".active") == null) {
    	    			z.set(zone + ".active", true);
    	    		}
    	    		if (z.getBoolean(zone + ".active")) {
    	    			z.set(zone + ".active", false);
    	    			sender.sendMessage(ChatColor.GOLD + zone + " disabled!"); 
    	    		} else {
    	    			z.set(zone + ".active", true);
    	    			sender.sendMessage(ChatColor.GOLD + zone + " enabled!"); 
    	    		}				
    	    	} else {
    	    		sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
        			return true;
    	    	}
    		} else {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /togglezone [zone]");
	    	}
	    		return true;	
	    }
    	
    	if (cmd.getName().equalsIgnoreCase("togglezonedebug")) {
    		
    		if (!tn.contains(player)) {
    			tn.add(player);
    			sender.sendMessage(ChatColor.GOLD + "Zone Debug Mode Enabled!");
    		} else {
    			tn.remove(player);
    			sender.sendMessage(ChatColor.GOLD + "Zone Debug Mode Disabled!");
	    	}
	    		return true;	
	    }
    	
    	
    	/*
    	if (cmd.getName().equalsIgnoreCase("setzonecheckinterval")) {
    			try {
		    		long interval = Long.parseLong(args[0]);
		    		z.setzoneInterval(interval);
		    		sender.sendMessage(ChatColor.GREEN + "Zone Check Interval Set!");
			    	return true;	
    			} catch (Exception e) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setzonecheckinterval [interval]");
    				return true;
    			}
	    	}
	    	*/
    	
    	if (cmd.getName().equalsIgnoreCase("browsezones")) {
    		try {
    			if (args.length > 2) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters.  Use /browsezones [Search string] (page)");
    				return true;
    			}
	    		//Gets the search string.
	    		String input = args[0].toLowerCase();
	    		
	    		//Gets the page number if given.
				int page;
				if (args.length == 1) {
					page = 1;
				} else {
					page = Integer.parseInt(args[1]);
				}
				ArrayList<String> names = z.getZoneKeys();
				ArrayList<String> rnames = new ArrayList<String>();
				int i = 0;
				while(i < names.size()) {
					String cname = names.get(i);
					if (cname.startsWith(input)) {
						rnames.add(cname);
					}
					i++;
				}
				//Alphabetizes arraylist.
				Collections.sort(rnames, String.CASE_INSENSITIVE_ORDER);
				int numberpage = page * 10;
				int count = 0;
				int rsize = rnames.size();
				double maxpages = rsize/10;
				maxpages = Math.ceil(maxpages);
				int maxpi = (int)maxpages + 1;
				sender.sendMessage(ChatColor.RED + "Page " + ChatColor.WHITE + "(" + ChatColor.RED + "" + page + ChatColor.WHITE + "/" + ChatColor.RED + "" + maxpi + ChatColor.WHITE + ")");
				while (count < numberpage) {
					if (count > ((page * 10) - 11)) {
						if (count < rsize) {
							String iname = rnames.get(count);		
							sender.sendMessage("§b" + iname + " [A]" + y.getZones().getString(iname + ".action") + " [EA]" + y.getZones().getString(iname + ".exit-action"));
						} else {
							sender.sendMessage("You have reached the end.");
							break;
						}			
					}
					count++;
				}
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters.  Use /browsezones [Search string] (page)");
    			return true;
    		}
    		return true;
    	}
    	
    	
    	if (cmd.getName().equalsIgnoreCase("browselocations")) {
    		try {
    			if (args.length > 2) {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters.  Use /browselocations [Search string] (page)");
    				return true;
    			}
	    		//Gets the search string.
	    		String input = args[0].toLowerCase();
	    		
	    		//Gets the page number if given.
				int page;
				if (args.length == 1) {
					page = 1;
				} else {
					page = Integer.parseInt(args[1]);
				}
				ArrayList<String> names = new ArrayList<String>();
				
    			Iterator<String> it = getYaml().getLocations().getKeys(false).iterator();
    			while (it.hasNext()) {   			
    				names.add(it.next().toString());
    			}
				
				
				ArrayList<String> rnames = new ArrayList<String>();
				int i = 0;
				while(i < names.size()) {
					String cname = names.get(i);
					if (cname.startsWith(input)) {
						rnames.add(cname);
					}
					i++;
				}
				//Alphabetizes arraylist.
				Collections.sort(rnames, String.CASE_INSENSITIVE_ORDER);
				int numberpage = page * 10;
				int count = 0;
				int rsize = rnames.size();
				double maxpages = rsize/10;
				maxpages = Math.ceil(maxpages);
				int maxpi = (int)maxpages + 1;
				sender.sendMessage(ChatColor.RED + "Page " + ChatColor.WHITE + "(" + ChatColor.RED + "" + page + ChatColor.WHITE + "/" + ChatColor.RED + "" + maxpi + ChatColor.WHITE + ")");
				while (count < numberpage) {
					if (count > ((page * 10) - 11)) {
						if (count < rsize) {
							String iname = rnames.get(count);		
							sender.sendMessage("§b" + iname);
						} else {
							sender.sendMessage("You have reached the end.");
							break;
						}			
					}
					count++;
				}
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters.  Use /browselocations [Search string] (page)");
    			return true;
    		}
    		return true;
    	}
    	
    	
    	return false;
    }
    
    
    
    
    
    
    
	public Zone getZone() {
		return z;
	}
    
	//public ActionData getActionData() {
	//	return ad;
	//}
	
	
	
    public YamlFile getYaml() {
    	return y;
    }
	
	
    
	public String fixzName(String nam) {
		String name = nam;
		int c = 0;
		int l = y.getZones().getKeys(false).size();
		Object names[] = y.getZones().getKeys(false).toArray();
		while (c < l) {
			if (names[c].toString().equalsIgnoreCase(name)) {
				name = names[c].toString();
				return name;
			}
			c++;
		}
		
		return name;
	}
	
	
	public String fixlName(String nam) {
		String name = nam;
		int c = 0;
		int l = y.getLocations().getKeys(false).size();
		Object names[] = y.getLocations().getKeys(false).toArray();
		while (c < l) {
			if (names[c].toString().equalsIgnoreCase(name)) {
				name = names[c].toString();
				return name;
			}
			c++;
		}
		
		return name;
	}
	
	
	
    public void requestSave(long wait) {
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    public void run() {
		    	y.saveYamls();
		    }
		}, wait);
    }
    
    
    public boolean useDebug(Player p) {
    	if (tn.contains(p)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public ArrayList<Integer> getFpid() {
    	return fpid;
    }
    
	
	
	
	
}
