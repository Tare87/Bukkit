package de.zeltclan.tare.zeltcmds;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import de.zeltclan.tare.bukkitutils.*;
import de.zeltclan.tare.zeltcmds.enums.Category;
import de.zeltclan.tare.zeltcmds.enums.Default;
import de.zeltclan.tare.zeltcmds.enums.MessageType;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class ZeltCmds extends JavaPlugin {
	
	private static LanguageUtils lang = new LanguageUtils("localization", "en");
	
	private FileConfiguration config;
	
	private CmdExecutor cmdExecutor;
	
	private CmdUpdateNotifier cmdUpdateNotifier;
	
	@Override
	public void onEnable() {
		this.enable();
		// Log number of commands enabled
		final int cmdCount = cmdExecutor.countCommands();
		if (cmdCount > 0) {
			LogUtils.info(this, lang.getString("commands_enabled", new Object[] {cmdCount}));
		} else {
			LogUtils.info(this, lang.getString("commands_no_command"));
			LogUtils.info(this, lang.getString("commands_examples"));
		}
		// Log number of aliases enabled
		final int aliasCount = cmdExecutor.countAliases();
		LogUtils.info(this, lang.getString("aliases_enabled", new Object[] {aliasCount}));
		// Register Listener
		this.getServer().getPluginManager().registerEvents(cmdExecutor, this);
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
		// Check if configuration "logCmd" exists
		if (config.getConfigurationSection("General").get("logCmd") == null) {
			// Set Default
			config.getConfigurationSection("General").set("logCmd", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"logCmd"}));
		}
		// Check if configuration "logAlias" exists
		if (config.getConfigurationSection("General").get("logAlias") == null) {
			// Set Default
			config.getConfigurationSection("General").set("logAlias", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"logAlias"}));
		}
		// Check if configuration "version" exists
		if (config.getConfigurationSection("General").get("version") == null) {
			// Set Default
			config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"version"}));
		}
		// Check if configurationSection "pageentries" exists
		if (config.getConfigurationSection("General").get("pageentries") == null) {
			// Set Default
			config.getConfigurationSection("General").set("pageentries", 5);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"pageentries"}));
		}
		// Check if configurationSection "checkUpdate" exists
		if (config.getConfigurationSection("General").get("checkUpdate") == null) {
			// Set Default
			config.getConfigurationSection("General").set("checkUpdate", true);
			LogUtils.info(this, lang.getString("option_not_set", new Object[] {"checkUpdate"}));
		}
		// Check if ConfigurationSection "Commands" exists
		if (config.getConfigurationSection("Commands") == null) {
			config.createSection("Commands");
		}
		// Check if ConfigurationSection "Aliases" exists
		if (config.getConfigurationSection("Aliases") == null) {
			config.createSection("Aliases");
		}
		// Update configfile to current version
		updateConfig();
		// Save configuration to config.yml for saving Defaults
		this.saveConfig();
		// Create manager
		cmdExecutor = new CmdExecutor(config.getConfigurationSection("General").getBoolean("logCmd"), config.getConfigurationSection("General").getBoolean("logAlias"));
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
		if (config.getConfigurationSection("General").getBoolean("checkUpdate")) {
			cmdUpdateNotifier = new CmdUpdateNotifier("http://dev.bukkit.org/server-mods/zeltcmds/files.rss", this.getDescription().getVersion());
			if (cmdUpdateNotifier.getFileFeed() != null) {
				HashMap<String,String> result = FileFeedReader.checkUpdate(cmdUpdateNotifier.getFileFeed(), this.getDescription().getVersion());
				if (result.containsKey("error")) {
					LogUtils.warning(this, result.get("error"));
					return;
				}
				if (result.containsKey("link")) {
					LogUtils.info(this, ZeltCmds.getLanguage().getString("update_version", new Object[]{result.get("version")}));
					LogUtils.info(this, ZeltCmds.getLanguage().getString("update_changelog", new Object[]{result.get("link")}));
					LogUtils.info(this, ZeltCmds.getLanguage().getString("update_download", new Object[]{result.get("jarLink")}));
				}
				// Register Notifier
				this.getServer().getPluginManager().registerEvents(cmdUpdateNotifier, this);
			} else {
				cmdUpdateNotifier = null;
			}
		}
	}
	
	private void updateConfig() {
		if (needUpdateTo("1.0.0")) {
			for (String cmd : config.getConfigurationSection("Commands").getKeys(false)) {
				if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category").equalsIgnoreCase("time")) {
					config.getConfigurationSection("Commands").getConfigurationSection(cmd).set("category", "WORLDTIME");
				}
				if (config.getConfigurationSection("Commands").getConfigurationSection(cmd).getString("category").equalsIgnoreCase("weather")) {
					config.getConfigurationSection("Commands").getConfigurationSection(cmd).set("category", "WORLDWEATHER");
				}
			}
			LogUtils.info(this, lang.getString("config_updated", new Object[] {"1.0.0"}));
		}
		if (needUpdateTo("1.4.0")) {
			if (config.getConfigurationSection("General").get("logMsg") != null) {
				config.getConfigurationSection("General").set("logCmd", config.getConfigurationSection("General").getBoolean("logMsg"));
				config.getConfigurationSection("General").set("logMsg", null);
				LogUtils.info(this, lang.getString("config_updated", new Object[] {"1.4.0"}));
			}
		}
		config.getConfigurationSection("General").set("version", this.getDescription().getVersion());
	}
	
	private boolean needUpdateTo(String p_version) {
		String[] temp = config.getConfigurationSection("General").getString("version").split("\\.");
		final int[] versionConfig = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionConfig[i] = Integer.parseInt(temp[i]);
		}
		temp = p_version.split("\\.");
		final int[] versionParameter = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionParameter[i] = Integer.parseInt(temp[i]);
		}
		if (versionConfig[0] < versionParameter[0]) {
			return true;
		} else if (versionConfig[0] == versionParameter[0] && versionConfig[1] < versionParameter[1]) {
			return true;
		} else if (versionConfig[1] == versionParameter[1] && versionConfig[2] < versionParameter[2]) {
			return true;
		}
		return false;
	}
	
	private void disable() {
		// Clear every command and permission
		for (String cmd : cmdExecutor.listCommands()) {
			cmdExecutor.removeCommand(cmd);
		}
		// Set cmdExecutor to null
		cmdExecutor = null;
		cmdUpdateNotifier = null;
	}
	
	@Override
	public boolean onCommand(CommandSender p_sender, Command p_cmd, String p_label, String[] p_args){
		if (p_cmd.getName().equalsIgnoreCase("zeltcmds")) {
			switch (p_args.length) {
				case 0:
					p_sender.sendMessage(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
					p_sender.sendMessage(this.getDescription().getDescription());
					p_sender.sendMessage(lang.getString("usage", new Object[] {(p_sender instanceof Player ? "/" : "") + "zeltcmds [add|addalias|examples|list|listalias|reload|remove|removealias] [param]*"}));
					p_sender.sendMessage("add - " + lang.getString("zeltcmds_add_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_add_description"));
					p_sender.sendMessage("addalias - " + lang.getString("zeltcmds_addalias_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_addalias_description"));
					p_sender.sendMessage("examples - " + lang.getString("zeltcmds_examples_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_examples_description"));
					p_sender.sendMessage("list - " + lang.getString("zeltcmds_list_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_list_description"));
					p_sender.sendMessage("listalias - " + lang.getString("zeltcmds_listalias_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_listalias_description"));
					p_sender.sendMessage("reload - " + lang.getString("zeltcmds_reload_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_reload_description"));
					p_sender.sendMessage("remove - " + lang.getString("zeltcmds_remove_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_remove_description"));
					p_sender.sendMessage("removealias - " + lang.getString("zeltcmds_removealias_usage"));
					p_sender.sendMessage(" > " + lang.getString("zeltcmds_removealias_description"));
					return true;
				case 1:
					// Dummy command
					if (p_args[0].equalsIgnoreCase("dummy")) {
						return true;
					}
					// Add examples of commands
					if (p_args[0].equalsIgnoreCase("examples")) {
						this.addExamples();
						MessageUtils.msg(p_sender, lang.getString("prefix") + " " + lang.getString("command_examples"));
						if (p_sender instanceof Player) {
							LogUtils.info(this, lang.getString("log_examples", new Object[] {((Player) p_sender).getDisplayName()}));
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
						MessageUtils.msg(p_sender, lang.getString("prefix") + " " + lang.getString("commands_list") + " (1/" + pages + ") [" + cmdcount + "]");
						String[] cmdList = cmdExecutor.listCommands();
						for (int i = 0; i < pageentries && i < cmdcount; i++) {
							MessageUtils.msg(p_sender, cmdList[i] + " - " + cmdExecutor.getCommandDescription(cmdList[i]));
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
						MessageUtils.msg(p_sender, lang.getString("prefix") + " " + lang.getString("aliases_list") + " (1/" + pages + ") [" + aliascount + "]");
						String[] aliasList = cmdExecutor.listAliases();
						for (int i = 0; i < pageentries && i < aliascount; i++) {
							MessageUtils.msg(p_sender, aliasList[i] + " > " + cmdExecutor.getAliasDescription(aliasList[i]));
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
							LogUtils.info(this, lang.getString("commands_enabled", new Object[] {cmdCount}));
						} else {
							LogUtils.info(this, lang.getString("commands_no_command"));
							LogUtils.info(this, lang.getString("commands_examples"));
						}
						// Log number of aliases enabled
						final int aliasCount = cmdExecutor.countAliases();
						LogUtils.info(this, lang.getString("aliases_enabled", new Object[] {aliasCount}));
						// Register Listener
						this.getServer().getPluginManager().registerEvents(cmdExecutor, this);
						p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("command_reload"));
						if (p_sender instanceof Player) {
							LogUtils.info(this, lang.getString("log_reload", new Object[] {((Player) p_sender).getDisplayName()}));
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
							MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
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
							MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("max_page", new Object[] {pages}));
							return true;
						}
						MessageUtils.msg(p_sender, lang.getString("prefix") + " " + lang.getString("commands_list") + " (" + page + "/" + pages + ") [" + cmdcount + "]");
						String[] cmdList = cmdExecutor.listCommands();
						int start = (page - 1) * pageentries;
						for (int i = start; i < (start + pageentries) && i < cmdcount; i++) {
							MessageUtils.msg(p_sender, cmdList[i] + " - " + cmdExecutor.getCommandDescription(cmdList[i]));
						}
						return true;
					}
					// Lists given page of aliases registered by the plugin
					if (p_args[0].equalsIgnoreCase("listalias")) {
						final int page;
						try {
							page = Integer.parseInt(p_args[1]);
						} catch (NumberFormatException e) {
							MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
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
							MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("max_page", new Object[] {pages}));
							return true;
						}
						MessageUtils.msg(p_sender, lang.getString("prefix") + " " + lang.getString("aliases_list") + " (" + page + "/" + pages + ") [" + aliascount + "]");
						String[] aliasList = cmdExecutor.listAliases();
						final int start = (page - 1) * pageentries;
						for (int i = start; i < (start + pageentries) && i < aliascount; i++) {
							MessageUtils.msg(p_sender, aliasList[i] + " - " + cmdExecutor.getAliasDescription(aliasList[i]));
						}
						return true;
					}
					// Removes a command including its permissions
					if (p_args[0].equalsIgnoreCase("remove")) {
						if (cmdExecutor.existCommand(p_args[1])) {
							cmdExecutor.removeCommand(p_args[1]);
							this.removeConfig(p_args[1]);
							p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("command_remove", new Object[] {p_args[1]}));
							if (p_sender instanceof Player) {
								LogUtils.info(this, lang.getString("log_command_remove", new Object[] {((Player) p_sender).getDisplayName(), p_args[1]}));
							}
						} else {
							p_sender.sendMessage(lang.getString("command_not_found"));
						}
						return true;
					}
					// Removes a alias
					if (p_args[0].equalsIgnoreCase("removealias")) {
						if (cmdExecutor.existAlias(p_args[1])) {
							cmdExecutor.removeAlias(p_args[1]);
							this.removeAliasConfig(p_args[1]);
							p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("alias_remove", new Object[] {p_args[1]}));
							if (p_sender instanceof Player) {
								LogUtils.info(this, lang.getString("log_alias_remove", new Object[] {((Player) p_sender).getDisplayName(), p_args[1]}));
							}
						} else {
							p_sender.sendMessage(lang.getString("alias_not_found"));
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
							p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("alias_add"));
							if (p_sender instanceof Player) {
								LogUtils.info(this, lang.getString("log_alias_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], aliasparams}));
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
							p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("command_add"));
							if (p_sender instanceof Player) {
								LogUtils.info(this, lang.getString("log_command_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], p_args[2], p_args[3], p_args[4], msg}));
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
							p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("alias_add"));
							if (p_sender instanceof Player) {
								LogUtils.info(this, lang.getString("log_alias_add", new Object[] {((Player) p_sender).getDisplayName(), p_args[1], aliasparams}));
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
			LogUtils.warning(this, lang.getString("entry_exists", new Object[] {p_cmd}));
			return;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			LogUtils.warning(this, lang.getString("commands_no_category", new Object[] {p_category}));
			return;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			LogUtils.warning(this, lang.getString("commands_no_type", new Object[] {p_type}));
			return;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		MessageType message = CmdChooser.getMessageType(category, type);
		if (message == MessageType.NOMESSAGE) {
			p_msg = null;
		}
		CmdParent cmd;
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, p_msg);
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, p_msg);
		}
		if (cmd != null) {
			cmdExecutor.addCommand(p_cmd, cmd);
		} else {
			LogUtils.warning(this, lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
		}
	}
	
	private void addExampleCommand(String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (cmdExecutor.checkEntry(p_cmd)) {
			return;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			LogUtils.warning(this, lang.getString("commands_no_category", new Object[] {p_category}));
			return;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			LogUtils.warning(this, lang.getString("commands_no_type", new Object[] {p_type}));
			return;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		MessageType message = CmdChooser.getMessageType(category, type);
		if (message == MessageType.NOMESSAGE) {
			p_msg = null;
		}
		CmdParent cmd;
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), p_type, permissionDefault.name(), p_msg);
			} else {
				LogUtils.warning(this, lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				return;
			}
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), type.name(), permissionDefault.name(), p_msg);
			} else {
				LogUtils.warning(this, lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				return;
			}
		}
		cmdExecutor.addCommand(p_cmd, cmd);
	}
	
	private boolean addSenderCommand(CommandSender p_sender, String p_cmd, String p_category, String p_type, String p_dperm, String p_msg) {
		if (cmdExecutor.checkEntry(p_cmd)) {
			p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("entry_exists", new Object[] {p_cmd}));
			return false;
		}
		Category category = CmdChooser.getCategory(p_category);
		if (category == Category.NOCMD) {
			p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("commands_no_category", new Object[] {p_category}));
			return false;
		}
		Type type = CmdChooser.getType(category, p_type);
		if (type == Default.NOTYPE) {
			p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("commands_no_type", new Object[] {p_type}));
			return false;
		}
		PermissionDefault permissionDefault = CmdChooser.getDefaultPermission(p_dperm);
		MessageType message = CmdChooser.getMessageType(category, type);
		if (message == MessageType.NOMESSAGE) {
			p_msg = null;
		}
		CmdParent cmd;
		if (type == Default.NOCHANGE) {
			cmd = CmdBuilder.build(p_cmd, category, p_type, permissionDefault, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), p_type, permissionDefault.name(), p_msg);
			} else {
				if (p_sender instanceof Player) {
					p_sender.sendMessage(lang.getString("command_not_build"));
				}
				LogUtils.warning(this, lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
				return false;
			}
		} else {
			cmd = CmdBuilder.build(p_cmd, category, type, permissionDefault, p_msg);
			if (cmd != null) {
				this.addConfig(p_cmd, category.name(), type.name(), permissionDefault.name(), p_msg);
			} else {
				if (p_sender instanceof Player) {
					p_sender.sendMessage(lang.getString("command_not_build"));
				}
				LogUtils.warning(this, lang.getString("builder_null", new Object[] {p_cmd, p_category, p_type, p_dperm, p_msg}));
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
			LogUtils.warning(this, lang.getString("entry_exists", new Object[] {p_alias}));
			return;
		}
		cmdExecutor.addAlias(p_alias, p_aliasParam);
	}
	
	private boolean addSenderAlias(CommandSender p_sender, String p_alias, String p_aliasParams) {
		if (cmdExecutor.checkEntry(p_alias)) {
			p_sender.sendMessage(lang.getString("prefix") + " " + lang.getString("entry_exists", new Object[] {p_alias}));
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
	
	private void removeAliasConfig(String p_cmd) {
		config.getConfigurationSection("Aliases").set(p_cmd , null);
		this.saveConfig();
	}
	
	public static LanguageUtils getLanguage() {
		return lang;
	}
	
}