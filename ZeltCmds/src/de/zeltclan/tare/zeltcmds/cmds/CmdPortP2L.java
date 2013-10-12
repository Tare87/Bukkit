package de.zeltclan.tare.zeltcmds.cmds;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.utils.LocationUtils;

public class CmdPortP2L extends CmdParent {

	private final String msg;

	public CmdPortP2L(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2l"), p_perm, p_permExt, p_listener);
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
			if (!player.hasMetadata("ZeltCmds_Port_Last_x") || !player.hasMetadata("ZeltCmds_Port_Last_y") || !player.hasMetadata("ZeltCmds_Port_Last_z") || !player.hasMetadata("ZeltCmds_Port_Last_World")) {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("no_port_last"));
				break;
			}
			int x = 0;
			List<MetadataValue> metadata = player.getMetadata("ZeltCmds_Port_Last_x");
			for (MetadataValue value : metadata) {
				x = value.asInt();
				break;
			}
			int y = 0;
			metadata = player.getMetadata("ZeltCmds_Port_Last_y");
			for (MetadataValue value : metadata) {
				y = value.asInt();
				break;
			}
			int z = 0;
			metadata = player.getMetadata("ZeltCmds_Port_Last_z");
			for (MetadataValue value : metadata) {
				z = value.asInt();
				break;
			}
			World world = player.getWorld();
			metadata = player.getMetadata("ZeltCmds_Port_Last_World");
			for (MetadataValue value : metadata) {
				world = player.getServer().getWorld(value.asString());
				break;
			}
			if (world == null) {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
				break;
			}
			final Location target = LocationUtils.getSafeLocation(world.getBlockAt(x, y, z).getLocation().add(0, 1, 0));
			if (target == null) {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("teleport_bad_location"));
				break;
			}
			if (!target.getChunk().isLoaded()) {
				if (!target.getChunk().load(true)) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("chunk_not_load"));
					break;
				}
			}
			player.teleport(target, TeleportCause.COMMAND);
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
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					if (!player.hasMetadata("ZeltCmds_Port_Last_x") || !player.hasMetadata("ZeltCmds_Port_Last_y") || !player.hasMetadata("ZeltCmds_Port_Last_z") || !player.hasMetadata("ZeltCmds_Port_Last_World")) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_port_last"));
						break;
					}
					int x = 0;
					List<MetadataValue> metadata = player.getMetadata("ZeltCmds_Port_Last_x");
					for (MetadataValue value : metadata) {
						x = value.asInt();
						break;
					}
					int y = 0;
					metadata = player.getMetadata("ZeltCmds_Port_Last_y");
					for (MetadataValue value : metadata) {
						y = value.asInt();
						break;
					}
					int z = 0;
					metadata = player.getMetadata("ZeltCmds_Port_Last_z");
					for (MetadataValue value : metadata) {
						z = value.asInt();
						break;
					}
					World world = player.getWorld();
					metadata = player.getMetadata("ZeltCmds_Port_Last_World");
					for (MetadataValue value : metadata) {
						world = player.getServer().getWorld(value.asString());
						break;
					}
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					final Location target = LocationUtils.getSafeLocation(world.getBlockAt(x, y, z).getLocation().add(0, 1, 0));
					if (target == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!target.getChunk().isLoaded()) {
						if (!target.getChunk().load(true)) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2l", new Object[] {target.getBlockX(), target.getBlockY(), target.getBlockZ(), target.getWorld().getName(), p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
