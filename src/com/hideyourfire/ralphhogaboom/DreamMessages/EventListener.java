package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventListener implements Listener {
	
//		dreams[0] = "";
//		dreams[1] = "";
//		dreams[2] = "";
//		dreams[3] = "";
//		dreams[4] = "";
//		dreams[5] = "";
//		dreams[6] = "";
//		dreams[7] = "";
//		dreams[8] = "";
//		dreams[9] = "";
//		dreams[10] = "";
//		dreams[11] = "";
//		dreams[12] = "";
//		dreams[13] = "";
//		dreams[14] = "";
//		dreams[15] = "";
//		dreams[16] = "";
//		dreams[17] = "";
//		dreams[18] = "";
//		dreams[19] = "";
//		dreams[20] = "";
	
	public boolean day() {
	    Server server = Main.getPlugin().getServer();
	    long time = server.getWorld("survival").getTime();
	    return time < 12300 || time > 23850;
	}
		
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	@EventHandler
	public void onWakeup(PlayerBedLeaveEvent event) {
		if (day()) {
			Player player = event.getPlayer();
			Main.getPlugin().getLogger().info(player.getDisplayName() + " slept: But it's day now, so let's proceed.");
			int random = randInt(0, 12);
			if (random < 12) {
				String[] dreams = new String[12];
				dreams[0] = "Last night you dreamed of a black and red snake. The snake flew into the air, away from you.";
				dreams[1] = "You awaken, having dreamed of a vast underwater temple, containing a new kind of treasure.";
				dreams[2] = "You arise refreshed, having dreamed of a cat named Xander who lived to be -2900 years old.";
				dreams[3] = "Awake! Last night you dreamt of a big cat named Yvonne who lived to be 44 years old.";
				dreams[4] = "Good morning; last night you had a vivid dream of a sheep named Zeus, who lived to be 1330 years old.";
				dreams[5] = "You awaken having dreams of power, flowing out from books, and flying into your head.";
				dreams[6] = "In your dreams, you had visions of power flowing through your body - and out like a beacon, into the Lord of Ghasts.";
				dreams[7] = "You awake having dreamed of the world turning green, then brown from heat, then white from ice, then black - all life having drained into the oceans.";
				dreams[8] = "You awake from a disturbing dream where you joined the Lord of Ghasts, and worked in secret to hurt your fellow players.";
				dreams[9] = "You awake from a nightmare of a tower, burning endlessly through the night while laughter rings out.";
				dreams[10] = "In your dreams, a trusted friend hands you a diamond sword, but you are too weak to hold it. Overhead, you hear the sound of ghasts.";
				dreams[11] = "You dream this night of a powerful force flowing through you, giving you powers you never dreamed of before.";
				Main.getPlugin().getLogger().info(player.getDisplayName() + " dreamed: " + dreams[random]);
				player.sendMessage(ChatColor.AQUA + dreams[random]);
				if (random == 8 || random == 10) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
				}
				if (random == 7 || random == 9) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 1));
				}
			}
		} else {
			Main.getPlugin().getLogger().info("It's still night - don't do a dream event.");
		}
	}

	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {

	}

	
}
