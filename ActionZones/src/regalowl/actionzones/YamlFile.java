package regalowl.actionzones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;



/**
 * 
 *This class handles the items, config, and enchants yaml files.
 *
 */
public class YamlFile {

    FileConfiguration config;
    FileConfiguration zones;
    FileConfiguration locations;
    FileConfiguration inventory;
    FileConfiguration blocks;
    FileConfiguration swapzones;
    FileConfiguration entrancelist;
    FileConfiguration players;
    File configFile;
    File zonesFile;      
    File locationsFile; 
    File inventoryFile;
    File blocksFile;
    File swapzonesFile;
    File entrancelistFile;
    File playersFile;
    

    private ActionZones az;
    
	/**
	 * 
	 * This is run when the plugin is enabled, it calls the firstRun() method to create the yamls if they don't exist and then loads the yaml files with the loadYamls() method.
	 * 
	 */
	public void YamlEnable(ActionZones atz) {
		az = atz;

        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "config.yml");  
        zonesFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "zones.yml");   
        locationsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "locations.yml");
        inventoryFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "inventory.yml");
        blocksFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "blocks.yml");
        swapzonesFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "swapzones.yml");
        entrancelistFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "entrancelist.yml");
        playersFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ActionZones").getDataFolder(), "players.yml");
        
        try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        config = new YamlConfiguration();
        zones = new YamlConfiguration();
        locations = new YamlConfiguration();
        inventory = new YamlConfiguration();
        blocks = new YamlConfiguration();
        swapzones = new YamlConfiguration();
        entrancelist = new YamlConfiguration();
        players = new YamlConfiguration();
        loadYamls();
		
	}
	
	
	/**
	 * 
	 * Checks if the yamls exist, and if they don't, it copies the default files to the plugin's folder.
	 * 
	 */
    private void firstRun() throws Exception {
        if(!configFile.exists()){              
            configFile.getParentFile().mkdirs();         
            copy(this.getClass().getResourceAsStream("/config.yml"), configFile);
        }
        if(!zonesFile.exists()){
            zonesFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/zones.yml"), zonesFile);
        }
        if(!locationsFile.exists()){
            locationsFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/locations.yml"), locationsFile);
        }
        if(!inventoryFile.exists()){
            inventoryFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/inventory.yml"), inventoryFile);
        }
        if(!blocksFile.exists()){
            blocksFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/blocks.yml"), blocksFile);
        }
        if(!swapzonesFile.exists()){
            swapzonesFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/swapzones.yml"), swapzonesFile);
        }
        if(!entrancelistFile.exists()){
            entrancelistFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/entrancelist.yml"), entrancelistFile);
        }
        if(!playersFile.exists()){
            playersFile.getParentFile().mkdirs();
            copy(this.getClass().getResourceAsStream("/players.yml"), playersFile);
        }
    }


	/**
	 * 
	 * This actually copies the files.
	 * 
	 */
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 
	 * This loads the yaml files.
	 * 
	 */
    public void loadYamls() {
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad config.yml file.", "actionzones.error");
        }
        try {
        	zones.load(zonesFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad zones.yml file.", "actionzones.error");
        }
        try {
        	locations.load(locationsFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad locations.yml file.", "actionzones.error");
        }
        try {
        	inventory.load(inventoryFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad inventory.yml file.", "actionzones.error");
        }
        try {
        	blocks.load(blocksFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad blocks.yml file.", "actionzones.error");
        }
        try {
        	swapzones.load(swapzonesFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad swapzones.yml file.", "actionzones.error");
        }
        try {
        	entrancelist.load(entrancelistFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad entrancelist.yml file.", "actionzones.error");
        }
        try {
        	players.load(playersFile);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.broadcast(ChatColor.DARK_RED + "Bad players.yml file.", "actionzones.error");
        }
    }

	/**
	 * 
	 * This saves the yaml files.
	 * 
	 */
    public void saveYamls() {
        try {
            config.save(configFile); 
            zones.save(zonesFile);
            locations.save(locationsFile);
            inventory.save(inventoryFile);
            blocks.save(blocksFile);
            swapzones.save(swapzonesFile);
            entrancelist.save(entrancelistFile);
            players.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
	/**
	 * 
	 * This saves the yaml files.
	 * 
	 */
    public void saveInventories() {
        try {
            inventory.save(inventoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
	/**
	 * 
	 * This saves the yaml files.
	 * 
	 */
    public void saveBlocks() {
        try {
        	blocks.save(blocksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * 
	 * This saves the yaml files.
	 * 
	 */
    public void saveSwapZones() {
        try {
        	swapzones.save(swapzonesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * 
	 * This saves the yaml files.
	 * 
	 */
    public void saveEntranceList() {
        try {
        	entrancelist.save(entrancelistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

	
	/**
	 * 
	 * This gets the FileConfiguration zones.
	 * 
	 */
	public FileConfiguration getZones(){
		return zones;
	}
	
	/**
	 * 
	 * This gets the config FileConfiguraiton.
	 * 
	 */
	public FileConfiguration getConfig(){
		return config;
	}
	
	
	/**
	 * 
	 * This gets the locations FileConfiguration.
	 * 
	 */
	public FileConfiguration getLocations(){
		return locations;
	}
	
	/**
	 * 
	 * This gets the inventory FileConfiguration.
	 * 
	 */
	public FileConfiguration getInventory(){
		return inventory;
	}
	
	/**
	 * 
	 * This gets the blocks FileConfiguration.
	 * 
	 */
	public FileConfiguration getBlocks(){
		return blocks;
	}
	
	/**
	 * 
	 * This gets the swapzones FileConfiguration.
	 * 
	 */
	public FileConfiguration getSwapZones(){
		return swapzones;
	}
	
	/**
	 * 
	 * This gets the entrancelist FileConfiguration.
	 * 
	 */
	public FileConfiguration getEntranceList(){
		return entrancelist;
	}
	
	
	/**
	 * 
	 * This gets the entrancelist FileConfiguration.
	 * 
	 */
	public FileConfiguration getPlayers(){
		return players;
	}


}