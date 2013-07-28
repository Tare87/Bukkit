package de.zeltclan.tare.zeltcarts.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.zeltclan.tare.zeltcarts.api.service.DataManager;
import de.zeltclan.tare.zeltcarts.api.util.BlockUtils;
import de.zeltclan.tare.zeltcarts.test.util.LanguageUtils;
import de.zeltclan.tare.zeltcarts.test.util.LogUtils;
import de.zeltclan.tare.zeltcarts.test.util.MessageUtils;
import de.zeltclan.tare.zeltcarts.test.util.UpdateNotifier;

public class ZeltCarts_Test extends JavaPlugin {
	
	private LanguageUtils lang = new LanguageUtils("localization", "en");
	
	private FileConfiguration config;
	
	private DataManager dataManager;

	private TreeSet<String> functionSet = null;
	
	@Override
	public void onEnable() {
		// Build Sets
		this.buildSets();
		// Load configuration and enable plugin
		this.enable();
		// Log end of onEnable()
		LogUtils.info(this, lang.getString("plugin_enabled", new Object[] {this.getDescription().getName(), this.getDescription().getVersion()}));
	}
	
	@Override
	public void onDisable() {
		this.disable();
		// Log end of onDisable()
		LogUtils.info(this, lang.getString("plugin_disabled", new Object[] {this.getDescription().getName(), this.getDescription().getVersion()}));
	}
	
