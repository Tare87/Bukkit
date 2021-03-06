package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortW2P extends CmdParent {

	private final String msg;
	
	public CmdPortW2P(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_w2p"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World_Player", new Object[] {p_cmd}));
				break;
			case 2:
				final World world = p_sender.getServer().getWorld(p_args[0]);
				if (world == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
					break;
				}
				final OfflinePlayer off_target = p_sender.getServer().getOfflinePlayer(p_args[1]);
				if (!off_target.isOnline()) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_target.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[1]}));
					break;
				}
				final Player target = off_target.getPlayer();
				for (Player player : world.getPlayers()) {
					if (player.isOnline() && player != target) {
						player.teleport(target, TeleportCause.COMMAND);
						if (msg != null) {
							player.sendMessage(ChatColor.GREEN + msg);
						}
					}
				}
				break;
			default:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World_Player", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_World_Player", new Object[] {"/" + p_cmd}));
				break;
			case 2:
				if (this.checkPerm(p_player, false)) {
					final World world = p_player.getServer().getWorld(p_args[0]);
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					final OfflinePlayer off_target = p_player.getServer().getOfflinePlayer(p_args[1]);
					if (!off_target.isOnline()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_target.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[1]}));
						break;
					}
					final Player target = off_target.getPlayer();
					for (Player player : world.getPlayers()) {
						if (player.isOnline() && player != target) {
							player.teleport(target, TeleportCause.COMMAND);
							if (msg != null) {
								player.sendMessage(ChatColor.GREEN + msg);
							}
						}
					}
					return ZeltCmds.getLanguage().getString("log_port_w2p", new Object[] {p_player.getDisplayName(), world.getName(), target.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_World_Player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
