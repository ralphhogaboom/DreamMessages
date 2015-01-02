package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.sql.ResultSet;
import java.sql.SQLException;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hideyourfire.ralphhogaboom.DreamMessages.Main;

public class EventListener implements Listener {

	Main thisInstance;
	private SQLite sqlite;
	
	public EventListener(Main instance) {
		  thisInstance = instance;
		}
	
	public boolean day(String world) {
	    Server server = Main.getPlugin().getServer();
	    long time = server.getWorld(world).getTime();
	    return time < 12300 || time > 23850;
	}

	
	@EventHandler
	public void onWakeup(PlayerBedLeaveEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("dreams.get")) {
			if (day(player.getWorld().getName())) {
				if (thisInstance.doDebug()) {
					Main.getPlugin().getLogger().info(player.getDisplayName() + " slept: But it's day now, so let's proceed.");
				}
				try {
					// get total number of dreams & nightmares.
					
					sqlConnection();
					String sqlQuery = "SELECT COUNT(id) AS totalDreams FROM dreams;";
					ResultSet rs = sqlite.query(sqlQuery);
					while (rs.next()) {
						Main.getPlugin().getLogger().info("Total dreams + nightmares on file: " + rs.getInt("totalDreams"));
					}
					sqlQuery = "SELECT * FROM dreams ORDER BY RANDOM() LIMIT 1;";
					rs = sqlite.query(sqlQuery);
					while (rs.next()) {
						player.sendMessage(ChatColor.AQUA + rs.getString("dream"));
						if (rs.getInt("isNightmare") != 0) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
							player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 1));
						}
					}
				} catch (SQLException e) {
					if (thisInstance.doDebug()) {
						Main.getPlugin().getLogger().info(e.getMessage());
					}
				}
				
			}	
		}
	}
	
	private void sqlConnection() {
		sqlite = new SQLite(Main.getPlugin().getLogger(), "DreamMessages", Main.getPlugin().getDataFolder().getAbsolutePath(), "dreams");
		try {
			sqlite.open();
		} catch (Exception e) {
			if (thisInstance.doDebug()) {
				Main.getPlugin().getLogger().info(e.getMessage());
			}
			Main.getPlugin().getLogger().info("Unable to open database; players will not receive dreams.");
		}
	}
}
