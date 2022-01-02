package me.cageydinosaur.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd implements CommandExecutor {

	Main plugin;

	public Cmd(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("lifesteal"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		} else if (sender.hasPermission("lifesteal")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "Usage:");
				if (sender.hasPermission("lifesteal.add"))
					sender.sendMessage(ChatColor.GREEN + "/lifesteal add <player> - gives player 1 heart");
				if (sender.hasPermission("lifesteal.remove"))
					sender.sendMessage(ChatColor.GREEN + "/lifesteal remove <player> - takes one heart from player");
				if (!(sender.hasPermission("lifesteal.take")) && !(sender.hasPermission("lifesteal.add"))) {
				}
				return true;
			} else if (args.length > 0) {

				if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("lifesteal.reload")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					plugin.reloadConfig();
					sender.sendMessage("Reloaded the config");
					return true;

				} else if (args[0].equalsIgnoreCase("rl")) {
					if (!sender.hasPermission("lifesteal.reloadlives")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					plugin.addLivesToList();

					sender.sendMessage("Added respawns to list");
					return true;

				} else if (args[0].equalsIgnoreCase("lc")) {
					if (!sender.hasPermission("lifesteal.reloadlifeconfig")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					plugin.addLivesToConfig();
					sender.sendMessage("Added respawns to respawns.yml");
					return true;

				} else if (args[0].equalsIgnoreCase("add")) {
					if (!(sender.hasPermission("lifesteal.add"))) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					if (args.length == 1) {
						sender.sendMessage(ChatColor.RED + "You must specify a player name");
						return true;
					} else if (args.length == 2) {
						Player recievingPlayer = Bukkit.getPlayer(args[1]);
						if (recievingPlayer == null) {
							sender.sendMessage(ChatColor.RED + "That player is not online");
							return true;
						}

						int lifeAmt = (plugin.getPlayerLives(recievingPlayer) + 1);
						plugin.removeInfo(recievingPlayer);
						plugin.addInfo(recievingPlayer, lifeAmt);
						recievingPlayer.setMaxHealth(lifeAmt * 2);
						sender.sendMessage(ChatColor.RED + recievingPlayer.getDisplayName() + ChatColor.GREEN
								+ " now has " + ChatColor.RED + lifeAmt + ChatColor.GREEN + " hearts");

						return true;
					}

				} else if (args[0].equalsIgnoreCase("remove")) {
					if (!(sender.hasPermission("lifesteal.remove"))) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					if (args.length == 1) {
						sender.sendMessage(ChatColor.RED + "You must specify a player name");
						return true;
					} else if (args.length == 2) {
						Player recievingPlayer = Bukkit.getPlayer(args[1]);
						if (recievingPlayer == null) {
							sender.sendMessage(ChatColor.RED + "That player is not online");
							return true;
						}

						int lifeAmt = (plugin.getPlayerLives(recievingPlayer));
						if (lifeAmt == 1) {
							sender.sendMessage(
									ChatColor.GREEN + "That player already has the minimum amount of hearts");
						} else {
							lifeAmt = (plugin.getPlayerLives(recievingPlayer) - 1);
							plugin.removeInfo(recievingPlayer);
							plugin.addInfo(recievingPlayer, lifeAmt);
							recievingPlayer.setMaxHealth(lifeAmt * 2);
							sender.sendMessage(ChatColor.RED + recievingPlayer.getDisplayName() + ChatColor.GREEN
									+ " now has " + ChatColor.RED + lifeAmt + ChatColor.GREEN + " hearts");

							return true;
						}
					}

				}
			}
			return true;
		}
		return false;
	}
}