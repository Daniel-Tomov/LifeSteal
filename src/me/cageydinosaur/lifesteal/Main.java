package me.cageydinosaur.lifesteal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private File customConfigFile;
	private FileConfiguration customConfig;

	public ArrayList<String> lifeList = new ArrayList<String>();
	ArrayList<String> itemLore = new ArrayList<String>();
	public void onEnable() {

		this.saveDefaultConfig();
		itemLore.add("Consume this to gain an extra heart");
		getCommand("lifesteal").setExecutor(new Cmd(this));
		getCommand("lifesteal").setTabCompleter(new TabCompletion(this));
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		Bukkit.addRecipe(getRecipe());

		this.createCustomConfig();
		this.addLivesToList();

	}

	public void onDisable() {
		this.addLivesToConfig();
	}

	public String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public ShapedRecipe getRecipe() {
		ItemStack item  = new ItemStack(Material.SUSPICIOUS_STEW);
		NamespacedKey key = new NamespacedKey(this, "heart");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Heart");
		meta.setLore(itemLore);
		meta.setCustomModelData(6789);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		item.setItemMeta(meta);
		
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		
		
		recipe.shape("ABC", "DEF", "GHI");
		recipe.setIngredient('A', Material.valueOf(this.getConfig().getString("TopLeft")));
		recipe.setIngredient('B', Material.valueOf(this.getConfig().getString("TopMiddle")));
		recipe.setIngredient('C', Material.valueOf(this.getConfig().getString("TopRight")));
		recipe.setIngredient('D', Material.valueOf(this.getConfig().getString("MiddleLeft")));
		recipe.setIngredient('E', Material.valueOf(this.getConfig().getString("MiddleMiddle")));
		recipe.setIngredient('F', Material.valueOf(this.getConfig().getString("MiddleRight")));
		recipe.setIngredient('G', Material.valueOf(this.getConfig().getString("BottomLeft")));
		recipe.setIngredient('H', Material.valueOf(this.getConfig().getString("BottomMiddle")));
		recipe.setIngredient('I', Material.valueOf(this.getConfig().getString("BottomRight")));
 		
		return recipe;
	}
	public String getPlayerName() {
		String playerName = "hey";
		for (String i : this.lifeList) {
			String[] split = i.split(", ", 2);
			playerName = split[0];
		}
		return playerName;
	}

	public void addInfo(Player player, int lifesL) {
		this.lifeList.add(player.getDisplayName() + ", " + Integer.toString(lifesL));
	}

	public void removeInfo(Player player) {
		for (String i : lifeList) {
			String[] split = i.split(", ", 2);
			if (split[0].equals(player.getDisplayName())) {
				lifeList.remove(split[0] + ", " + split[1]);
				return;
			}
		}
	}

	public boolean ifLives(Player player) {
		for (String i : this.lifeList) {
			String[] split = i.split(", ", 2);
			if (Bukkit.getPlayer(split[0]) == player) {
				return true;
			}
		}
		return false;
	}

	public int getPlayerLives(Player player) {
		int livesLeft = 3;
		for (String i : this.lifeList) {
			String[] split = i.split(", ", 2);
			if (Bukkit.getPlayer(split[0]) == player) {
				livesLeft = Integer.parseInt(split[1]);
				return livesLeft;
			}
		}
		return livesLeft;
	}

	public void addLivesToConfig() {
		ArrayList<String> list = new ArrayList<String>();
		// String[] list = null;
		list.clear();
		/* this.clearConfig(); */
		for (String r : this.lifeList) {
			list.add(r);
		}
		this.getCustomConfig().set("lives", list);
		this.saveCustomConfig();
	}

	public void addLivesToList() {
		this.lifeList.clear();
		for (String i : this.customConfig.getStringList("lives")) {
			this.lifeList.add(i);

		}
	}

	public void clearConfig() {
		this.getCustomConfig().set("lives", "");
		this.saveCustomConfig();
	}

	public FileConfiguration getCustomConfig() {
		return this.customConfig;
	}

	public void saveCustomConfig() {
		try {
			this.customConfig.save(customConfigFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createCustomConfig() {
		customConfigFile = new File(getDataFolder(), "lives.yml");
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			saveResource("lives.yml", true);
		}

		customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}