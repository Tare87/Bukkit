package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortP2P extends CmdParent {

	private final String msg;
	
	public CmdPortP2P(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2p"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Player", new Object[] {p_cmd}));
				break;
			case 2:
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final OfflinePlayer off_target = p_sender.getServer().getOfflinePlayer(p_args[1]);
				if (!off_target.isOnline()) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_target.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[1]}));
					break;
				}
				final Player player = off_player.getPlayer();
				final Player target = off_target.getPlayer();
				player.teleport(target, TeleportCause.COMMAND);
				if (msg != null) {
					MessageUtils.info(player, msg);
				}
				break;
			default:
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Player", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Player", new Object[] {"/" + p_cmd}));
				break;
			case 2:
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final OfflinePlayer off_target = p_player.getServer().getOfflinePlayer(p_args[1]);
					if (!off_target.isOnline()) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_target.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[1]}));
						break;
					}
					final Player player = off_player.getPlayer();
					final Player target = off_target.getPlayer();
					player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						MessageUtils.info(player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2p", new Object[] {p_player.getDisplayName(), player.getDisplayName(), target.getDisplayName()});
				}
				break;
			default:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
