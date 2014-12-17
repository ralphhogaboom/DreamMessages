package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Plugin plugin;
	
    File configFile;
    FileConfiguration config;
	public static List<String> dreams;
	public static List<String> nightmares;
    
	public void onEnable() {	
		plugin = this;
		registerEvents(this, new EventListener());
		getCommand("dreams").setExecutor(new Commands());
		readDreams();
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

	public void readDreams() {
		// This also reads in the nightmares.
		dreams = (List<String>) this.getConfig().getList("dreams");
		nightmares = (List<String>) this.getConfig().getList("nightmares");
	}
	
	public void saveDreams() {
		// This also saves nightmares.
		
	}
	
	public void addDream(String dreamText, boolean isNightmare) {
		// This also adds nightmares.
		if (isNightmare) {
			nightmares.add(dreamText);
		} else {
			dreams.add(dreamText);
		}
	}
	
	public void deleteDream() {
		// This also deletes nightmares.
		
	}
	
}
