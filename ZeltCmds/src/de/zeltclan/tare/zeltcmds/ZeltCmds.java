package de.zeltclan.tare.zeltcmds;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import de.zeltclan.tare.zeltcmds.enums.Category;
import de.zeltclan.tare.zeltcmds.enums.Default;
import de.zeltclan.tare.zeltcmds.enums.MessageType;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;
import de.zeltclan.tare.zeltcmds.utils.LanguageUtils;
import de.zeltclan.tare.zeltcmds.utils.UpdateNotifier;
import de.zeltclan.tare.zeltcmds.utils.VersionUtils;

public class ZeltCmds extends JavaPlugin {
	
	private static LanguageUtils lang = new LanguageUtils("localization", "en");
	
	private FileConfiguration config;
	
	private CmdExecutor cmdExecutor;

	private TreeSet<String> functionSet = null;
	private TreeSet<String> parameterSet = null;
	private TreeSet<String> permissionSet = null;
	
	@Override
	public void onEnable() {
		// Build Sets
		this.buildSets();
		// Load configuration and enable plugin
		this.enable();
		// Log number of commands enabled
		final int cmdCount = cmdExecutor.countCommands();
		if (cmdCount > 0) {
			this.getLogger().info(lang.getString("commands_enabled", new Object[] {cmdCount}));
		} else {
			this.getLogger().info(lang.getString("commands_no_command"));
			this.getLogger().info(lang.getString("commands_examples"));
		}
		// Log number of aliases enabled
		final int aliasCount = cmdExecutor.countAliases();
		this.getLogger().info(lang.getString("aliases_enabled", new Object[] {aliasCount}));
		// Log end of onEnable()
		this.getLogger().info(lang.getString("plugin_enabled", new Object[] {this.getDescription().getName(), this.getDescription().getVersion()}));
	}
	
