package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public final class CmdWorldTime extends CmdParent {
	
	private final long time;
	private final long rel_time;
	private final String msg;
	
	public CmdWorldTime(long p_time, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_worldtime", new Object[] {p_time}), p_perm, p_permExt, p_listener);
		rel_time = p_time;
		time = p_time - (p_time < 6000 ? -18000 : 6000);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		case 1:
			final World world = p_sender.getServer().getWorld(p_args[0]);
			if (world == null) {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
				break;
			}
			world.setTime(time);
			if (msg != null) {
				for (Player player : world.getPlayers()) {
					if (player.isOnline()) {
						player.getPlayer().sendMessage(ChatColor.GREEN + msg);
					}
				}
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		final World world;
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					world = p_player.getWorld();
					world.setTime(time);
					if (msg != null) {
						for (Player player : world.getPlayers()) {
							if (player.isOnline()) {
								player.getPlayer().sendMessage(ChatColor.GREEN + msg);
							}
						}
					}
					return ZeltCmds.getLanguage().getString("log_worldtime", new Object[] {rel_time, p_player.getDisplayName(), world.getName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					world = p_player.getServer().getWorld(p_args[0]);
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					world.setTime(time);
					if (msg != null) {
						for (Player player : world.getPlayers()) {
							if (player.isOnline()) {
								player.getPlayer().sendMessage(ChatColor.GREEN + msg);
							}
						}
					}
					return ZeltCmds.getLanguage().getString("log_worldtime", new Object[] {rel_time, p_player.getDisplayName(), world.getName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_world", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
