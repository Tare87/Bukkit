package de.zeltclan.tare.zeltcmds;

import java.util.HashSet;
import java.util.TreeMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

import de.zeltclan.tare.bukkitutils.LogUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.cmds.CmdPortM2D;
import de.zeltclan.tare.zeltcmds.cmds.CmdPortM2L;
import de.zeltclan.tare.zeltcmds.cmds.CmdPortP2D;
import de.zeltclan.tare.zeltcmds.cmds.CmdPortP2L;
import de.zeltclan.tare.zeltcmds.listener.*;

class CmdExecutor implements Listener {
	
	final private Plugin plugin;
	
	private Listener listenPort;
	private Listener listenDeath;
	
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
		cmdSet.add("enchant");
		cmdSet.add("gamemode");
		cmdSet.add("GameRule");
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
		cmdMap = new TreeMap<String, CmdParent>();
		aliasMap = new TreeMap<String, String>();
		plugin = p_plugin;
		listenPort = null;
		listenDeath = null;
		logCmd = p_logCmd;
		logAlias = p_logAlias;
		casesensitive = p_casesensitive;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent p_event) {
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
			//Replace parameters
			int replaceindex = 0;
			while (event_message.contains("<param>")) {
				if (replaceindex < args.length) {
					event_message = event_message.replaceFirst("<param>", args[replaceindex++]);
				} else {
					MessageUtils.msg(p_event.getPlayer(), ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
					p_event.setCancelled(true);
					return;
				}
			}
			for (int i = replaceindex; i < args.length; i++) {
				event_message += " " + args[i];
			}
			if (logAlias) {
				LogUtils.info(ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("log_alias", new Object[] {p_event.getPlayer().getDisplayName(), p_event.getMessage(), event_message}));
			}
			// Handle alias
			String[] cmds = event_message.split("<cmd>");
			for (int i = 0; i < cmds.length; i++) {
				p_event.getPlayer().chat("/" + cmds[i].trim());
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
				LogUtils.info(ZeltCmds.getLanguage().getString("prefix") + " " + logEntry);
			}
			p_event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
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
			int replaceindex = 0;
			while (event_message.contains("<param>")) {
				if (replaceindex < args.length) {
					event_message = event_message.replaceFirst("<param>", args[replaceindex++]);
				} else {
					LogUtils.warning(ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
					p_event.setCommand("zeltcmds dummy");
					return;
				}
			}
			for (int i = replaceindex; i < args.length; i++) {
				event_message += " " + args[i];
			}
			// Handle alias
			String[] cmds = event_message.split("<cmd>");
			for (int i = 0; i < cmds.length; i++) {
				p_event.getSender().getServer().getPluginManager().callEvent(new ServerCommandEvent(p_event.getSender(), cmds[i].trim()));
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
		if (p_cmd.equalsIgnoreCase("zeltcmds")) {
			return true;
		} else if ((casesensitive && cmdSet.contains(p_cmd)) || (!casesensitive && cmdSet.contains(p_cmd.toLowerCase()))) {
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
		if ((listenPort == null) && ((p_cmd instanceof CmdPortM2L) || (p_cmd instanceof CmdPortP2L))) {
			listenPort = new PortListener(plugin);
			plugin.getServer().getPluginManager().registerEvents(listenPort, plugin);
		}
		if ((listenDeath == null) && ((p_cmd instanceof CmdPortM2D) || (p_cmd instanceof CmdPortP2D))) {
			listenDeath = new DeathListener(plugin);
			plugin.getServer().getPluginManager().registerEvents(listenDeath, plugin);
		}
	}

	void removeCommand(String p_cmd) {
		CmdParent cmd = cmdMap.remove(casesensitive ? p_cmd : p_cmd.toLowerCase());
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