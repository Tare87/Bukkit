package de.zeltclan.tare.zeltcmds.cmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.LocationUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortP2D extends CmdParent {

	private final String msg;

	public CmdPortP2D(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2d"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (!off_player.isOnline()) {
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				break;
			}
			final Player player = off_player.getPlayer();
			if (!player.hasMetadata("ZeltCmds_Death_Last_x") || !player.hasMetadata("ZeltCmds_Death_Last_y") || !player.hasMetadata("ZeltCmds_Death_Last_z") || !player.hasMetadata("ZeltCmds_Death_Last_World")) {
				MessageUtils.msg(player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_death_last"));
				break;
			}
			int x = 0;
			List<MetadataValue> metadata = player.getMetadata("ZeltCmds_Death_Last_x");
			for (MetadataValue value : metadata) {
				x = value.asInt();
				break;
			}
			int y = 0;
			metadata = player.getMetadata("ZeltCmds_Death_Last_y");
			for (MetadataValue value : metadata) {
				y = value.asInt();
				break;
			}
			int z = 0;
			metadata = player.getMetadata("ZeltCmds_Death_Last_z");
			for (MetadataValue value : metadata) {
				z = value.asInt();
				break;
			}
			World world = player.getWorld();
			metadata = player.getMetadata("ZeltCmds_Death_Last_World");
			for (MetadataValue value : metadata) {
				world = player.getServer().getWorld(value.asString());
				break;
			}
			if (!world.isChunkLoaded(x, z)) {
				if (!world.loadChunk(x, z, true)) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
					break;
				}
			}
			final Location target = LocationUtils.getSafeLocation(world.getBlockAt(x, y, z).getLocation().add(0, 1, 0));
			if (target == null) {
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
				break;
			}
			if (!target.getChunk().isLoaded()) {
				if (!target.getChunk().load(true)) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
					break;
				}
			}
			player.teleport(target, TeleportCause.COMMAND);
			if (msg != null) {
				MessageUtils.info(player, msg);
			}
			break;
		default:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
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
						MessageUtils.msg(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					if (!player.hasMetadata("ZeltCmds_Death_Last_x") || !player.hasMetadata("ZeltCmds_Death_Last_y") || !player.hasMetadata("ZeltCmds_Death_Last_z") || !player.hasMetadata("ZeltCmds_Death_Last_World")) {
						MessageUtils.msg(player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_death_last"));
						break;
					}
					int x = 0;
					List<MetadataValue> metadata = player.getMetadata("ZeltCmds_Death_Last_x");
					for (MetadataValue value : metadata) {
						x = value.asInt();
						break;
					}
					int y = 0;
					metadata = player.getMetadata("ZeltCmds_Death_Last_y");
					for (MetadataValue value : metadata) {
						y = value.asInt();
						break;
					}
					int z = 0;
					metadata = player.getMetadata("ZeltCmds_Death_Last_z");
					for (MetadataValue value : metadata) {
						z = value.asInt();
						break;
					}
					World world = player.getWorld();
					metadata = player.getMetadata("ZeltCmds_Death_Last_World");
					for (MetadataValue value : metadata) {
						world = player.getServer().getWorld(value.asString());
						break;
					}
					if (!world.isChunkLoaded(x, z)) {
						if (!world.loadChunk(x, z, true)) {
							MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					final Location target = LocationUtils.getSafeLocation(world.getBlockAt(x, y, z).getLocation().add(0, 1, 0));
					if (target == null) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!target.getChunk().isLoaded()) {
						if (!target.getChunk().load(true)) {
							MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						MessageUtils.info(player, msg);
					}
					return "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("log_port_p2d", new Object[] {p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
