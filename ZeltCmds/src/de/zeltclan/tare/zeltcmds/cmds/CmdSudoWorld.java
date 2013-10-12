package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdSudoWorld extends CmdParent {

	private final String msg;
	
	public CmdSudoWorld(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_sudo_world"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
		case 1:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World_Cmd", new Object[] {p_cmd}));
			break;
		default:
			final World world = p_sender.getServer().getWorld(p_args[0]);
			if (world == null) {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
				break;
			}
			StringBuilder cmdBuilder = new StringBuilder("/");
			for (int i = 1; i < p_args.length; i++) {
				cmdBuilder.append(p_args[i] + " ");
			}
			String cmd = cmdBuilder.toString().trim();
			for (Player player : world.getPlayers()) {
				if (player.isOnline()) {
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
					player.chat(cmd);
				}
			}
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_World_Cmd", new Object[] {"/" + p_cmd}));
				break;
			default:
				final World world = p_player.getServer().getWorld(p_args[0]);
				if (world == null) {
					p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
					break;
				}
				StringBuilder cmdBuilder = new StringBuilder("/");
				for (int i = 1; i < p_args.length; i++) {
					cmdBuilder.append(p_args[i] + " ");
				}
				String cmd = cmdBuilder.toString().trim();
				for (Player player : world.getPlayers()) {
					if (player.isOnline()) {
						if (msg != null) {
							player.sendMessage(ChatColor.GREEN + msg);
						}
						player.chat(cmd);
					}
				}
				return ZeltCmds.getLanguage().getString("log_sudo_world", new Object[] {p_player.getDisplayName(), cmd, world.getName()});
		}
		return null;
	}
}
