package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortA2P extends CmdParent {
	
	private final String msg;
	
	public CmdPortA2P(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_a2p"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
				break;
			case 1:
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (off_player.isOnline()) {
					final Player target = off_player.getPlayer();
					for (Player player : p_sender.getServer().getOnlinePlayers()) {
						if (player != target) {
							player.teleport(target, TeleportCause.COMMAND);
							if (msg != null) {
								player.sendMessage(ChatColor.GREEN + msg);
							}
						}
					}
				} else {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				}
				break;
			default:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {"/" + p_cmd}));
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (off_player.isOnline()) {
						final Player target = off_player.getPlayer();
						for (Player player : p_player.getServer().getOnlinePlayers()) {
							if (player != target) {
								player.teleport(target, TeleportCause.COMMAND);
								if (msg != null) {
									player.sendMessage(ChatColor.GREEN + msg);
								}
							}
						}
						return ZeltCmds.getLanguage().getString("log_port_a2p", new Object[] {p_player.getName(), target.getName()});
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
