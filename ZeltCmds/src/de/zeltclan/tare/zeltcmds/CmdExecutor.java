package de.zeltclan.tare.zeltcmds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.listener.*;
import de.zeltclan.tare.zeltcmds.runnable.DelayCommandRunnable;

class CmdExecutor implements Listener {
	
	final private Plugin plugin;
	
	final private HashMap<RequireListener, Listener> listenerMap;
	
	final private HashSet<String> cmdSet;
	final private TreeMap<String, CmdParent> cmdMap;
	final private TreeMap<String, String> aliasMap;

	final private Boolean logCmd;
	final private Boolean logAlias;
	final private Boolean casesensitive;

	CmdExecutor(final Plugin p_plugin, final Boolean p_logCmd, final Boolean p_logAlias, final Boolean p_casesensitive) {
		cmdSet = new HashSet<String>();
		cmdSet.add("ban");
		cmdSet.add("ban-ip");
		cmdSet.add("banlist");
		cmdSet.add("clear");
		cmdSet.add("defaultgamemode");
		cmdSet.add("deop");
		cmdSet.add("difficulty");
		cmdSet.add("effect");
		cmdSet.add("enchant");
		cmdSet.add("gamemode");
		cmdSet.add("gameRule");
		cmdSet.add("give");
		cmdSet.add("help");
		cmdSet.add("kick");
		cmdSet.add("kill");
		cmdSet.add("list");
		cmdSet.add("me");
		cmdSet.add("op");
		cmdSet.add("pardon");
		cmdSet.add("pardon-ip");
		cmdSet.add("plugins");
		cmdSet.add("reload");
		cmdSet.add("save-all");
		cmdSet.add("save-off");
		cmdSet.add("save-on");
		cmdSet.add("say");
		cmdSet.add("seed");
		cmdSet.add("spawnpoint");
		cmdSet.add("stop");
		cmdSet.add("tell");
		cmdSet.add("time");
		cmdSet.add("timings");
		cmdSet.add("toggledownfall");
		cmdSet.add("tp");
		cmdSet.add("version");
		cmdSet.add("weather");
		cmdSet.add("whitelist");
		cmdSet.add("xp");
		cmdSet.add("?");
		for (Plugin plugin : p_plugin.getServer().getPluginManager().getPlugins()) {
			if (plugin.getDescription().getCommands() != null) {
				cmdSet.addAll(plugin.getDescription().getCommands().keySet());
			}
		}
		for (String command : p_plugin.getServer().getCommandAliases().keySet()) {
			cmdSet.add(command);
			for (String alias : p_plugin.getServer().getCommandAliases().get(command)) {
				cmdSet.add(alias);
			}
		}
		cmdMap = new TreeMap<String, CmdParent>();
		aliasMap = new TreeMap<String, String>();
		plugin = p_plugin;
		listenerMap = new HashMap<RequireListener, Listener>();
		logCmd = p_logCmd;
		logAlias = p_logAlias;
		casesensitive = p_casesensitive;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent p_event) {
		if (p_event.isCancelled()) {
			return;
		}
		String event_message = p_event.getMessage();
		String cmd = null;
		String [] args = null;
		// Extract command from message for aliascheck
		if (event_message.contains(" ")) {
			cmd = event_message.substring(1, event_message.indexOf(" "));
			args = event_message.substring(event_message.indexOf(" ")+1).split(" ");
		} else {
			cmd = event_message.substring(1);
			args = new String[0];
		}
		// Check for alias
		if ((casesensitive && aliasMap.containsKey(cmd)) || (!casesensitive && aliasMap.containsKey(cmd.toLowerCase()))) {
			event_message = aliasMap.get(cmd);
			// Replace parameters
			event_message = event_message.replace("<player_name>", p_event.getPlayer().getName());
			event_message = event_message.replace("<player_nick>", p_event.getPlayer().getDisplayName());
			event_message = event_message.replace("<player_x>", String.valueOf(p_event.getPlayer().getLocation().getBlockX()));
			event_message = event_message.replace("<player_y>", String.valueOf(p_event.getPlayer().getLocation().getBlockY()));
			event_message = event_message.replace("<player_z>", String.valueOf(p_event.getPlayer().getLocation().getBlockZ()));
			event_message = event_message.replace("<player_world>", p_event.getPlayer().getWorld().getName());
			event_message = event_message.replace("<item_id>", String.valueOf(p_event.getPlayer().getItemInHand().getTypeId()));
			event_message = event_message.replace("<item_data>", String.valueOf(((int) p_event.getPlayer().getItemInHand().getData().getData() & 0xFF)));
			event_message = event_message.replace("<item_name>", p_event.getPlayer().getItemInHand().getType().name());
			for (int i = 0; i < args.length; i++) {
				if (event_message.contains("<param" + (i+1) + ">")) {
					event_message = event_message.replace("<param" + (i+1) + ">", args[i]);
					args[i] = null;
				}
			}
			if (event_message.matches(".*<param[0-9]+>.*")) {
				p_event.getPlayer().sendMessage(ChatColor.GREEN + "[" + plugin.getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_event.setCancelled(true);
				return;
			}
			int replaceindex = 0;
			while (event_message.contains("<param>")) {
				if (replaceindex < args.length) {
					String arg = args[replaceindex++];
					if (arg != null) {
						event_message = event_message.replaceFirst("<param>", arg);
					}
				} else {
					p_event.getPlayer().sendMessage(ChatColor.GREEN + "[" + plugin.getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
					p_event.setCancelled(true);
					return;
				}
			}
			while (event_message.contains("<param?>")) {
				if (replaceindex < args.length) {
					String arg = args[replaceindex++];
					if (arg != null) {
						event_message = event_message.replaceFirst("<param\\?>", arg);
					}
				} else {
					event_message = event_message.replace(" <param?> ", " ");
					event_message = event_message.replaceAll("\\s?<param\\?>\\s?", "");
				}
			}
			String temp = "";
			for (int i = replaceindex; i < args.length; i++) {
				final String arg = args[i];
				if (arg != null) {
					temp += arg + " ";
				}
			}
			temp = temp.trim();
			if (event_message.contains("<param*>")) {
				event_message = event_message.replace("<param*>", temp);
			} else {
				event_message += " " + temp;
			}
			if (logAlias) {
				plugin.getLogger().info(ZeltCmds.getLanguage().getString("log_alias", new Object[] {p_event.getPlayer().getDisplayName(), p_event.getMessage(), event_message}));
			}
			// Handle alias
			String[] cmds = event_message.split("<cmd>");
			for (int i = 0; i < cmds.length; i++) {
				String cmdString = cmds[i].trim();
				Pattern pattern = Pattern.compile("^<wait([0-9]+)>.*");
				Matcher matcher = pattern.matcher(cmdString);
				if (matcher.matches()) {
					Long ticks = Long.parseLong(matcher.group(1));
					final String waitCmdString = cmdString.replaceAll("^<wait[0-9]+>\\s?", "");
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayCommandRunnable(p_event, waitCmdString), ticks);
				} else {
					p_event.getPlayer().chat("/" + cmdString);
				}
			}
			p_event.setCancelled(true);
			return;
		}
		// Extract command and arguments from message
		if (event_message.contains(" ")) {
			cmd = event_message.substring(1, event_message.indexOf(" "));
			args = event_message.substring(event_message.indexOf(" ")+1).split(" ");
		} else {
			cmd = event_message.substring(1);
			args = new String[0];
		}
		// Execute command if exists
		if ((casesensitive && cmdMap.containsKey(cmd)) || (!casesensitive && cmdMap.containsKey(cmd.toLowerCase()))) {
			String logEntry = cmdMap.get(cmd).executePlayer(p_event.getPlayer(), cmd, args);
			if (logCmd && logEntry != null) {
				plugin.getLogger().info(logEntry);
			}
			p_event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerCommand(ServerCommandEvent p_event) {
		String event_message = p_event.getCommand();
		String cmd = null;
		String [] args = null;
		// Extract command from message for aliascheck
		if (event_message.contains(" ")) {
			cmd = event_message.substring(0, event_message.indexOf(" "));
			args = event_message.substring(event_message.indexOf(" ")+1).split(" ");
		} else {
			cmd = event_message;
			args = new String[0];
		}
		// Check for alias
		if ((casesensitive && aliasMap.containsKey(cmd)) || (!casesensitive && aliasMap.containsKey(cmd.toLowerCase()))) {
			event_message = aliasMap.get(cmd);
			//Replace parameters
			for (int i = 0; i < args.length; i++) {
				if (event_message.contains("<param" + (i+1) + ">")) {
					event_message = event_message.replace("<param" + (i+1) + ">", args[i]);
					args[i] = null;
				}
			}
			if (event_message.matches(".*<param[0-9]+>.*")) {
				plugin.getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_event.setCommand("zeltcmds dummy");
				return;
			}
			int replaceindex = 0;
			while (event_message.contains("<param>")) {
				if (replaceindex < args.length) {
					String arg = args[replaceindex++];
					if (arg != null) {
						event_message = event_message.replaceFirst("<param>", arg);
					}
				} else {
					plugin.getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
					p_event.setCommand("zeltcmds dummy");
					return;
				}
			}
			while (event_message.contains("<param?>")) {
				if (replaceindex < args.length) {
					String arg = args[replaceindex++];
					if (arg != null) {
						event_message = event_message.replaceFirst("<param?>", arg);
					}
				} else {
					event_message = event_message.replace(" <param\\?> ", " ");
					event_message = event_message.replaceAll("\\s?<param\\?>\\s?", "");
				}
			}
			String temp = "";
			for (int i = replaceindex; i < args.length; i++) {
				final String arg = args[i];
				if (arg != null) {
					temp += arg + " ";
				}
			}
			temp = temp.trim();
			if (event_message.contains("<param*>")) {
				event_message = event_message.replace("<param*>", temp);
			} else {
				event_message += " " + temp;
			}
			// Handle alias
			String[] cmds = event_message.split("<cmd>");
			for (int i = 0; i < cmds.length; i++) {
				String cmdString = cmds[i].trim();
				Pattern pattern = Pattern.compile("^<wait([0-9]+)>.*");
				Matcher matcher = pattern.matcher(cmdString);
				if (matcher.matches()) {
					Long ticks = Long.parseLong(matcher.group(1));
					final String waitCmdString = cmdString.replaceAll("^<wait[0-9]+>\\s?", "");
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayCommandRunnable(p_event, waitCmdString), ticks);
				} else {
					ServerCommandEvent sCommand = new ServerCommandEvent(p_event.getSender(), cmdString);
					p_event.getSender().getServer().getPluginManager().callEvent(sCommand);
					p_event.getSender().getServer().dispatchCommand(sCommand.getSender(), sCommand.getCommand());
				}
			}
			p_event.setCommand("zeltcmds dummy");
			return;
		}
		// Extract command and arguments from message
		if (event_message.contains(" ")) {
			cmd = event_message.substring(0, event_message.indexOf(" "));
			args = event_message.substring(event_message.indexOf(" ")+1).split(" ");
		} else {
			cmd = event_message;
			args = new String[0];
		}
		// Execute command if exists
		if ((casesensitive && cmdMap.containsKey(cmd)) || (!casesensitive && cmdMap.containsKey(cmd.toLowerCase()))) {
			cmdMap.get(cmd).executeConsole(p_event.getSender(), cmd, args);
			p_event.setCommand("zeltcmds dummy");
		}
	}
	
	boolean checkEntry(String p_cmd) {
		if ((casesensitive && cmdSet.contains(p_cmd)) || (!casesensitive && cmdSet.contains(p_cmd.toLowerCase()))) {
			return true;
		} else if ((casesensitive && cmdMap.containsKey(p_cmd)) || (!casesensitive && cmdMap.containsKey(p_cmd.toLowerCase()))) {
			return true;
		} else {
			return (casesensitive && aliasMap.containsKey(p_cmd)) || (!casesensitive && aliasMap.containsKey(p_cmd.toLowerCase()));
		}
	}
	
	boolean existCommand(String p_cmd) {
		return (casesensitive && cmdMap.containsKey(p_cmd)) || (!casesensitive && cmdMap.containsKey(p_cmd.toLowerCase()));
	}
	
	void addCommand(String p_cmdString, CmdParent p_cmd) {
		cmdMap.put(casesensitive ? p_cmdString : p_cmdString.toLowerCase(), p_cmd);
		RequireListener reqListener = p_cmd.getListener();
		if (!listenerMap.containsKey(reqListener)) {
			final Listener listener;
			switch (reqListener) {
			case ALWAYSFLY:
				listener = new AlwaysFlyListener();
				break;
			case BUILD:
				listener = new BuildListener();
				break;
			case DEATH:
				listener = new DeathListener(plugin);
				break;
			case FREEZE:
				listener = new FreezeListener();
				break;
			case HIDE:
				listener = new HideListener();
				break;
			case MUTE:
				listener = new MuteListener();
				break;
			case PORT:
				listener =  new PortListener(plugin);
				break;
			default:
				listener = null;
				break;
			}
			if (listener != null) {
				listenerMap.put(reqListener, listener);
				plugin.getServer().getPluginManager().registerEvents(listener, plugin);
				
			}
		}
	}

	void removeCommand(String p_cmd) {
		CmdParent cmd = cmdMap.remove(casesensitive ? p_cmd : p_cmd.toLowerCase());
		RequireListener reqListener = cmd.getListener();
		if (reqListener != RequireListener.NONE) {
			boolean delete = true;
			for (CmdParent otherCmd : cmdMap.values()) {
				if (otherCmd.getListener().equals(reqListener)) {
					delete = false;
					break;
				}
			}
			if (delete) {
				Listener listener = listenerMap.remove(reqListener);
				HandlerList.unregisterAll(listener);
			}
		}
		cmd.removePermissions();
	}
	
	String[] listCommands() {
		String[] cmdList = new String[cmdMap.size()];
		cmdList = cmdMap.keySet().toArray(cmdList);
		return cmdList;
	}
	
	int countCommands() {
		return cmdMap.size();
	}
	
	String getCommandDescription(String p_cmd) {
		return cmdMap.get(casesensitive ? p_cmd : p_cmd.toLowerCase()).getDescription();
	}
	
	boolean existAlias(String p_alias) {
		return aliasMap.containsKey(casesensitive ? p_alias : p_alias.toLowerCase());
	}
	
	void addAlias(String p_alias, String p_aliasParams) {
		aliasMap.put(casesensitive ? p_alias : p_alias.toLowerCase(), p_aliasParams);
	}

	void removeAlias(String p_alias) {
		aliasMap.remove(casesensitive ? p_alias : p_alias.toLowerCase());
	}
	
	String[] listAliases() {
		String[] aliasList = new String[aliasMap.size()];
		aliasList = aliasMap.keySet().toArray(aliasList);
		return aliasList;
	}
	
	int countAliases() {
		return aliasMap.size();
	}
	
	String getAliasDescription(String p_alias) {
		return aliasMap.get(casesensitive ? p_alias : p_alias.toLowerCase());
	}
}