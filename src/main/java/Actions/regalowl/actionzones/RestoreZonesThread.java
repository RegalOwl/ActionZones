package regalowl.actionzones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.bukkit.configuration.file.FileConfiguration;

public class RestoreZonesThread {

	private ActionZones az;
	private RestoreZones sz;
	private int savecompletetaskid;
	private int counter;
	private ArrayList<String> statements;
	private boolean savecomplete;
	private int tid;
	
	private String username;
	private String password;
	private int port;
	private String host;
	private String database;
	
	public void swapZonesThread(ActionZones atz, RestoreZones swapz, ArrayList<String> states, int threadid) {
		az = atz;
		sz = swapz;
		statements = states;
		tid = threadid;
		
		savecomplete = false;
		counter = 0;
		
		FileConfiguration config = az.getYaml().getConfig();
		username = config.getString("sql-connection.username");
		password = config.getString("sql-connection.password");
		port = config.getInt("sql-connection.port");
		host = config.getString("sql-connection.host");
		database = config.getString("sql-connection.database");
		
		
		az.getServer().getScheduler().scheduleAsyncDelayedTask(az, new Runnable() {
			public void run() {
		        try {
					Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
					Statement state = connection.createStatement();
					counter = 0;
					while (counter < statements.size()) {
						state.execute(statements.get(counter));
						counter++;
					}
					
			        state.close();
			        connection.close();
			        savecomplete = true;
				} catch (SQLException e) {
					e.printStackTrace();
					sz.abortSave();
				}
			}
		}, 0L);
	

		
		savecompletetaskid = az.getServer().getScheduler().scheduleSyncRepeatingTask(az, new Runnable() {
			public void run() {
				sz.setBlocksProcessed(tid, counter);
				if (savecomplete) {
					saveComplete();
					//p.sendMessage(ChatColor.GREEN + "Save complete!" + " (Thread: " + tid + ")");
				} else {
					//p.sendMessage(ChatColor.GREEN + "Saving Zone: " + counter + " blocks processed." + " (Thread: " + tid + ")");
				}
			}
		}, 0L, 200L);
		
	
	
}
	
	
	public void saveComplete() {
		az.getServer().getScheduler().cancelTask(savecompletetaskid);
	}
}
