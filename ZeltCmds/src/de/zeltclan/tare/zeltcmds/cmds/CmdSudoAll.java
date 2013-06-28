package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdSudoAll extends CmdParent {

	private final String msg;
	
	public CmdSudoAll(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_sudo_all"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Cmd", new Object[] {p_cmd}));
			break;
		default:
			String cmd = "/";
			for (String arg : p_args) {
				cmd += arg + " ";
			}
			cmd = cmd.trim();
			for (Player player : p_sender.getServer().getOnlinePlayers()) {
				if (msg != null) {
					MessageUtils.info(player, msg);
				}
				player.chat(cmd);
			}
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Cmd", new Object[] {"/" + p_cmd}));
				break;
			default:
				String cmd = "/";
				for (String arg : p_args) {
					cmd += arg + " ";
				}
				cmd = cmd.trim();
				for (Player player : p_player.getServer().getOnlinePlayers()) {
					if (msg != null) {
						MessageUtils.info(player, msg);
					}
					player.chat(cmd);
				}
				return ZeltCmds.getLanguage().getString("log_sudo_all", new Object[] {p_player.getDisplayName(), cmd});
		}
		return null;
	}
}
