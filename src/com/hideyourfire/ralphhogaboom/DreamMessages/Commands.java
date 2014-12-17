package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dreams")) {
			if (args.length < 1) {
				Main.getPlugin().getLogger().info("No arguments detected.");
				sender.sendMessage("Type " + ChatColor.GOLD + "/dreams list" + ChatColor.WHITE + " to show all dreams currently loaded.");
				sender.sendMessage("Type " + ChatColor.GOLD + "/dreams add" + ChatColor.WHITE + " to add a new dream, or " + ChatColor.GOLD + "/dreams delete" + ChatColor.WHITE + " to remove one.");
				return true;
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					listDreams(sender);
					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					addDreams(sender, false, args[1]);
					return true;
				}
				if (args[0].equalsIgnoreCase("delete")) {
					return true;
				}
				return true;
			}
			if (args.length == 2) {
				Main.getPlugin().getLogger().info("2 arguments detected.");
				return true;
			}
		}
		return false;
	}
	
	public void addDreams(CommandSender sender, boolean isNightmare, String dreamText) {
		if (isNightmare) {
			Main.dreams.add("whatever");
		} else {
			Main.nightmares.add("whatever");
		}
	}
	
	public void listDreams(CommandSender sender) {
		int listSize = Main.dreams.size();
		for (int i = 0; i < listSize; ++i) {
			sender.sendMessage(Main.dreams.get(i));
		}
	}
}
