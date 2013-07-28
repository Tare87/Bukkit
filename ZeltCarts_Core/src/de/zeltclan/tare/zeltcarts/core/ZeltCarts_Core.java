package de.zeltclan.tare.zeltcarts.core;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.zeltclan.tare.zeltcarts.api.service.DataManager;
import de.zeltclan.tare.zeltcarts.core.util.LanguageUtils;
import de.zeltclan.tare.zeltcarts.core.util.LogUtils;
import de.zeltclan.tare.zeltcarts.core.util.UpdateNotifier;

public class ZeltCarts_Core extends JavaPlugin {
	
	private boolean activeListener;
	
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
		// Check if ConfigurationSection "DataManager" exists
		if (config.getConfigurationSection("DataManager") == null) {
			config.createSection("DataManager");
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
		// Check if configuration "saveIntervall" exists
		if (config.getConfigurationSection("DataManager").get("saveIntervall") == null) {
			// Set Default
			config.getConfigurationSection("DataManager").set("saveIntervall", 18000);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"saveIntervall"}));
		}
		// Check if configuration "saveBroadcast" exists
		if (config.getConfigurationSection("DataManager").get("saveBroadcast") == null) {
			// Set Default
			config.getConfigurationSection("DataManager").set("saveBroadcast", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"saveBroadcast"}));
		}
		// Save configuration to config.yml for saving Defaults
		this.saveConfig();
		if (config.getConfigurationSection("Update").getBoolean("check")) {
			new UpdateNotifier(this, "http://dev.bukkit.org/server-mods/zeltcarts_core/files.rss", config.getConfigurationSection("Update").getBoolean("check"), config.getConfigurationSection("General").getString("lang"));
		}
		// Register DataManager as Service
		dataManager = new DataManager(this, config.getConfigurationSection("General").getString("lang"), config.getConfigurationSection("DataManager").getInt("saveIntervall"), config.getConfigurationSection("DataManager").getBoolean("saveBroadcast"));
		this.getServer().getServicesManager().register(DataManager.class, dataManager, this, ServicePriority.Normal);
		// Register Listener
		this.getServer().getPluginManager().registerEvents(new EventTranslator(), this);
		activeListener = true;
	}
	
	private void updateConfig() {
		config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
	}
	
	private void disable() {
		// Unregister all Listener
		HandlerList.unregisterAll(this);
		// Cancel task of Scheduler
		this.getServer().getScheduler().cancelTasks(this);
		// Unregister Service
		this.getServer().getServicesManager().unregisterAll(this);
		// Save DataManager
		dataManager.save(this);
	}
	
	private void buildSets() {
		// functionSet
		if (functionSet == null) {
			functionSet = new TreeSet<String>();
			functionSet.add("clearplugin");
			functionSet.add("clearworld");
			functionSet.add("cleandata");
			functionSet.add("save");
			functionSet.add("status");
			functionSet.add("toggle");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender p_sender, Command p_command, String p_alias, String[] p_args) {
		List<String> result = new ArrayList<String>();
		switch (p_args.length) {
			default:
				break;
		}
		return result;
	}
	
	@Override
	public boolean onCommand(CommandSender p_sender, Command p_cmd, String p_label, String[] p_args){
		if (p_cmd.getName().equalsIgnoreCase("zeltcarts_core")) {
			switch (p_args.length) {
				case 0:
					p_sender.sendMessage(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
					p_sender.sendMessage(this.getDescription().getDescription());
					return true;
				case 1:
					// Clean data
					if (p_args[0].equalsIgnoreCase("cleandata")) {
						dataManager.cleanData(this);
						return true;
					}
					// Save data to file
					else if (p_args[0].equalsIgnoreCase("save")) {
						dataManager.save(this);
						return true;
					}
					// Shows if translator is active
					else if (p_args[0].equalsIgnoreCase("status")) {
						if (activeListener) {
							p_sender.sendMessage("[" + this.getName() + "] " + lang.getString("listener_active"));
						} else {
							p_sender.sendMessage("[" + this.getName() + "] " + lang.getString("listener_inactive"));
						}
						return true;
					}
					// Toggles active state of listener
					else if (p_args[0].equalsIgnoreCase("toggle")) {
						if (activeListener) {
							HandlerList.unregisterAll(this);
						} else {
							this.getServer().getPluginManager().registerEvents(new EventTranslator(), this);
						}
						activeListener = !activeListener;
						return true;
					}
					break;
				case 2:
					// Clear data of plugin
					if (p_args[0].equalsIgnoreCase("clearplugin")) {
						dataManager.clearPlugin(this, p_args[1]);
						return true;
					}
					// Clears data of world
					else if (p_args[0].equalsIgnoreCase("clearworld")) {
						dataManager.clearWorld(this, p_args[1]);;
						return true;
					}
					break;
				default:
					break;
			}
		}
		return false; 
	}
}