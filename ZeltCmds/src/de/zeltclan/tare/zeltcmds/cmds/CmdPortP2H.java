package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.utils.LocationUtils;

public class CmdPortP2H extends CmdParent {

	private final String msg;
	
	public CmdPortP2H(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2h"), p_perm, p_permExt, p_listener);
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
				if (!off_player.isOnline()) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				if (player.getBedSpawnLocation() == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("no_bedspawn"));
					break;
				}
				final Location home = LocationUtils.getSafeLocation(player.getBedSpawnLocation());
				if (home == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				if (!home.getChunk().isLoaded()) {
					if (!home.getChunk().load(true)) {
						this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				player.teleport(home, TeleportCause.COMMAND);
				if (msg != null) {
					player.sendMessage(ChatColor.GREEN + msg);
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
					if (!off_player.isOnline()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					if (player.getBedSpawnLocation() == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_bedspawn"));
						break;
					}
					final Location home = LocationUtils.getSafeLocation(player.getBedSpawnLocation());
					if (home == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!home.getChunk().isLoaded()) {
						if (!home.getChunk().load(true)) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					player.teleport(home, TeleportCause.COMMAND);
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2h", new Object[] {p_player.getDisplayName(), player.getDisplayName()});
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
