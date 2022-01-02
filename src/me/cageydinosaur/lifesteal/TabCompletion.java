package me.cageydinosaur.lifesteal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {

	Main plugin;

	public TabCompletion(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> commands = new ArrayList<>();
		if (args.length == 1) {
			if (sender.hasPermission("lifesteal")) {
				if (sender.hasPermission("lifesteal.add")) {
					commands.add("add");
				}
				if (sender.hasPermission("lifesteal.remove")) {
					commands.add("remove");
				}
				if (sender.hasPermission("lifesteal.reload")) {
					commands.add("reload");
				}
				if (sender.hasPermission("lifesteal.testing")) {
					commands.add("rl");
					commands.add("lc");
				}
				return commands;
			}
		}
		return null;
	}

}
