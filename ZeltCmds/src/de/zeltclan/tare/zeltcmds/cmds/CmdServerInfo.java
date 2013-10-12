package de.zeltclan.tare.zeltcmds.cmds;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdServerInfo extends CmdParent {

	public static enum Types implements Type {
		BLACKLIST, INFO, ONLINELIST, OPLIST, RAM, WHITELIST, WORLDLIST;
	}
	private final Types type;
	
	public CmdServerInfo(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener) {
		super(ZeltCmds.getLanguage().getString("description_serverinfo_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			for (String msg : this.getInformation(p_sender.getServer())) {
				this.getPlugin().getLogger().info(msg);
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().info(ZeltCmds.getLanguage().getString("usage", new Object[] {p_cmd}));
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					for (String msg : this.getInformation(p_player.getServer())) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_serverinfo", new Object[] {type.name(), p_player.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private List<String> getInformation (Server p_server) {
		List<String> result = new ArrayList<String>();
		switch (type) {
		case BLACKLIST:
			TreeSet<String> set_ban_player = new TreeSet<String>();
			for (OfflinePlayer player : p_server.getBannedPlayers()) {
				set_ban_player.add(player.getName());
			}
			int size_ban_player = set_ban_player.size();
			StringBuilder line_ban_player = new StringBuilder();
			if (set_ban_player.size() > 0) {
				line_ban_player.append(set_ban_player.pollFirst());
				String name = new String();
				while ((name = set_ban_player.pollFirst()) != null) {
					line_ban_player.append(", " + name);
				}
			}
			TreeSet<String> set_ban_ip = new TreeSet<String>(p_server.getIPBans());
			int size_ban_ip = set_ban_ip.size();
			StringBuilder line_ban_ip = new StringBuilder();
			if (set_ban_ip.size() > 0) {
				line_ban_ip.append(set_ban_ip.pollFirst());
				String name = new String();
				while ((name = set_ban_ip.pollFirst()) != null) {
					line_ban_ip.append(", " + name);
				}
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_blacklist", new Object[] {(size_ban_player + size_ban_ip)}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_player", new Object[] {size_ban_player}));
			result.add(line_ban_player.toString());
			result.add(ZeltCmds.getLanguage().getString("serverinfo_black_ip", new Object[] {size_ban_ip}));
			result.add(line_ban_ip.toString());
			break;
		case INFO:
			result.add(ZeltCmds.getLanguage().getString("serverinfo_name", new Object[] {p_server.getServerName()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_version", new Object[] {p_server.getVersion()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_bukkitversion", new Object[] {p_server.getBukkitVersion()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_ip", new Object[] {p_server.getIp(), String.valueOf(p_server.getPort())}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_motd", new Object[] {p_server.getMotd()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_maxplayers", new Object[] {p_server.getMaxPlayers()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_whitelist_" + (p_server.hasWhitelist() ? "on" : "off")));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_flying_" + (p_server.getAllowFlight() ? "on" : "off")));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_viewdistance", new Object[] {p_server.getViewDistance()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_spawnradius", new Object[] {p_server.getSpawnRadius()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_moblimit", new Object[] {p_server.getMonsterSpawnLimit()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_animallimit", new Object[] {p_server.getAnimalSpawnLimit()}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_waterlimit", new Object[] {p_server.getWaterAnimalSpawnLimit()}));
			break;
		case ONLINELIST:
			TreeSet<String> set_online_op = new TreeSet<String>();
			TreeSet<String> set_online_player = new TreeSet<String>();
			for (Player player : p_server.getOnlinePlayers()) {
				if (player.isOp()) {
					set_online_op.add(player.getName());
				} else {
					set_online_player.add(player.getName());
				}
			}
			int size_online_op = set_online_op.size();
			StringBuilder line_online_op = new StringBuilder();
			if (set_online_op.size() > 0) {
				line_online_op.append(set_online_op.pollFirst());
				String name = new String();
				while ((name = set_online_op.pollFirst()) != null) {
					line_online_op.append(", " + name);
				}
			}
			int size_online_player = set_online_player.size();
			StringBuilder line_online_player = new StringBuilder();
			if (set_online_player.size() > 0) {
				line_online_player.append(set_online_player.pollFirst());
				String name = new String();
				while ((name = set_online_player.pollFirst()) != null) {
					line_online_player.append(", " + name);
				}
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_onlinelist", new Object[] {size_online_op + size_online_player}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_op", new Object[] {size_online_op}));
			result.add(line_online_op.toString());
			result.add(ZeltCmds.getLanguage().getString("serverinfo_player", new Object[] {size_online_player}));
			result.add(line_online_player.toString());
			break;
		case OPLIST:
			for (OfflinePlayer player : p_server.getOperators()) {
				result.add(player.getName() + ": " + (player.isOnline() ? "Online" : "Offline"));
			}
			result.add(0, ZeltCmds.getLanguage().getString("serverinfo_oplist", new Object[] {result.size()}));
			break;
		case RAM:
			final Runtime runtime = Runtime.getRuntime();
			final int conversion = 1024 * 1024;
			final int freeMem = ((int) (runtime.freeMemory() / conversion));
			final int totalMem = ((int) (runtime.totalMemory() / conversion));
			final int maxMem = ((int) (runtime.maxMemory() / conversion));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_memory"));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_memory_free", new Object[] {freeMem, ((int) (100 * freeMem / totalMem))}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_memory_used", new Object[] {(totalMem - freeMem), ((int) (100 * (totalMem - freeMem) / totalMem))}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_memory_total", new Object[] {totalMem}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_memory_max", new Object[] {maxMem}));
			break;
		case WHITELIST:
			TreeSet<String> set_white_player = new TreeSet<String>();
			for (OfflinePlayer player : p_server.getWhitelistedPlayers()) {
				set_white_player.add(player.getName());
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_whitelist", new Object[] {set_white_player.size()}));
			StringBuilder line_white_player = new StringBuilder();
			if (set_white_player.size() > 0) {
				line_white_player.append(set_white_player.pollFirst());
				String name = new String();
				while ((name = set_white_player.pollFirst()) != null) {
					line_white_player.append(", " + name);
				}
			}
			result.add(line_white_player.toString());
			break;
		case WORLDLIST:
			TreeSet<String> set_world = new TreeSet<String>();
			for (World world : p_server.getWorlds()) {
				set_world.add(world.getName());
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_worldlist", new Object[] {set_world.size()}));
			StringBuilder line_world = new StringBuilder();
			if (set_world.size() > 0) {
				line_world.append(set_world.pollFirst());
				String name = new String();
				while ((name = set_world.pollFirst()) != null) {
					line_world.append(", " + name);
				}
			}
			result.add(line_world.toString());
			break;
		}
		return result;
	}

}
