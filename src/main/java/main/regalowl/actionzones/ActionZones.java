package regalowl.actionzones;

import java.util.ArrayList;
import java.util.Collection;
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
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import regalowl.databukkit.CommonFunctions;
import regalowl.databukkit.DataBukkit;
import regalowl.databukkit.YamlHandler;

public class ActionZones extends JavaPlugin {

	public static ActionZones az;
	private ActionFactory af;
	//private YamlFile y;
	//private int savetaskid;
	private Account acc;
	private ActionPlayerHandler op;
	private ZoneHandler zh;
	private DataBukkit db;
	private YamlHandler yh;
	private CommonFunctions cf;
	

	private Logger log = Logger.getLogger("Minecraft");
	private Vault vault = null;
	private Economy economy;

	@Override
	public void onEnable() {
		enable();
		/*
		 * SpawnBlocks sb = new SpawnBlocks(); sb.forceRestore(this, a);
		 * ReplaceBlocks rb = new ReplaceBlocks(); rb.forceRestore(this, a);
		 */
	}

	@Override
	public void onDisable() {
		disable();
	}

	public void enable() {
		az = this;
		db = new DataBukkit(this);
		yh = db.getYamlHandler();
		yh.copyFromJar("config");
		yh.registerFileConfiguration("config");
		yh.registerFileConfiguration("blocks");
		yh.registerFileConfiguration("entrancelist");
		yh.registerFileConfiguration("inventory");
		yh.registerFileConfiguration("locations");
		yh.registerFileConfiguration("players");
		yh.registerFileConfiguration("swapzones");
		yh.registerFileConfiguration("zones");
		yh.setCurrentFileConfiguration("config");
		if (yh.gB("mysql.use")) {
			db.enableMySQL(yh.gS("mysql.host"), yh.gS("mysql.database"), yh.gS("mysql.username"), yh.gS("mysql.password"), yh.gI("mysql.port"));
		}
		db.createDatabase();
		cf = db.getCommonFunctions();
		op = new ActionPlayerHandler();
		af = new ActionFactory();
		//y = new YamlFile();
		//y.YamlEnable(this);
		//startSave();
		setupEconomy();
		acc = new Account(economy);
		zh = new ZoneHandler();
		Logger log = Logger.getLogger("Minecraft");
		log.info("ActionZones has been successfully enabled!");
	}

	public void disable() {
		zh.stopZoneCheck();
		az.getServer().getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		db.shutDown();
		//y.saveYamls();
		Logger log = Logger.getLogger("Minecraft");
		log.info("ActionZones has been disabled!");
	}

