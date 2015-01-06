package com.hideyourfire.ralphhogaboom.DreamMessages;

import java.sql.ResultSet;
import java.sql.SQLException;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	Main thisInstance;
	public SQLite sqlite;
	
	public Commands(Main instance) {
		thisInstance = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Check if sender has permissions
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!(player.hasPermission("dreams.admin"))) {
				sender.sendMessage(ChatColor.YELLOW + "Sorry, you don't have permission to use this command. Missing 'dreams.admin' permissions node.");
				return false;
			}
		}
		if (cmd.getName().equalsIgnoreCase("dreams")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("list")) {
					// List dreams; fetch SQL query and list all dreams. We'll page 'em later.
					try {
						sqlConnection();
						String sqlQuery = "SELECT id, dream, isNightmare FROM dreams ORDER BY id;";
						ResultSet rs = sqlite.query(sqlQuery);
						int i = 0;
						while (rs.next()) {
							++i;
							if (rs.getInt("isNightmare") == 0) {
								sender.sendMessage("" + ChatColor.DARK_GREEN + i + ": " + rs.getString("dream"));
							} else {
								sender.sendMessage("" + ChatColor.RED + i + ": " + rs.getString("dream"));
							}
						}
						rs.close();
						if (i < 1) {
							sender.sendMessage("There are no dreams or nightmares recorded. Type " + ChatColor.GOLD + "/dreams add" + ChatColor.WHITE + " to begin creating dreams.");
						}
					} catch (SQLException e) {
						sender.sendMessage(ChatColor.RED + "Can't list dreams - an error occured (the database file is probably locked).");
						if (thisInstance.doDebug()) {
							e.printStackTrace();
						}
					}
				}
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length > 1) {
						// Add dreams
						if (args[1].equalsIgnoreCase("dream") || args[1].equalsIgnoreCase("nightmare")) {
							// Get the whole sentence, minus the first two args
							StringBuilder dream = new StringBuilder();
							for (int s = 2; s < args.length; s++) {
								dream.append(args[s].replace("'", "''"));
								dream.append(" ");
							}
							if (thisInstance.doDebug()) {
								Main.getPlugin().getLogger().info("StringBuilder dream: " + dream);
							}
							// Figger if it's a dream or it ain't
							int isNightmare = 0;
							if (args[1].equalsIgnoreCase("nightmare")) {
								isNightmare = -1;
							}
							String sqlQuery = "INSERT INTO dreams (isNightmare, dream) VALUES (" + isNightmare + ", '" + dream.toString() + "');";
							if (thisInstance.doDebug()) {
								Main.getPlugin().getLogger().info("sqlQuery: " + sqlQuery);
							}
							try {
								sqlConnection();
								sqlite.query(sqlQuery);
								sender.sendMessage(ChatColor.DARK_GREEN + "Dream added successfully.");
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Missing " + ChatColor.GOLD + "dream|nightmare" + ChatColor.WHITE + " - check your syntax and try again.");
							sender.sendMessage("  Ex: " + ChatColor.GOLD + "/dreams add nightmare This night you dreamed of a creeper, standing at the foot of your bed!");
						}
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Missing " + ChatColor.GOLD + "dream|nightmare" + ChatColor.WHITE + " - check your syntax and try again.");
						sender.sendMessage("  Ex: " + ChatColor.GOLD + "/dreams add nightmare This night you dreamed of a creeper, standing at the foot of your bed!");
					}
				}
				if (args[0].equalsIgnoreCase("delete")) {
					if (args.length == 2) {
						// Check if it's a number.
						if (isNumeric(args[1].toString())) {
							String sqlQuery = "SELECT id, dream, isNightmare FROM dreams ORDER BY id;";
							ResultSet rs;
							try {
								sqlConnection();
								rs = sqlite.query(sqlQuery);
								int i = 0;
								while (rs.next()) {
									++i;
									if (i == Integer.parseInt(args[1].toString())) {
										// Delete it
										sqlQuery = "DELETE FROM dreams WHERE id = " + rs.getInt("id") + ";";
									}
								}
								rs.close();
								try {
									sqlite.query(sqlQuery);
									sender.sendMessage("Dream " + args[1].toString() + " has been deleted.");
								} catch (SQLException e) {
									e.printStackTrace();
									sender.sendMessage("An internal error occurred; check the console log for specific errors.");
								}
								rs.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Unrecognized command format.");
							sender.sendMessage("  Example: " + ChatColor.GOLD + "/dreams delete 21");
							return false;
						}
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Unrecognized command format.");
						sender.sendMessage("  Example: " + ChatColor.GOLD + "/dreams delete 21");
						return false;
					}
				}
			} else {
				sender.sendMessage(ChatColor.YELLOW + "Unrecognized command format.");
				sender.sendMessage("Format is " + ChatColor.GOLD + "/dreams (add|delete|list)" + ChatColor.WHITE + ".");
				sender.sendMessage("Example: " + ChatColor.GOLD + "/dreams list");
				sender.sendMessage("    or: " + ChatColor.GOLD + "/dreams add nightmare Tonight you dreamed of a creeper standing at the foot of your bed!");
				sender.sendMessage("    or: " + ChatColor.GOLD + "/dreams delete 21");
				return false;
			}
		}
		return true;
	}
	
	public void sqlConnection() {
		sqlite = new SQLite(Main.getPlugin().getLogger(), "DreamMessages", Main.getPlugin().getDataFolder().getAbsolutePath(), "dreams");
		try {
			sqlite.open();
		} catch (Exception e) {
			if (thisInstance.doDebug()) {
				Main.getPlugin().getLogger().info(e.getMessage());
			}
		}
	}

	public static boolean isNumeric(String str) {  
		try {  
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}
