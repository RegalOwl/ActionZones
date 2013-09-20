package regalowl.actionzones;



import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;



/**
 * 
 * 
 * This class deals with player accounts.
 * 
 */
public class Account {	
	private Economy economy;
	
	
	
	Account(Economy e) {
		economy = e;
	}
	

	
	/**
	 * 
	 * 
	 * This function determines if a players balance is greater than the given amount of money.
	 * 
	 */
	public boolean checkFunds(Player player, double money) {
		if (economy != null) {
			boolean result = false;
			String name = player.getName();
			if ((economy.getBalance(name) - money) >= 0) {
				result = true;
			}
			return result;
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
			return false;
		}
	}
	
	/**
	 * 
	 * 
	 * This function withdraws money from a player's account.
	 * 
	 */
	public void withdraw(Player player, double money){
		if (economy != null) {
			String name = player.getName();
			economy.withdrawPlayer(name, money);
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	
	/**
	 * 
	 * 
	 * This function withdraws money from a player's account.
	 * 
	 */
	public void withdrawAccount(String name, double money){
		if (economy != null) {
			economy.withdrawPlayer(name, money);
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	/**
	 * 
	 * 
	 * This function deposits money into a player's account.
	 * 
	 */
	public void deposit(Player player, double money){		
		if (economy != null) {
			String name = player.getName();
			economy.depositPlayer(name, money);
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	
	/**
	 * 
	 * 
	 * This function deposits money into a player's account.
	 * 
	 */
	public void depositAccount(String name, double money){		
		if (economy != null) {
			economy.depositPlayer(name, money);
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	


	
	
	
	
	/**
	 * 
	 * 
	 * This function sets an account's balance.
	 * 
	 */
	public void setBalance(String name, double balance){		
		if (economy != null) {
			if (economy.hasAccount(name)) {
				economy.withdrawPlayer(name, economy.getBalance(name));
				economy.depositPlayer(name, balance);
			} else {
				economy.createPlayerAccount(name);
				economy.depositPlayer(name, balance);
			}
			
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	
	
	/**
	 * 
	 * 
	 * This function tests if an account exists.
	 * 
	 */
	public boolean checkAccount(String name){
		boolean hasaccount = false;
		if (economy != null) {
			if (economy.hasAccount(name)) {
				hasaccount = true;
			}
			return hasaccount;
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
	    	return false;
		}
	}
	


	
	
	
	/**
	 * 
	 * 
	 * This function returns the current balance of an account.
	 * 
	 */
	public double getBalance(String account){		
		if (economy != null) {
			
			return economy.getBalance(account);
			
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
	    	return 0;
		}
	}
	
	
	
	
	
	/**
	 * 
	 * 
	 * This function creates a new account if it doesn't already exist.
	 * 
	 */
	public void createAccount(String account){		
		if (economy != null) {

			if (!economy.hasAccount(account)) {
				setBalance(account, 0);
			}
			
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	
	/**
	 * 
	 * 
	 * This function deletes an account.
	 * 
	 */
	public void deleteAccount(String account){		
		if (economy != null) {

			if (economy.hasAccount(account)) {
				//TODO  Currently don't know how.
			}
			
		} else {
			Bukkit.broadcast(ChatColor.DARK_RED + "No economy plugin detected! No money can be gained or lost.", "hyperconomy.error");
	    	Logger log = Logger.getLogger("Minecraft");
	    	log.info("No economy plugin detected! No money can be gained or lost. Please read the installation guide here: http://dev.bukkit.org/server-mods/hyperconomy/pages/quick-installation-guide/");
		}
	}
	
	
}
