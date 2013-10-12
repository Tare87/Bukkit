package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

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
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Cmd", new Object[] {p_cmd}));
			break;
		default:
			StringBuilder cmdBuilder = new StringBuilder("/");
			for (String arg : p_args) {
				cmdBuilder.append(arg + " ");
			}
			String cmd = cmdBuilder.toString().trim();
			for (Player player : p_sender.getServer().getOnlinePlayers()) {
				if (msg != null) {
					player.sendMessage(ChatColor.GREEN + msg);
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
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Cmd", new Object[] {"/" + p_cmd}));
				break;
			default:
				StringBuilder cmdBuilder = new StringBuilder("/");
				for (String arg : p_args) {
					cmdBuilder.append(arg + " ");
				}
				String cmd = cmdBuilder.toString().trim();
				for (Player player : p_player.getServer().getOnlinePlayers()) {
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
					player.chat(cmd);
				}
				return ZeltCmds.getLanguage().getString("log_sudo_all", new Object[] {p_player.getDisplayName(), cmd});
		}
		return null;
	}
}
