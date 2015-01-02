package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

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
		
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
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
						int random = randInt(0, rs.getInt("totalDreams")); // This leaves a chance one won't dream.
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
	
	public void OldCodeIWillProbablyReuse() {
		{
			Player player = null;
			int random = randInt(0, 15);
			if (random < 8) {
				String[] dreams = new String[11];
				dreams[0] = "Tonight your dreams convince you thus: there are two projectionists, and both will lie to you.";
				dreams[1] = "This night you dreamed of standing, with your back to the wall - and Mrcreeper and Cyclone56724 held swords to your throat.";
				dreams[2] = "You awake from dreams of Cyclone56724 - he cast spells unlike any you've seen, but each spell drains the life of those around him.";
				dreams[3] = "You awake from dreams of Mrcreeper - he cast spells unlike any you've seen, but each spell drains the life of those around him.";
				dreams[4] = "In your dreams, you saw a castle in a volcano, surrounded by monsters. At the top, a portal opened and spilled forth the Lord of Ghasts unto the world.";
				dreams[5] = "You dreamed that Mrcreeper traded your soul for his own, and sold you out to the Lord of Ghasts.";
				dreams[6] = "You dreamed that Cyclone56724 traded your soul for his own, and sold you out to the Lord of Ghasts.";
				dreams[7] = "You awake from disturbing dreams; you are filled with sadness. The Lord of Ghasts is coming, there is no hope for us left.";
				dreams[8] = "Last night you had terrible sleep; filled with dreams of the Lord of Ghasts burning our world.";
				if (player.getName().equalsIgnoreCase("Mrcreeper") || player.getName().equalsIgnoreCase("ralphhogaboom") || player.getName().equalsIgnoreCase("cyclone56724")) {
					random = randInt(0, 5);
					dreams[0] = "YOU MUST RECOVER THE GREAT BOOK OF SPELLS FOR ME TO ENTER THE WORLD";
					dreams[1] = "I MUST HAVE THE BOOK OF SPELLS";
					dreams[2] = "THE BOOK OF SPELLS IS ENDERGHAST AND AKRANON";
					dreams[6] = "CLIMB INTO THE FIRE OF THE GREAT GHAST PORTAL / THE WAY IS SET";
					dreams[3] = "ENTER THE SKYLAND TOWER OF SORCERY / IT MUST BE UNDONE";
					dreams[4] = "BRING AGAIN THE BOOK OF SPELLS / ALL THIS IS YOUR DOING";
					dreams[5] = "YOU HAVE DONE THIS BEFORE / YOU HAVE LIVED COUNTLESS LIVES";
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
					player.sendMessage(ChatColor.AQUA + dreams[random]);
					Main.getPlugin().getLogger().info(player.getName() + " dreamed: " + dreams[random]);
				} else {
					player.sendMessage(ChatColor.AQUA + dreams[random]);
					Main.getPlugin().getLogger().info(player.getName() + " dreamed: " + dreams[random]);
				}
				if (random == 8 || random == 10) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
				}
				if (random == 7 || random == 9) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 1));
				}
			}
		// } else {
			if (thisInstance.doDebug()) {
				Main.getPlugin().getLogger().info(player.getDisplayName() + " got up, but it's still night - don't do a dream event.");
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
