package me.cageydinosaur.lifesteal;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

	Main plugin;

	public Events(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player joiner = e.getPlayer();
		if (!(plugin.ifLives(joiner))) {
			plugin.addInfo(joiner, 10);
			joiner.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + "10" + ChatColor.GREEN
					+ " hearts. If you die you will " + ChatColor.RED + "lose one" + ChatColor.GREEN
					+ " heart. If you " + ChatColor.RED + "kill" + ChatColor.GREEN + " other players, you will "
					+ ChatColor.RED + "gain one" + ChatColor.GREEN
					+ " heart. Have fun playing!");
		} else {
			joiner.setMaxHealth(plugin.getPlayerLives(joiner) * 2);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player recievingPlayer = e.getEntity();

		if (!(plugin.ifLives(recievingPlayer))) {
			plugin.addInfo(recievingPlayer, 3);
		}

		int lifeAmt = plugin.getPlayerLives(recievingPlayer);
		lifeAmt = plugin.getPlayerLives(recievingPlayer);
		if (lifeAmt == 1) {
			return;
		}
		if (e.getEntity().getKiller() instanceof Player) {
			Player killer = e.getEntity().getKiller();
			lifeAmt = (plugin.getPlayerLives(killer) + 1);
			plugin.removeInfo(killer);
			plugin.addInfo(killer, lifeAmt);
			killer.setMaxHealth(lifeAmt * 2);
			killer.sendMessage(ChatColor.GREEN + "You have killed " + ChatColor.RED + recievingPlayer.getDisplayName()
					+ ChatColor.GREEN + " and have received one heart. Your total is " + ChatColor.RED + lifeAmt
					+ ChatColor.GREEN + " hearts");

			lifeAmt = plugin.getPlayerLives(recievingPlayer);
			if (lifeAmt == 1) {
				return;
			} else {
				lifeAmt = (lifeAmt - 1);
				plugin.removeInfo(recievingPlayer);
				plugin.addInfo(recievingPlayer, lifeAmt);
				recievingPlayer.setMaxHealth(lifeAmt * 2);
				recievingPlayer.sendMessage(ChatColor.GREEN + "You have died" + ChatColor.GREEN + " and now have "
						+ ChatColor.RED + lifeAmt + ChatColor.GREEN + " hearts");

			}
		}

	}
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		Player eater = e.getPlayer();
		if (eater.hasPermission("lifesteal.use")) {
			if (e.getItem().getItemMeta().hasCustomModelData()) {
				if (e.getItem().getItemMeta().getCustomModelData() == 6789) {
					int lifeAmt = (plugin.getPlayerLives(eater) + 1);
					plugin.removeInfo(eater);
					plugin.addInfo(eater, lifeAmt);
					eater.setMaxHealth(lifeAmt);
				}
			} else {
				eater.sendMessage(ChatColor.RED + "You do not have permission to eat hearts");
			}
		}
	}
}
