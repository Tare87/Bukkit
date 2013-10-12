package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.utils.LocationUtils;

public class CmdPortA2W extends CmdParent {

private final String msg;
	
	public CmdPortA2W(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_a2w"), p_perm, p_permExt, p_listener);
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
				if (world.getSpawnLocation() == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("no_spawn"));
					break;
				}
				final Location spawn = LocationUtils.getSafeLocation(world.getSpawnLocation());
				if (spawn == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				if (!spawn.getChunk().isLoaded()) {
					if (!spawn.getChunk().load(true)) {
						this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				for (Player player : world.getPlayers()) {
					if (player.isOnline()) {
						player.teleport(spawn, TeleportCause.COMMAND);
						if (msg != null) {
							player.sendMessage(ChatColor.GREEN + msg);
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
		switch (p_args.length) {
			case 0:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_World", new Object[] {"/" + p_cmd}));
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					final World world = p_player.getServer().getWorld(p_args[0]);
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					if (world.getSpawnLocation() == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_spawn"));
					}
					final Location spawn = LocationUtils.getSafeLocation(world.getSpawnLocation());
					if (spawn == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!spawn.getChunk().isLoaded()) {
						if (!spawn.getChunk().load(true)) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					for (Player player : world.getPlayers()) {
						if (player.isOnline()) {
							player.teleport(spawn, TeleportCause.COMMAND);
							if (msg != null) {
								player.sendMessage(ChatColor.GREEN + msg);
							}
						}
					}
					return ZeltCmds.getLanguage().getString("log_port_a2w", new Object[] {p_player.getName(), world.getName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_World", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