	@Override
	public void onDisable() {
		this.disable();
		// Log end of onDisable()
		this.getLogger().info(lang.getString("plugin_disabled", new Object[] {this.getDescription().getName(), this.getDescription().getVersion()}));
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
		// Check if ConfigurationSection "Commands" exists
		if (config.getConfigurationSection("Commands") == null) {
			config.createSection("Commands");
		}
		// Check if ConfigurationSection "Aliases" exists
		if (config.getConfigurationSection("Aliases") == null) {
			config.createSection("Aliases");
		}
		// Check if configuration "lang" exists
		if (config.getConfigurationSection("General").get("lang") == null) {
			// Set Default
			config.getConfigurationSection("General").set("lang", "en");
			lang = new LanguageUtils("localization", "en");
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"lang"}));
		} else {
			// Get language
			lang = new LanguageUtils("localization", config.getConfigurationSection("General").getString("lang"));
		}
		// Log used Locale
		this.getLogger().info(lang.getString("plugin_local", new Object[] {lang.getLanguage()}));
		// Update configfile to current version
		updateConfig();
		// Check if configuration "logCmd" exists
		if (config.getConfigurationSection("General").get("logCmd") == null) {
			// Set Default
			config.getConfigurationSection("General").set("logCmd", true);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"logCmd"}));
		}
		// Check if configuration "logAlias" exists
		if (config.getConfigurationSection("General").get("logAlias") == null) {
			// Set Default
			config.getConfigurationSection("General").set("logAlias", true);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"logAlias"}));
		}
		// Check if configuration "version" exists
		if (config.getConfigurationSection("General").get("version") == null) {
			// Set Default
			config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"version"}));
		}
		// Check if configuration "pageentries" exists
		if (config.getConfigurationSection("General").get("pageentries") == null) {
			// Set Default
			config.getConfigurationSection("General").set("pageentries", 5);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"pageentries"}));
		}
		// Check if configuration "casesensitive" exists
		if (config.getConfigurationSection("General").get("casesensitive") == null) {
			// Set Default
			config.getConfigurationSection("General").set("casesensitive", true);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"casesensitive"}));
		}
		// Check if configuration "check" exists
		if (config.getConfigurationSection("Update").get("check") == null) {
			// Set Default
			config.getConfigurationSection("Update").set("check", true);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"check"}));
		}
		// Check if configuration "notifyOP" exists
		if (config.getConfigurationSection("Update").get("notifyOP") == null) {
			// Set Default
			config.getConfigurationSection("Update").set("notifyOP", true);
			this.getLogger().info(lang.getString("option_not_set", new Object[] {"notifyOP"}));
		}
		// Save configuration to config.yml for saving Defaults
		this.saveConfig();
		// Create manager
		cmdExecutor = new CmdExecutor(this, config.getConfigurationSection("General").getBoolean("logCmd"), config.getConfigurationSection("General").getBoolean("logAlias"), config.getConfigurationSection("General").getBoolean("casesensitive"));
		// Load commands
		for (String cmd : config.getConfigurationSection("Commands").getKeys(false)) {
			String msg = "";
			// Get message if set
			if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).contains("msg")) {
				msg = config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("msg");
			}
			// Add command
			this.addConfigCommand(cmd, config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category"), config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("type"), config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("perm"), msg);
		}
		// Load Aliases
		for (String alias : config.getConfigurationSection("Aliases").getKeys(false)) {
			// Add alias
			this.addConfigAlias(alias, config.getConfigurationSection("Aliases").getString(alias));
		}
		if (config.getConfigurationSection("Update").getBoolean("check")) {
			new UpdateNotifier(this, "http://dev.bukkit.org/server-mods/zeltcmds/files.rss", config.getConfigurationSection("Update").getBoolean("check"), config.getConfigurationSection("General").getString("lang"));
		}
		// Register Listener
		this.getServer().getPluginManager().registerEvents(cmdExecutor, this);
	}
	
	private void updateConfig() {
		if (VersionUtils.isNewer(config.getConfigurationSection("General").getString("version"), "1.0.0")) {
			for (String cmd : config.getConfigurationSection("Commands").getKeys(false)) {
				if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category").equalsIgnoreCase("time")) {
					config.getConfigurationSection("Commands").getConfigurationSection(cmd).set("category", "WORLDTIME");
				}
				if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category").equalsIgnoreCase("weather")) {
					config.getConfigurationSection("Commands").getConfigurationSection(cmd).set("category", "WORLDWEATHER");
				}
			}
			this.getLogger().info(lang.getString("config_updated", new Object[] {"1.0.0"}));
		}
		if (VersionUtils.isNewer(config.getConfigurationSection("General").getString("version"), "1.4.0")) {
			if (config.getConfigurationSection("General").get("logMsg") != null) {
				config.getConfigurationSection("General").set("logCmd", config.getConfigurationSection("General").getBoolean("logMsg"));
				config.getConfigurationSection("General").set("logMsg", null);
			}
			this.getLogger().info(lang.getString("config_updated", new Object[] {"1.4.0"}));
		}
		if (VersionUtils.isNewer(config.getConfigurationSection("General").getString("version"), "1.7.0")) {
			if (config.getConfigurationSection("General").get("checkUpdate") != null) {
				config.getConfigurationSection("Update").set("check", config.getConfigurationSection("General").getBoolean("checkUpdate"));
				config.getConfigurationSection("General").set("checkUpdate", null);
			}
			for (String cmd : config.getConfigurationSection("Commands").getKeys(false)) {
				if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category").equalsIgnoreCase("player") && config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("type").equalsIgnoreCase("clear")) {
					config.getConfigurationSection("Commands").getConfigurationSection(cmd).set("type", "CLEARINVENTORY");
				}
			}
			this.getLogger().info(lang.getString("config_updated", new Object[] {"1.7.0"}));
		}
		config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
	}
	
	private void disable() {
		// Clear every command and permission
		for (String cmd : cmdExecutor.listCommands()) {
			cmdExecutor.removeCommand(cmd);
		}
		// Unregister all Listener
		HandlerList.unregisterAll(this);
		// Set cmdExecutor to null
		cmdExecutor = null;
	}
	
	private void buildSets() {
		// functionSet
		if (functionSet == null) {
			functionSet = new TreeSet<String>();
			functionSet.add("add");
			functionSet.add("addalias");
			functionSet.add("examples");
			functionSet.add("list");
			functionSet.add("listalias");
			functionSet.add("reload");
			functionSet.add("remove");
			functionSet.add("removealias");
		}
		// parameterSet
		if (parameterSet == null) {
			parameterSet = new TreeSet<String>();
			parameterSet.add("<player_name>");
			parameterSet.add("<player_nick>");
			parameterSet.add("<player_x>");
			parameterSet.add("<player_y>");
			parameterSet.add("<player_z>");
			parameterSet.add("<player_world>");
			parameterSet.add("<item_name>");
			parameterSet.add("<item_id>");
			parameterSet.add("<item_data>");
			parameterSet.add("<param>");
			parameterSet.add("<param?>");
			parameterSet.add("<param*>");
			parameterSet.add("<cmd>");
		}
		// permissionSet
		if (permissionSet == null) {
			permissionSet = new TreeSet<String>();
			for (PermissionDefault permission : PermissionDefault.values()) {
				permissionSet.add(permission.name().toLowerCase());
			}
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
			case 2:
				if (p_args[0].equalsIgnoreCase("list")) {
					final int cmdCount = cmdExecutor.countCommands();
					final int pageEntries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
					int pageCount;
					if (cmdCount == 0) {
						pageCount = 1;
					} else {
						pageCount = ((cmdCount - (cmdCount % pageEntries)) / pageEntries);
						if ((cmdCount % pageEntries) != 0) {
							pageCount++;
						}
					}
					for (int i = 1; i <= pageCount; i++) {
						if (String.valueOf(i).startsWith(p_args[1].toLowerCase())) {
							result.add(String.valueOf(i));
						}
					}
				} else if (p_args[0].equalsIgnoreCase("listalias")) {
					final int aliasCount = cmdExecutor.countAliases();
					final int pageEntries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
					int pageCount;
					if (aliasCount == 0) {
						pageCount = 1;
					} else {
						pageCount = ((aliasCount - (aliasCount % pageEntries)) / pageEntries);
						if ((aliasCount % pageEntries) != 0) {
							pageCount++;
						}
					}
					for (int i = 1; i <= pageCount; i++) {
						if (String.valueOf(i).startsWith(p_args[1].toLowerCase())) {
							result.add(String.valueOf(i));
						}
					}
				} else if (p_args[0].equalsIgnoreCase("remove")) {
					final String[] cmdList = cmdExecutor.listCommands();
					for (String cmd : cmdList) {
						if (cmd.startsWith(p_args[1].toLowerCase())) {
							result.add(cmd);
						}
					}
				} else if (p_args[0].equalsIgnoreCase("removealias")) {
					final String[] aliasList = cmdExecutor.listAliases();
					for (String alias : aliasList) {
						if (alias.startsWith(p_args[1].toLowerCase())) {
							result.add(alias);
						}
					}
				}
				break;
			case 3:
				if (p_args[0].equalsIgnoreCase("add")) {
					for (String category : CmdChooser.listCategories()) {
						if (category.startsWith(p_args[2].toLowerCase())) {
							result.add(category);
						}
					}
				}
				if (p_args[0].equalsIgnoreCase("addalias")) {
					for (String parameter : parameterSet) {
						if (parameter.startsWith(p_args[2].toLowerCase())) {
							result.add(parameter);
						}
					}
				}
				break;
			case 4:
				if (p_args[0].equalsIgnoreCase("add")) {
					Category category = CmdChooser.getCategory(p_args[2]);
					Set<String> typeSet = CmdChooser.listTypes(category);
					if (typeSet != null) {
						for (String type : typeSet) {
							if (type.startsWith(p_args[3].toLowerCase())) {
								result.add(type);
							}
						}
					} else {
						switch (category) {
							case SERVERTIME:
							case WORLDTIME:
								for (String permission : permissionSet) {
									if (permission.startsWith(p_args[3].toLowerCase())) {
										result.add(permission);
									}
								}
								break;
							default:
								break;
						}
					}
				}
				if (p_args[0].equalsIgnoreCase("addalias")) {
					for (String parameter : parameterSet) {
						if (parameter.startsWith(p_args[3].toLowerCase())) {
							result.add(parameter);
						}
					}
				}
				break;
			case 5:
				if (p_args[0].equalsIgnoreCase("add")) {
					Category category = CmdChooser.getCategory(p_args[2]);
					Set<String> typeSet = CmdChooser.listTypes(category);
					if (typeSet != null) {
						if (typeSet.contains(p_args[3].toLowerCase())) {
							for (String permission : permissionSet) {
								if (permission.startsWith(p_args[4].toLowerCase())) {
									result.add(permission);
								}
							}
						}
					}
				}
				if (p_args[0].equalsIgnoreCase("addalias")) {
					for (String parameter : parameterSet) {
						if (parameter.startsWith(p_args[4].toLowerCase())) {
							result.add(parameter);
						}
					}
				}
				break;
			default:
				if (p_args[0].equalsIgnoreCase("addalias")) {
					for (String parameter : parameterSet) {
						if (parameter.startsWith(p_args[p_args.length-1].toLowerCase())) {
							result.add(parameter);
						}
					}
				}
				break;
		}
		return result;
	}
	
	@Override
	public boolean onCommand(CommandSender p_sender, Command p_cmd, String p_label, String[] p_args){
		if (p_cmd.getName().equalsIgnoreCase("zeltcmds")) {
			switch (p_args.length) {
				case 0:
					if (p_sender instanceof Player) {
						p_sender.sendMessage(ChatColor.GREEN + this.getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
						p_sender.sendMessage(ChatColor.GREEN + this.getDescription().getDescription());
						p_sender.sendMessage(ChatColor.GREEN + lang.getString("usage", new Object[] {(p_sender instanceof Player ? "/" : "") + "zeltcmds [add|addalias|examples|list|listalias|reload|remove|removealias] [param]*"}));
						p_sender.sendMessage(ChatColor.GREEN + "add - " + lang.getString("zeltcmds_add_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_add_description"));
						p_sender.sendMessage(ChatColor.GREEN + "addalias - " + lang.getString("zeltcmds_addalias_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_addalias_description"));
						p_sender.sendMessage(ChatColor.GREEN + "examples - " + lang.getString("zeltcmds_examples_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_examples_description"));
						p_sender.sendMessage(ChatColor.GREEN + "list - " + lang.getString("zeltcmds_list_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_list_description"));
						p_sender.sendMessage(ChatColor.GREEN + "listalias - " + lang.getString("zeltcmds_listalias_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_listalias_description"));
						p_sender.sendMessage(ChatColor.GREEN + "reload - " + lang.getString("zeltcmds_reload_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_reload_description"));
						p_sender.sendMessage(ChatColor.GREEN + "remove - " + lang.getString("zeltcmds_remove_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_remove_description"));
						p_sender.sendMessage(ChatColor.GREEN + "removealias - " + lang.getString("zeltcmds_removealias_usage"));
						p_sender.sendMessage(ChatColor.GREEN + " > " + lang.getString("zeltcmds_removealias_description"));
					} else {
						this.getLogger().info(this.getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
						this.getLogger().info(this.getDescription().getDescription());
						this.getLogger().info(lang.getString("usage", new Object[] {(p_sender instanceof Player ? "/" : "") + "zeltcmds [add|addalias|examples|list|listalias|reload|remove|removealias] [param]*"}));
						this.getLogger().info("add - " + lang.getString("zeltcmds_add_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_add_description"));
						this.getLogger().info("addalias - " + lang.getString("zeltcmds_addalias_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_addalias_description"));
						this.getLogger().info("examples - " + lang.getString("zeltcmds_examples_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_examples_description"));
						this.getLogger().info("list - " + lang.getString("zeltcmds_list_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_list_description"));
						this.getLogger().info("listalias - " + lang.getString("zeltcmds_listalias_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_listalias_description"));
						this.getLogger().info("reload - " + lang.getString("zeltcmds_reload_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_reload_description"));
						this.getLogger().info("remove - " + lang.getString("zeltcmds_remove_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_remove_description"));
						this.getLogger().info("removealias - " + lang.getString("zeltcmds_removealias_usage"));
						this.getLogger().info(" > " + lang.getString("zeltcmds_removealias_description"));
					}
					return true;
				case 1:
					// Dummy command
					if (p_args[0].equalsIgnoreCase("dummy")) {
						return true;
					}
					// Add examples of commands
					if (p_args[0].equalsIgnoreCase("examples")) {
						this.addExamples();
						if (p_sender instanceof Player) {
							p_sender.sendMessage("[" + this.getName() + "] " + lang.getString("command_examples"));
							this.getLogger().info(lang.getString("log_examples", new Object[] {((Player) p_sender).getDisplayName()}));
						} else {
							this.getLogger().info(lang.getString("command_examples"));
						}
						return true;
					}
					// Lists first page of commands registered by the plugin
					if (p_args[0].equalsIgnoreCase("list")) {
						final int cmdcount = cmdExecutor.countCommands();
						final int pageentries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
						int pages;
						if (cmdcount == 0) {
							pages = 1;
						} else {
							pages = ((cmdcount - (cmdcount % pageentries)) / pageentries);
							if ((cmdcount % pageentries) != 0) {
								pages++;
							}
						}
						if (p_sender instanceof Player) {
							p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("commands_list") + " (1/" + pages + ") [" + cmdcount + "]");
						} else {
							this.getLogger().info(lang.getString("commands_list") + " (1/" + pages + ") [" + cmdcount + "]");
						}
						String[] cmdList = cmdExecutor.listCommands();
						for (int i = 0; i < pageentries && i < cmdcount; i++) {
							p_sender.sendMessage(cmdList[i] + " - " + cmdExecutor.getCommandDescription(cmdList[i]));
						}
						return true;
					}
					// Lists first page of aliases registered by the plugin
					if (p_args[0].equalsIgnoreCase("listalias")) {
						final int aliascount = cmdExecutor.countAliases();
						final int pageentries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
						int pages;
						if (aliascount == 0) {
							pages = 1;
						} else {
							pages = ((aliascount - (aliascount % pageentries)) / pageentries);
							if ((aliascount % pageentries) != 0) {
								pages++;
							}
						}
						if (p_sender instanceof Player) {
							p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("aliases_list") + " (1/" + pages + ") [" + aliascount + "]");
						} else {
							this.getLogger().info(lang.getString("aliases_list") + " (1/" + pages + ") [" + aliascount + "]");
						}
						String[] aliasList = cmdExecutor.listAliases();
						for (int i = 0; i < pageentries && i < aliascount; i++) {
							p_sender.sendMessage(aliasList[i] + " > " + cmdExecutor.getAliasDescription(aliasList[i]));
						}
						return true;
					}
					// Reload configuration and commands from config.yml
					if (p_args[0].equalsIgnoreCase("reload")) {
						// Clear every command and permission
						this.disable();
						this.enable();
						// Get commands from config.yml and log number of commands enabled
						final int cmdCount = cmdExecutor.countCommands();
						if (cmdCount > 0) {
							this.getLogger().info(lang.getString("commands_enabled", new Object[] {cmdCount}));
						} else {
							this.getLogger().info(lang.getString("commands_no_command"));
							this.getLogger().info(lang.getString("commands_examples"));
						}
						// Log number of aliases enabled
						final int aliasCount = cmdExecutor.countAliases();
						this.getLogger().info(lang.getString("aliases_enabled", new Object[] {aliasCount}));
						// Register Listener
						this.getServer().getPluginManager().registerEvents(cmdExecutor, this);
						if (p_sender instanceof Player) {
							p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + " " + lang.getString("command_reload"));
							this.getLogger().info(lang.getString("log_reload", new Object[] {((Player) p_sender).getDisplayName()}));
						} else {
							this.getLogger().info(lang.getString("command_reload"));
						}
						return true;
					}
					break;
				case 2:
					// Lists given page of commands registered by the plugin
					if (p_args[0].equalsIgnoreCase("list")) {
						final int page;
						try {
							page = Integer.parseInt(p_args[1]);
						} catch (NumberFormatException e) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("not_integer", new Object[] {p_args[1]}));
							} else {
								this.getLogger().warning(lang.getString("not_integer", new Object[] {p_args[1]}));
							}
							return true;
						}
						final int cmdcount = cmdExecutor.countCommands();
						final int pageentries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
						int pages;
						if (cmdcount == 0) {
							pages = 1;
						} else {
							pages = ((cmdcount - (cmdcount % pageentries)) / pageentries);
							if ((cmdcount % pageentries) != 0) {
								pages++;
							}
						}
						if (page > pages) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("max_page", new Object[] {pages}));
							} else {
								this.getLogger().warning(lang.getString("max_page", new Object[] {pages}));
							}
							return true;
						}
						if (p_sender instanceof Player) {
							p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("commands_list") + " (" + page + "/" + pages + ") [" + cmdcount + "]");
						} else {
							this.getLogger().info(lang.getString("commands_list") + " (" + page + "/" + pages + ") [" + cmdcount + "]");
						}
						String[] cmdList = cmdExecutor.listCommands();
						int start = (page - 1) * pageentries;
						for (int i = start; i < (start + pageentries) && i < cmdcount; i++) {
							p_sender.sendMessage(cmdList[i] + " - " + cmdExecutor.getCommandDescription(cmdList[i]));
						}
						return true;
					}
					// Lists given page of aliases registered by the plugin
					if (p_args[0].equalsIgnoreCase("listalias")) {
						final int page;
						try {
							page = Integer.parseInt(p_args[1]);
						} catch (NumberFormatException e) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("not_integer", new Object[] {p_args[1]}));
							} else {
								this.getLogger().warning(lang.getString("not_integer", new Object[] {p_args[1]}));
							}
							return true;
						}
						final int aliascount = cmdExecutor.countAliases();
						final int pageentries = this.getConfig().getConfigurationSection("General").getInt("pageentries");
						int pages;
						if (aliascount == 0) {
							pages = 1;
						} else {
							pages = ((aliascount - (aliascount % pageentries)) / pageentries);
							if ((aliascount % pageentries) != 0) {
								pages++;
							}
						}
						if (page > pages) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("max_page", new Object[] {pages}));
							} else {
								this.getLogger().warning(lang.getString("max_page", new Object[] {pages}));
							}
							return true;
						}
						if (p_sender instanceof Player) {
							p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("aliases_list") + " (" + page + "/" + pages + ") [" + aliascount + "]");
						} else {
							this.getLogger().info(lang.getString("aliases_list") + " (" + page + "/" + pages + ") [" + aliascount + "]");
						}
						String[] aliasList = cmdExecutor.listAliases();
						final int start = (page - 1) * pageentries;
						for (int i = start; i < (start + pageentries) && i < aliascount; i++) {
							p_sender.sendMessage(aliasList[i] + " - " + cmdExecutor.getAliasDescription(aliasList[i]));
						}
						return true;
					}
					// Removes a command including its permissions
					if (p_args[0].equalsIgnoreCase("remove")) {
						if (cmdExecutor.existCommand(p_args[1])) {
							cmdExecutor.removeCommand(p_args[1]);
							this.removeConfig(p_args[1]);
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("command_remove", new Object[] {p_args[1]}));
								this.getLogger().info(lang.getString("log_command_remove", new Object[] {((Player) p_sender).getDisplayName(), p_args[1]}));
							} else {
								this.getLogger().info(lang.getString("command_remove", new Object[] {p_args[1]}));
							}
						} else {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("command_not_found"));
							} else {
								this.getLogger().warning(lang.getString("command_not_found"));
							}
						}
						return true;
					}
					// Removes a alias
					if (p_args[0].equalsIgnoreCase("removealias")) {
						if (cmdExecutor.existAlias(p_args[1])) {
							cmdExecutor.removeAlias(p_args[1]);
							this.removeAliasConfig(p_args[1]);
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("alias_remove", new Object[] {p_args[1]}));
								this.getLogger().info(lang.getString("log_alias_remove", new Object[] {((Player) p_sender).getDisplayName(), p_args[1]}));
							} else {
								this.getLogger().info(lang.getString("alias_remove", new Object[] {p_args[1]}));
							}
						} else {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("alias_not_found"));
							} else {
								this.getLogger().warning(lang.getString("alias_not_found"));
							}
						}
						return true;
					}
					break;
				case 3:
				case 4:
					// Adds a alias
					if (p_args[0].equalsIgnoreCase("addalias")) {
						String aliasparams = "";
						for (int i = 2; i < p_args.length; i++) {
							aliasparams += p_args[i] + (i+1 < p_args.length ? " " : "");
						}
						if (this.addSenderAlias(p_sender, p_args[1], aliasparams)) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("alias_add"));
								this.getLogger().info(lang.getString("log_alias_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], aliasparams}));
							} else {
								this.getLogger().info(lang.getString("alias_add"));
							}
						}
						return true;
					}
					break;
				default:
					// Adds a command including permissions
					if (p_args[0].equalsIgnoreCase("add")) {
						String msg = "";
						for (int i = 5; i < p_args.length; i++) {
							msg += p_args[i] + (i+1 < p_args.length ? " " : "");
						}
						if (this.addSenderCommand(p_sender, p_args[1], p_args[2], p_args[3], p_args[4], msg)) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("command_add"));
								this.getLogger().info(lang.getString("log_command_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], p_args[2], p_args[3], p_args[4], msg}));
							} else {
								this.getLogger().info(lang.getString("command_add"));
							}
						}
						return true;
					}
					// Adds a alias
					if (p_args[0].equalsIgnoreCase("addalias")) {
						String aliasparams = "";
						for (int i = 2; i < p_args.length; i++) {
							aliasparams += p_args[i] + (i+1 < p_args.length ? " " : "");
						}
						if (this.addSenderAlias(p_sender, p_args[1], aliasparams)) {
							if (p_sender instanceof Player) {
								p_sender.sendMessage(ChatColor.GREEN + "[" + this.getName() + "] " + lang.getString("alias_add"));
								this.getLogger().info(lang.getString("log_alias_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], aliasparams}));
							} else {
								this.getLogger().info(lang.getString("alias_add"));
							}
						}
						return true;
					}
					break;
			}
		}
		return false; 
	}
	
	
	private void addExamples() {
		// Examples for CmdPlayerInfo
		this.addExampleCommand("info", "player", "info", "op", "");
		this.addExampleCommand("seen", "player", "seen", "true", "");
		// Examples for CmdLocation
		this.addExampleCommand("sethome", "set", "home", "true", "New homepoint set");
		// Examples for CmdMode
		this.addExampleCommand("toggle", "mode", "toggle", "op", "Gamemode changed");
		// Examples for CmdPlayer
		this.addExampleCommand("heal", "player", "heal", "op", "Fresh and healthy");
		this.addExampleCommand("kill", "player", "kill", "false", "Now you are dead");
		// Examples for CmdPort
		this.addExampleCommand("home", "port", "m2h", "true", "Home sweet home");
		this.addExampleCommand("all", "port", "a2m", "op", "Meetingtime");
		// Examples for CmdWorldTime
		this.addExampleCommand("day", "time", "5500", "true", "Good Morning");
		this.addExampleCommand("night", "time", "20000", "op", "Good Night");
		// Examples for CmdWorldWeather
		this.addExampleCommand("sun", "weather", "sun", "true", "The sun is shining again");
		this.addExampleCommand("rain", "weather", "rain", "op", "Just singing in the rain");
	}
	
	private void addConfigCommand(String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (cmdExecutor.checkEntry(p_cmd)) {
			this.getLogger().warning(lang.getString("entry_exists", new Object[] {p_cmd}));
			return;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			this.getLogger().warning(lang.getString("commands_no_category", new Object[] {p_category}));
			return;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			this.getLogger().warning(lang.getString("commands_no_type", new Object[] {p_type}));
			return;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		CmdParent cmd;
		final RequireListener listener = CmdChooser.getListener(category, type);
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, listener, p_msg);
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, listener, p_msg);
		}
		if (cmd != null) {
			cmdExecutor.addCommand(p_cmd, cmd);
		} else {
			this.getLogger().warning(lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
		}
	}
	
	private void addExampleCommand(String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (cmdExecutor.checkEntry(p_cmd)) {
			return;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			this.getLogger().warning(lang.getString("commands_no_category", new Object[] {p_category}));
			return;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			this.getLogger().warning(lang.getString("commands_no_type", new Object[] {p_type}));
			return;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		CmdParent cmd;
		final RequireListener listener = CmdChooser.getListener(category, type);
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, listener, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), p_type, permissionDefault.name(), p_msg);
			} else {
				this.getLogger().warning(lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				return;
			}
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, listener, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), type.name(), permissionDefault.name(), p_msg);
			} else {
				this.getLogger().warning(lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				return;
			}
		}
		cmdExecutor.addCommand(p_cmd, cmd);
	}
	
	private boolean addSenderCommand(CommandSender p_sender, String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (cmdExecutor.checkEntry(p_cmd)) {
			if (p_sender instanceof Player) {
				p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("entry_exists", new Object[] {p_cmd}));
			} else {
				this.getLogger().warning(lang.getString("entry_exists", new Object[] {p_cmd}));
			}
			return false;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			if (p_sender instanceof Player) {
				p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("commands_no_category", new Object[] {p_category}));
			} else {
				this.getLogger().warning(lang.getString("commands_no_category", new Object[] {p_category}));
			}
			return false;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			if (p_sender instanceof Player) {
				p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("commands_no_type", new Object[] {p_type}));
			} else {
				this.getLogger().warning(lang.getString("commands_no_type", new Object[] {p_type}));
			}
			return false;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		MessageType message = CmdChooser.getMessageType(category, type);
		if (message == MessageType.NOMESSAGE) {
			p_msg = null;
		}
		CmdParent cmd;
		final RequireListener listener = CmdChooser.getListener(category, type);
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, listener, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), p_type, permissionDefault.name(), p_msg);
			} else {
				if (p_sender instanceof Player) {
					p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("command_not_build"));
				} else {
					this.getLogger().warning(lang.getString("command_not_build"));
					this.getLogger().warning(lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				}
				return false;
			}
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, listener, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), type.name(), permissionDefault.name(), p_msg);
			} else {
				if (p_sender instanceof Player) {
					p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("command_not_build"));
				} else {
					this.getLogger().warning(lang.getString("command_not_build"));
					this.getLogger().warning(lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				}
				return false;
			}
		}
		cmdExecutor.addCommand(p_cmd, cmd);
		return true;
	}
	
	private void addConfig(String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (config.getConfigurationSection("Commands").getConfigurationSection(p_cmd) != null) {
			this.removeConfig(p_cmd);
		}
		config.getConfigurationSection("Commands").createSection(p_cmd);
		config.getConfigurationSection("Commands").getConfigurationSection(p_cmd).set("category", p_category);
		config.getConfigurationSection("Commands").getConfigurationSection(p_cmd).set("type", p_type);
		config.getConfigurationSection("Commands").getConfigurationSection(p_cmd).set("perm", p_dperm);
		config.getConfigurationSection("Commands").getConfigurationSection(p_cmd).set("msg", p_msg);
		this.saveConfig();
	}
	
	private void removeConfig(String p_cmd) {
		config.getConfigurationSection("Commands").set(p_cmd , null);
		this.saveConfig();
	}
	
	private void addConfigAlias(String p_alias, String p_aliasParam) {
		if (cmdExecutor.checkEntry(p_alias)) {
			this.getLogger().warning(lang.getString("entry_exists", new Object[] {p_alias}));
			return;
		}
		cmdExecutor.addAlias(p_alias, p_aliasParam);
	}
	
	private boolean addSenderAlias(CommandSender p_sender, String p_alias, String p_aliasParams) {
		if (cmdExecutor.checkEntry(p_alias)) {
			if (p_sender instanceof Player) {
				p_sender.sendMessage(ChatColor.RED + "[" + this.getName() + "] " + lang.getString("entry_exists", new Object[] {p_alias}));
			} else {
				this.getLogger().warning(lang.getString("entry_exists", new Object[] {p_alias}));
			}
			return false;
		}
		this.addAliasConfig(p_alias, p_aliasParams);
		cmdExecutor.addAlias(p_alias, p_aliasParams);
		return true;
	}
	
	private void addAliasConfig(String p_alias, String p_aliasParams) {
		if (config.getConfigurationSection("Aliases").getConfigurationSection(p_alias) != null) {
			this.removeAliasConfig(p_alias);
		}
		config.getConfigurationSection("Aliases").set(p_alias, p_aliasParams);
		this.saveConfig();
	}
	
	private void removeAliasConfig(String p_alias) {
		config.getConfigurationSection("Aliases").set(p_alias , null);
		this.saveConfig();
	}
	
	public static LanguageUtils getLanguage() {
		return lang;
	}
}