	private void enable() {
		// Get FileConfiguration
		this.reloadConfig();
		config = this.getConfig();
		// Check if ConfigurationSection "General" exists
		if (config.getConfigurationSection("General") == null) {
			config.createSection("General");
		}
		// Check if ConfigurationSection "Update" exists
		if (config.getConfigurationSection("Update") == null) {
			config.createSection("Update");
		}
		// Check if configuration "lang" exists
		if (config.getConfigurationSection("General").get("lang") == null) {
			// Set Default
			config.getConfigurationSection("General").set("lang", "en");
			lang = new LanguageUtils("localization", "en");
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"lang"}));
		} else {
			// Get language
			lang = new LanguageUtils("localization", config.getConfigurationSection("General").getString("lang"));
		}
		// Log used Locale
		LogUtils.info(this,lang.getString("plugin_local", new Object[] {lang.getLanguage()}));
		// Update configfile to current version
		updateConfig();
		// Check if configuration "version" exists
		if (config.getConfigurationSection("General").get("version") == null) {
			// Set Default
			config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"version"}));
		}
		// Check if configuration "check" exists
		if (config.getConfigurationSection("Update").get("check") == null) {
			// Set Default
			config.getConfigurationSection("Update").set("check", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"check"}));
		}
		// Check if configuration "notifyOP" exists
		if (config.getConfigurationSection("Update").get("notifyOP") == null) {
			// Set Default
			config.getConfigurationSection("Update").set("notifyOP", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"notifyOP"}));
		}
		// Save configuration to config.yml for saving Defaults
		this.saveConfig();
		if (config.getConfigurationSection("Update").getBoolean("check")) {
			new UpdateNotifier(this, "http://dev.bukkit.org/server-mods/zeltcarts_test/files.rss", config.getConfigurationSection("Update").getBoolean("check"), config.getConfigurationSection("General").getString("lang"));
		}
		// Get DataManager
		if (!this.getServer().getServicesManager().isProvidedFor(DataManager.class)) {
			this.onDisable();
		}
		dataManager = this.getServer().getServicesManager().load(DataManager.class);
		// Register Listener
		this.getServer().getPluginManager().registerEvents(new TestListener(this, dataManager), this);
	}
	
	private void updateConfig() {
		config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
	}
	
	private void disable() {
		// Unregister all Listener
		HandlerList.unregisterAll(this);
	}
	
	private void buildSets() {
		// functionSet
		if (functionSet == null) {
			functionSet = new TreeSet<String>();
			functionSet.add("add");
			functionSet.add("set");
			functionSet.add("insert");
			functionSet.add("list");
			functionSet.add("size");
			functionSet.add("removeindex");
			functionSet.add("remove");
			functionSet.add("removeall");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender p_sender, Command p_command, String p_alias, String[] p_args) {
		List<String> result = new ArrayList<String>();
		switch (p_args.length) {
		case 1:
			for (String funct : functionSet) {
				if (funct.startsWith(p_args[0].toLowerCase())) {
					result.add(funct);
				}
			}
			break;
		default:
			break;
		}
		return result;
	}
	
	@Override
	public boolean onCommand(CommandSender p_sender, Command p_cmd, String p_label, String[] p_args){
		if (p_cmd.getName().equalsIgnoreCase("zeltcarts_test")) {
			switch (p_args.length) {
				case 0:
					p_sender.sendMessage(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
					p_sender.sendMessage(this.getDescription().getDescription());
					return true;
				case 1:
					// List data of target rail
					if (p_args[0].equalsIgnoreCase("list")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								List<String> dataList = dataManager.getBlock(this, block);
								if (dataList.isEmpty()) {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_data"));
								} else {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("datalist"));
									for (String data : dataList) {
										MessageUtils.info(((Player) p_sender), "- " + data);
									}
								}
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					// Remove data of target rail
					else if (p_args[0].equalsIgnoreCase("remove")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								dataManager.removeBlock(this, block);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					// Remove data of plugin
					else if (p_args[0].equalsIgnoreCase("removeall")) {
						dataManager.removeAll(this);
						return true;
					}
					// Remove data of plugin
					else if (p_args[0].equalsIgnoreCase("size")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("datasize", new Object[] {dataManager.getSize(this, block)}));
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					break;
				case 2:
					if (p_args[0].equalsIgnoreCase("add")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(p_args[1].split(";")));
								dataManager.add(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("set")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(p_args[1].split(";")));
								dataManager.set(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("removeindex")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								try {
									dataManager.removeIndex(this, block, Integer.parseInt(p_args[1]));
								} catch (NumberFormatException e) {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("not_number", new Object[] {p_args[1]}));
								}
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("removeindex")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								try {
									dataManager.removeIndex(this, block, Integer.parseInt(p_args[1]));
								} catch (NumberFormatException e) {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("not_number", new Object[] {p_args[1]}));
								}
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					break;
				case 3:
					if (p_args[0].equalsIgnoreCase("add")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								final String dataString = p_args[1] + " " + p_args[2];
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(";")));
								dataManager.add(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("insert")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(p_args[2].split(";")));
								try {
									dataManager.insert(this, block, dataList, Integer.parseInt(p_args[1]));
								} catch (NumberFormatException e) {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("not_number", new Object[] {p_args[1]}));
								}
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("set")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								final String dataString = p_args[1] + " " + p_args[2];
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(";")));
								dataManager.set(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					break;
				default:
					if (p_args[0].equalsIgnoreCase("add")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								String dataString = "";
								for (int i = 1; i < p_args.length; i++) {
									dataString += p_args[i] + " ";
								}
								dataString.trim();
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(";")));
								dataManager.add(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("insert")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								String dataString = "";
								for (int i = 2; i < p_args.length; i++) {
									dataString += p_args[i] + " ";
								}
								dataString.trim();
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(";")));
								try {
									dataManager.insert(this, block, dataList, Integer.parseInt(p_args[1]));
								} catch (NumberFormatException e) {
									MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("not_number", new Object[] {p_args[1]}));
								}
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					} else if (p_args[0].equalsIgnoreCase("set")) {
						if (p_sender instanceof Player) {
							final Block block = ((Player) p_sender).getTargetBlock(null, 100);
							if (BlockUtils.isRail(block)) {
								String dataString = "";
								for (int i = 1; i < p_args.length; i++) {
									dataString += p_args[i] + " ";
								}
								dataString.trim();
								ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(";")));
								dataManager.set(this, block, dataList);
							} else {
								MessageUtils.info(((Player) p_sender), "[" + this.getName() + "] " + lang.getString("no_rail"));
							}
							return true;
						}
					}
					break;
			}
		}
		return false; 
	}
}