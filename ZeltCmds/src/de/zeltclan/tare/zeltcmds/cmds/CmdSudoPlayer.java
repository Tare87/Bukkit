package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdSudoPlayer extends CmdParent {

	private final String msg;
	
	public CmdSudoPlayer(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_sudo_player"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
		case 1:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Cmd", new Object[] {p_cmd}));
			break;
		default:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (!off_player.isOnline()) {
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				break;
			}
			final Player player = off_player.getPlayer();
			String cmd = "/";
			for (int i = 1; i < p_args.length; i++) {
				cmd += p_args[i] + " ";
			}
			cmd = cmd.trim();
			if (msg != null) {
				MessageUtils.info(player, msg);
			}
			player.chat(cmd);
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_Cmd", new Object[] {"/" + p_cmd}));
				break;
			default:
				final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				String cmd = "/";
				for (int i = 1; i < p_args.length; i++) {
					cmd += p_args[i] + " ";
				}
				cmd = cmd.trim();
				if (msg != null) {
					MessageUtils.info(player, msg);
				}
				player.chat(cmd);
				return ZeltCmds.getLanguage().getString("log_sudo_player", new Object[] {p_player.getDisplayName(), cmd, player.getDisplayName()});
		}
		return null;
	}
}
