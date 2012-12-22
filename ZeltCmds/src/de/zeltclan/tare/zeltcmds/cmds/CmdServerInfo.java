package de.zeltclan.tare.zeltcmds.cmds;

import java.util.ArrayList;
import java.util.TreeSet;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.Msg;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdServerInfo extends CmdParent {

	public static enum Types implements Type {
		BLACKLIST, INFO, ONLINELIST, OPLIST, WHITELIST, WORLDLIST;
	}
	private final Types type;
	
	public CmdServerInfo(Types p_type, Permission p_perm, Permission p_permExt) {
		super(ZeltCmds.getLanguage().getString("description_serverinfo_" + p_type.name().toLowerCase()), p_perm, p_permExt);
		type = p_type;
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			final String[] info = this.getInformation(p_sender.getServer());
			for (String msg : info) {
				Msg.msg(p_sender, msg);
			}
			break;
		default:
			Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage", new Object[] {p_cmd}));
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		final String[] info;
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					info = this.getInformation(p_player.getServer());
					for (String msg : info) {
						Msg.msg(p_player, msg);
					}
					return (ZeltCmds.getLanguage().getString("log_serverinfo", new Object[] {type.name(), p_player.getDisplayName()}));
				}
				break;
			default:
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private String[] getInformation (Server p_server) {
		ArrayList<String> result = new ArrayList<String>();
		TreeSet<String> temp = new TreeSet<String>();
		String line = new String();
		switch (type) {
		case BLACKLIST:
			temp.clear();
			line = "";
			for (OfflinePlayer player : p_server.getBannedPlayers()) {
				temp.add(player.getName());
			}
			int size_ban_player = temp.size();
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			String line_ban_player = line;
			temp.clear();
			line = "";
			temp.addAll(p_server.getIPBans());
			int size_ban_ip = temp.size();
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			String line_ban_ip = line;
			result.add(ZeltCmds.getLanguage().getString("serverinfo_blacklist", new Object[] {(size_ban_player + size_ban_ip)}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_player", new Object[] {size_ban_player}));
			result.add(line_ban_player);
			result.add(ZeltCmds.getLanguage().getString("serverinfo_black_ip", new Object[] {size_ban_ip}));
			result.add(line_ban_ip);
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
			temp.clear();
			line = "";
			for (Player player : p_server.getOnlinePlayers()) {
				if (player.isOp()) {
					temp.add(player.getName());
				}
			}
			int size_op = temp.size();
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			String line_op = line;
			temp.clear();
			line = "";
			for (Player player : p_server.getOnlinePlayers()) {
				if (!player.isOp()) {
					temp.add(player.getName());
				}
			}
			int size_player = temp.size();
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			String line_player = line;
			result.add(ZeltCmds.getLanguage().getString("serverinfo_onlinelist", new Object[] {size_op + size_player}));
			result.add(ZeltCmds.getLanguage().getString("serverinfo_op", new Object[] {size_op}));
			result.add(line_op);
			result.add(ZeltCmds.getLanguage().getString("serverinfo_player", new Object[] {size_player}));
			result.add(line_player);
			break;
		case OPLIST:
			temp.clear();
			for (OfflinePlayer player : p_server.getOperators()) {
				temp.add(player.getName() + ": " + (player.isOnline() ? "Online" : "Offline"));
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_oplist", new Object[] {temp.size()}));
			result.addAll(temp);
			break;
		case WHITELIST:
			temp.clear();
			line = "";
			for (OfflinePlayer player : p_server.getWhitelistedPlayers()) {
				temp.add(player.getName());
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_whitelist", new Object[] {temp.size()}));
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			result.add(line);
			break;
		case WORLDLIST:
			temp.clear();
			line = "";
			for (World world : p_server.getWorlds()) {
				temp.add(world.getName());
			}
			result.add(ZeltCmds.getLanguage().getString("serverinfo_worldlist", new Object[] {temp.size()}));
			if (temp.size() > 0) {
				line = temp.pollFirst();
				String name = new String();
				while ((name = temp.pollFirst()) != null) {
					line += ", " + name;
				}
			}
			result.add(line);
			break;
		default:
			break;
		}
		return result.toArray(new String[result.size()]);
	}

}
