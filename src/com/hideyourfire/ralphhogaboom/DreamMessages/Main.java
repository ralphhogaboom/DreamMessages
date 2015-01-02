package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.io.File;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lib.PatPeter.SQLibrary.SQLite;

public class Main extends JavaPlugin {

	private static Plugin plugin;
	private SQLite sqlite;
	private boolean debug = false;
	
    File configFile;
    FileConfiguration config;
    
	public void onEnable() {	
		plugin = this;
		this.saveDefaultConfig(); // For first run, save default config file.
		this.getConfig();
		debug = this.getConfig().getBoolean("debug");
		Main.getPlugin().getLogger().info("Debug output? " + doDebug());
		sqlConnection();
		sqlTableCheck("dreams");
		registerEvents(this, new EventListener(this));
		getCommand("dreams").setExecutor(new Commands());
	}

	@SuppressWarnings("deprecation")
	public void sqlTableCheck(String tableName) {
	    if(sqlite.checkTable(tableName)){
	    	this.getLogger().info("Database opened; table '" + tableName + "' found.");
	    	return;
	    } else {
			try {
				sqlite.query("CREATE TABLE IF NOT EXISTS `dreams` (	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,	`isNightmare`	INTEGER DEFAULT 0, `dream`	TEXT );");
			} catch (SQLException e) {
				if (doDebug()) {
					e.printStackTrace();
				}
			}
	        plugin.getLogger().info("Table '" + tableName + "' has been created");
	    }
	}

	public void sqlConnection() {
		sqlite = new SQLite(Main.getPlugin().getLogger(), "DreamMessages", Main.getPlugin().getDataFolder().getAbsolutePath(), "dreams");
		try {
			sqlite.open();
		} catch (Exception e) {
			if (doDebug()) {
				Main.getPlugin().getLogger().info(e.getMessage());
			}
		}
	}
	
	private void registerEvents(org.bukkit.plugin.Plugin plugin, Listener...listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	public void onDisable() {
		plugin = null; // Stops memory leaks.
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

	public boolean doDebug() {
		return debug;
	}
}