	private void setupEconomy() {
		Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
		if (x != null & x instanceof Vault) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
			vault = (Vault) x;
		} else {
			log.warning(String.format("[%s] Vault was _NOT_ found!", getDescription().getName()));
		}
	}

	/*
	public void startSave() {
		savetaskid = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				y.saveYamls();
			}
		}, 1400L, 1400L);
	}
	 */
	//public void stopSave() {
	//	this.getServer().getScheduler().cancelTask(savetaskid);
	//}

	public ActionPlayerHandler getOnlinePlayer() {
		return op;
	}

	//public YamlFile getYaml() {
	//	return y;
	//}
	
	public YamlHandler getYamlHandler() {
		return yh;
	}
	
	public CommonFunctions getCommonFunctions() {
		return cf;
	}
	
	public DataBukkit getDataBukkit() {
		return db;
	}

	public ActionFactory getActionFactory() {return af;}
	public Account getAccount() {return acc;}
	public ZoneHandler getZoneHandler() {return zh;}

	public String fixzName(String name) {
		Object names[] = yh.gFC("zones").getKeys(false).toArray();
		for (Object cname:names) {
			if (cname.toString().equalsIgnoreCase(name)) {
				return cname.toString();
			}
		}
		return name;
	}

	public String fixlName(String name) {
		Object names[] = yh.gFC("locations").getKeys(false).toArray();
		for (Object cname:names) {
			if (cname.toString().equalsIgnoreCase(name)) {
				return cname.toString();
			}
		}
		return name;
	}

	//public void requestSave(long wait) {
	//	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	//		public void run() {
	//			y.saveYamls();
	//		}
	//	}, wait);
	//}
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		FileConfiguration zonesConfig = yh.gFC("zones");

		if (cmd.getName().equalsIgnoreCase("actionzones")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "ActionZones Active: " + zh.zonesActive());
				return true;
			}

			if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("0")) {
				disable();
				sender.sendMessage(ChatColor.GREEN + "ActionZones disabled!");
			} else if (args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("1")) {
				enable();
				sender.sendMessage(ChatColor.GREEN + "ActionZones enabled!");
			}

			if (args[0].equalsIgnoreCase("link") || args[0].equalsIgnoreCase("l")) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Use /az link [object]");
					return true;
				}
				if (args[1].equalsIgnoreCase("action") || args[1].equalsIgnoreCase("a")) {
					ActionType actionType = ActionType.fromString(args[2]);
					if (actionType == ActionType.INVALID) {
						sender.sendMessage(ChatColor.DARK_RED + "That action does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zone.addAction(actionType);
					sender.sendMessage(ChatColor.GREEN + "Action added!");
					return true;
				}

				if (args[1].equalsIgnoreCase("exitaction") || args[1].equalsIgnoreCase("ea")) {
					ActionType actionType = ActionType.fromString(args[2]);
					if (actionType == ActionType.INVALID) {
						sender.sendMessage(ChatColor.DARK_RED + "That action does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zone.addExitAction(actionType);
					sender.sendMessage(ChatColor.GREEN + "Exit action added!");
					return true;
				}

				if (args[1].equalsIgnoreCase("message")) {
					String message = args[2];
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".message", message);
					sender.sendMessage(ChatColor.GOLD + "Message set!");
					return true;
				}

				if (args[1].equalsIgnoreCase("delay")) {
					int delay = 0;
					try {
						delay = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The action delay must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zone.setDelay(delay);
					sender.sendMessage(ChatColor.GREEN + "Action delay set!");
					return true;
				}

				if (args[1].equalsIgnoreCase("exitdelay")) {
					int delay = 0;
					try {
						delay = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The exit action delay must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zone.setExitDelay(delay);
					sender.sendMessage(ChatColor.GREEN + "Exit action delay set!");
					return true;
				}

				if (args[1].equalsIgnoreCase("duration")) {
					int duration = 0;
					try {
						duration = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The duration must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".duration", duration);
					sender.sendMessage(ChatColor.GOLD + "Duration linked!");
					return true;
				}

				if (args[1].equalsIgnoreCase("location")) {
					String location = args[2];
					if (yh.gFC("locations").getString(location + ".x") == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That location does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".location", location);
					sender.sendMessage(ChatColor.GOLD + "Location linked to zone!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("region")) {
					Zone region = zh.getZone(args[2]);
					if (region == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That region does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".zone", region.getName());
					sender.sendMessage(ChatColor.GOLD + "Region linked to zone!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("condition")) {
					String condition = args[2];
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".condition", condition);
					sender.sendMessage(ChatColor.GOLD + "Condition linked to zone!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("mob")) {
					String mob = args[2];
					Spawnmob sm = new Spawnmob();
					if (sm.getEntityType(mob) == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That mob does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".mob", mob);
					sender.sendMessage(ChatColor.GOLD + "Mob linked to zone!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("effect")) {
					String effect = args[2];
					Potioneffect pe = new Potioneffect();
					if (!pe.testEffect(effect)) {
						sender.sendMessage(ChatColor.DARK_RED + "That effect does not exist!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".effect", effect);
					sender.sendMessage(ChatColor.GOLD + "Effect linked to zone!");
					return true;
				}
				
				
				if (args[1].equalsIgnoreCase("quantity")) {
					int quantity = 0;
					try {
						quantity = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The quantity must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".quantity", quantity);
					sender.sendMessage(ChatColor.GOLD + "Quantity linked!");
					return true;
				}
				
				
				if (args[1].equalsIgnoreCase("value")) {
					double value = 0.0;
					try {
						value = Double.parseDouble(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The value must be a number!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".value", value);
					sender.sendMessage(ChatColor.GOLD + "Value linked!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("blockid")) {
					int id = 0;
					try {
						id = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The id must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".blockid", id);
					sender.sendMessage(ChatColor.GOLD + "Block id linked!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("damagevalue")) {
					int dv = 0;
					try {
						dv = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The damage value must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".damagevalue", dv);
					sender.sendMessage(ChatColor.GOLD + "Damage value linked!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("replaceblockid")) {
					int rbi = 0;
					try {
						rbi = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The replace block id must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".replaceblockid", rbi);
					sender.sendMessage(ChatColor.GOLD + "Replace block id linked!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("replacedamagevalue")) {
					int rdv = 0;
					try {
						rdv = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The replace damage value must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".replacedamagevalue", rdv);
					sender.sendMessage(ChatColor.GOLD + "Replace damage value linked!");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("durability")) {
					int d = 0;
					try {
						d = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The durabilty must be an integer!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".durability", d);
					sender.sendMessage(ChatColor.GOLD + "Durability value linked!");
					return true;
				}
				
				
				if (args[1].equalsIgnoreCase("money")) {
					double value = 0.0;
					try {
						value = Double.parseDouble(args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.DARK_RED + "The monetary value must be a number!");
						return true;
					}
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".money", value);
					sender.sendMessage(ChatColor.GOLD + "Money linked!");
					return true;
				}
				
				
				if (args[1].equalsIgnoreCase("password")) {
					String pass = args[2];
					ActionZone zone = zh.getActionZone(args[3]);
					if (zone == null) {
						sender.sendMessage(ChatColor.DARK_RED + "That zone does not exist!");
						return true;
					}
					zonesConfig.set(zone.getName() + ".password", pass);
					sender.sendMessage(ChatColor.GOLD + "Password linked to zone!");
					return true;
				}

			}

			return true;
		}
		

		if (cmd.getName().equalsIgnoreCase("setzone") && player != null) {
			try {
				String name = fixzName(args[1].replace(".", "").replace(":", ""));
				ActionZone zone = zh.getActionZone(name);
				if (zone == null) {
					Location l = player.getLocation();
					zone = new ActionZone(name, l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName());
					zh.addActionZone(zone);
					player.sendMessage(ChatColor.GREEN + "New zone created!");
				} else {
					if (args[0].equalsIgnoreCase("p1")) {
						zone.setP1(player);
						player.sendMessage(ChatColor.GREEN + "Zone location p1 has been set!");
					} else if (args[0].equalsIgnoreCase("p2")) {
						zone.setP2(player);
						player.sendMessage(ChatColor.GREEN + "Zone location p2 has been set!");
					}
				}
			} catch (Exception e) {
				player.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setzone ['p1'/'p2'] [name]");
			}
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("setregion") && player != null) {
			try {
				String name = fixzName(args[1].replace(".", "").replace(":", ""));
				Zone zone = zh.getZone(name);
				if (zone == null) {
					Location l = player.getLocation();
					zone = new Zone(name, l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName());
					zh.addZone(zone);
					player.sendMessage(ChatColor.GREEN + "New zone created!");
				} else {
					if (args[0].equalsIgnoreCase("p1")) {
						zone.setP1(player);
						player.sendMessage(ChatColor.GREEN + "Zone location p1 has been set!");
					} else if (args[0].equalsIgnoreCase("p2")) {
						zone.setP2(player);
						player.sendMessage(ChatColor.GREEN + "Zone location p2 has been set!");
					}
				}
			} catch (Exception e) {
				player.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setregion ['p1'/'p2'] [name]");
			}
			return true;
		}
		

		if (cmd.getName().equalsIgnoreCase("setlocation") && player != null) {

			if (args.length == 1) {
				FileConfiguration l = yh.gFC("locations");

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

				FileConfiguration l = yh.gFC("locations");
				String teststring = l.getString(location);
				if (teststring == null) {
					location = fixlName(location);
					teststring = l.getString(location);
				}
				if (teststring == null) {
					player.sendMessage(ChatColor.RED + "That location doesn't exist!");
					return true;
				}

				// Gets new location.
				FileConfiguration locs = yh.gFC("locations");
				Double x = locs.getDouble(location + ".x");
				Double y = locs.getDouble(location + ".y");
				Double z = locs.getDouble(location + ".z");
				float yaw = player.getLocation().getYaw();
				float pitch = player.getLocation().getPitch();
				World w = Bukkit.getWorld(locs.getString(location + ".world"));

				// Sets location to new location.
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

		if (cmd.getName().equalsIgnoreCase("deletezone")) {

			if (args.length == 1) {
				String zone = args[0];
				FileConfiguration zones = yh.gFC("zones");
				String testzone = zones.getString(zone);

				if (testzone != null) {

					zones.set(zone, null);
					zh.deleteZone(zone);
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
				FileConfiguration l = yh.gFC("locations");
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
					ArrayList<String> zo = zh.getZoneNames();
					sender.sendMessage(ChatColor.AQUA + zo.toString());
				} else if (type.contains("loc")) {
					ArrayList<String> lo = new ArrayList<String>();
					Iterator<String> it = yh.gFC("locations").getKeys(false).iterator();
					while (it.hasNext()) {
						Object element2 = it.next();
						String ele = element2.toString();
						lo.add(ele);
					}
					Collections.sort(lo, String.CASE_INSENSITIVE_ORDER);
					sender.sendMessage(ChatColor.AQUA + lo.toString());
				} else if (type.contains("mob")) {
					Spawnmob sm = new Spawnmob();
					sender.sendMessage(ChatColor.AQUA + sm.getMobs().toString());
				} else if (type.contains("eff")) {
					Potioneffect pe = new Potioneffect();
					sender.sendMessage(ChatColor.AQUA + pe.getEffects().toString());
				} else if (type.contains("act")) {
					sender.sendMessage(ChatColor.AQUA + ActionType.values().toString());
				}

			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /actionlist [type]");
			}
			return true;
		}



		if (cmd.getName().equalsIgnoreCase("password")) {
			if (args.length == 1) {
				String password = args[0];
				FileConfiguration locs = yh.gFC("locations");
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
				FileConfiguration zones = yh.gFC("entrancelist");
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
				FileConfiguration zones = yh.gFC("entrancelist");
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
				ActionPlayer ap = op.getActionPlayer(player.getName());
				if (ap.isMonitoring()) {
					ap.setMonitoring(false);
					player.sendMessage(ChatColor.BLUE + "You are no longer monitoring action events.");
				} else {
					ap.setMonitoring(true);
					player.sendMessage(ChatColor.BLUE + "You are now monitoring action events.");
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /azmonitor");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("togglebypass")) {
			if (args.length == 1) {
				ActionPlayer ap = op.getActionPlayer(player.getName());
				String action = args[0].toLowerCase();
				if (ActionType.fromString(action) == ActionType.INVALID && !action.equalsIgnoreCase("all")) {
					sender.sendMessage(ChatColor.RED + "That action does not exist!");
					return true;
				}
				if (action.equalsIgnoreCase("all")) {
					boolean ignoreall = ap.isIgnoringAll();
					if (ignoreall) {
						ap.unignoreAll();
						player.sendMessage(ChatColor.BLUE + "You will now be affected by all actions.");
					} else {
						ap.ignoreAll();
						player.sendMessage(ChatColor.BLUE + "You will no longer be affected by any actions.");
					}
					return true;
				}
				ActionType actionType = ActionType.fromString(action);
				if (ap.isIgnoringAction(actionType)) {
					ap.unignoreAction(actionType);
					player.sendMessage(ChatColor.BLUE + "You will now be affected by " + action + " actions.");
				} else {
					ap.ignoreAction(actionType);
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
				FileConfiguration zones = yh.gFC("zones");
				String testzone = zones.getString(zone);

				if (testzone != null) {
					Restorezone sz = new Restorezone();
					sz.storeZone(this, zone, player);
					sender.sendMessage(ChatColor.GREEN + "Zone saved!");

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
				FileConfiguration zones = yh.gFC("zones");
				String testzone = zones.getString(zone);

				if (testzone != null) {
					Restorezone sz = new Restorezone();
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
				FileConfiguration zones = yh.gFC("zones");
				String testzone = zones.getString(zone);

				if (testzone != null) {
					Restorezone sz = new Restorezone();
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
				FileConfiguration config = yh.gFC("zones");
				config.set("swapzones.sql-threads", threads);
				sender.sendMessage(ChatColor.GREEN + "SQL thread count set!");
				return true;
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters. Use /setsqlthreads [threads]");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("azstats")) {
			Collection<ActionPlayer> players1 = op.getPlayers(1);
			Collection<ActionPlayer> players2 = op.getPlayers(2);
			Collection<ActionPlayer> players3 = op.getPlayers(3);
			String names1 = "";
			String names2 = "";
			String names3 = "";
			for (ActionPlayer ap:players1) {names1 += ap.getPlayer().getName() + ",";}
			for (ActionPlayer ap:players2) {names2 += ap.getPlayer().getName() + ",";}
			for (ActionPlayer ap:players3) {names3 += ap.getPlayer().getName() + ",";}
			sender.sendMessage(ChatColor.BLACK + "-----------------------------------------------------");
			sender.sendMessage(ChatColor.WHITE + "There are currently: " + ChatColor.DARK_PURPLE + "" + zh.getZones().size() + ChatColor.WHITE + " zones.");
			sender.sendMessage(ChatColor.WHITE + "There are currently: " + ChatColor.DARK_PURPLE + "" + yh.gFC("locations").getKeys(false).size() + ChatColor.WHITE + " locations.");
			sender.sendMessage(ChatColor.WHITE + "Thread 1: " + ChatColor.DARK_PURPLE + "" + names1);
			sender.sendMessage(ChatColor.WHITE + "Thread 2: " + ChatColor.DARK_PURPLE + "" + names2);
			sender.sendMessage(ChatColor.WHITE + "Thread 3: " + ChatColor.DARK_PURPLE + "" + names3);
			sender.sendMessage(ChatColor.BLACK + "-----------------------------------------------------");

			return true;
		}

		if (cmd.getName().equalsIgnoreCase("forcerestore")) {
			
			  Placeblocks sb = new Placeblocks(); sb.forceRestore();
			  Replaceblocks rb = new Replaceblocks(); rb.forceRestore();
			  sender.sendMessage(ChatColor.GOLD + "Restoration complete!");
			  
			  return true;
			 
		}

		if (cmd.getName().equalsIgnoreCase("togglezone")) {

			if (args.length == 1) {
				String zone = args[0];
				FileConfiguration z = yh.gFC("zones");
				String testzone = z.getString(zone);
				if (testzone != null) {
					// delete after updating all zones
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
			ActionPlayer ap = op.getActionPlayer(player.getName());
			if (!ap.isDebug()) {
				ap.setDebug(true);
				sender.sendMessage(ChatColor.GOLD + "Zone Debug Mode Enabled!");
			} else {
				ap.setDebug(false);
				sender.sendMessage(ChatColor.GOLD + "Zone Debug Mode Disabled!");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("browsezones")) {
			try {
				if (args.length > 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Invalid Parameters.  Use /browsezones [Search string] (page)");
					return true;
				}
				// Gets the search string.
				String input = args[0].toLowerCase();

				// Gets the page number if given.
				int page;
				if (args.length == 1) {
					page = 1;
				} else {
					page = Integer.parseInt(args[1]);
				}
				ArrayList<String> names = zh.getZoneNames();
				ArrayList<String> rnames = new ArrayList<String>();
				int i = 0;
				while (i < names.size()) {
					String cname = names.get(i);
					if (cname.startsWith(input)) {
						rnames.add(cname);
					}
					i++;
				}
				// Alphabetizes arraylist.
				Collections.sort(rnames, String.CASE_INSENSITIVE_ORDER);
				int numberpage = page * 10;
				int count = 0;
				int rsize = rnames.size();
				double maxpages = rsize / 10;
				maxpages = Math.ceil(maxpages);
				int maxpi = (int) maxpages + 1;
				sender.sendMessage(ChatColor.RED + "Page " + ChatColor.WHITE + "(" + ChatColor.RED + "" + page + ChatColor.WHITE + "/" + ChatColor.RED + "" + maxpi + ChatColor.WHITE + ")");
				while (count < numberpage) {
					if (count > ((page * 10) - 11)) {
						if (count < rsize) {
							String iname = rnames.get(count);
							sender.sendMessage("�b" + iname + " [A]" + yh.gFC("zones").getString(iname + ".action") + " [EA]" + yh.gFC("zones").getString(iname + ".exit-action"));
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
				// Gets the search string.
				String input = args[0].toLowerCase();

				// Gets the page number if given.
				int page;
				if (args.length == 1) {
					page = 1;
				} else {
					page = Integer.parseInt(args[1]);
				}
				ArrayList<String> names = new ArrayList<String>();

				Iterator<String> it = yh.gFC("locations").getKeys(false).iterator();
				while (it.hasNext()) {
					names.add(it.next().toString());
				}

				ArrayList<String> rnames = new ArrayList<String>();
				int i = 0;
				while (i < names.size()) {
					String cname = names.get(i);
					if (cname.startsWith(input)) {
						rnames.add(cname);
					}
					i++;
				}
				// Alphabetizes arraylist.
				Collections.sort(rnames, String.CASE_INSENSITIVE_ORDER);
				int numberpage = page * 10;
				int count = 0;
				int rsize = rnames.size();
				double maxpages = rsize / 10;
				maxpages = Math.ceil(maxpages);
				int maxpi = (int) maxpages + 1;
				sender.sendMessage(ChatColor.RED + "Page " + ChatColor.WHITE + "(" + ChatColor.RED + "" + page + ChatColor.WHITE + "/" + ChatColor.RED + "" + maxpi + ChatColor.WHITE + ")");
				while (count < numberpage) {
					if (count > ((page * 10) - 11)) {
						if (count < rsize) {
							String iname = rnames.get(count);
							sender.sendMessage("�b" + iname);
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

}
