package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdSudoConsole extends CmdParent {

	public CmdSudoConsole(Permission p_perm, Permission p_permExt, RequireListener p_listener) {
		super(ZeltCmds.getLanguage().getString("description_sudo_console"), p_perm, p_permExt, p_listener);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Cmd", new Object[] {p_cmd}));
			break;
		default:
			String cmd = "";
			for (String arg : p_args) {
				cmd += arg + " ";
			}
			cmd = cmd.trim();
			ServerCommandEvent sCommand = new ServerCommandEvent(p_sender, cmd);
			p_sender.getServer().getPluginManager().callEvent(sCommand);
			p_sender.getServer().dispatchCommand(sCommand.getSender(), sCommand.getCommand());
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
				String cmd = "";
				for (String arg : p_args) {
					cmd += arg + " ";
				}
				cmd = cmd.trim();
				ServerCommandEvent sCommand = new ServerCommandEvent(p_player.getServer().getConsoleSender(), cmd);
				p_player.getServer().getPluginManager().callEvent(sCommand);
				p_player.getServer().dispatchCommand(sCommand.getSender(), sCommand.getCommand());
				return ZeltCmds.getLanguage().getString("log_sudo_console", new Object[] {p_player.getDisplayName(), cmd});
		}
		return null;
	}
}
