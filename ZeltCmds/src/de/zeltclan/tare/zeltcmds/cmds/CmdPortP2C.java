package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.LocationUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;

public class CmdPortP2C extends CmdParent {

	private final String msg;
	
	public CmdPortP2C(Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2c"), p_perm, p_permExt);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Z", new Object[] {p_cmd}));
				break;
			case 3:
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				final int x;
				try {
					x = Integer.parseInt(p_args[1]);
				} catch (NumberFormatException e) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
					break;
				}
				final int z;
				try {
					z = Integer.parseInt(p_args[2]);
				} catch (NumberFormatException e) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
					break;
				}
				final World world = player.getWorld();
				if (!world.isChunkLoaded(x, z)) {
					if (!world.loadChunk(x, z, true)) {
						MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				final Location target = LocationUtils.getSafeLocation(world.getHighestBlockAt(x, z).getLocation().add(0, 1, 0));
				if (target == null) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				if (!target.getChunk().isLoaded()) {
					if (!target.getChunk().load(true)) {
						MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				player.teleport(target, TeleportCause.COMMAND);
				if (msg != null) {
					MessageUtils.info(player, msg);
				}
				break;
			default:
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Z", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Z", new Object[] {"/" + p_cmd}));
				break;
			case 3:
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					final int x;
					try {
						x = Integer.parseInt(p_args[1]);
					} catch (NumberFormatException e) {
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
						break;
					}
					final int z;
					try {
						z = Integer.parseInt(p_args[2]);
					} catch (NumberFormatException e) {
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
						break;
					}
					final World world = player.getWorld();
					if (!world.isChunkLoaded(x, z)) {
						if (!world.loadChunk(x, z, true)) {
							MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					final Location target = LocationUtils.getSafeLocation(world.getHighestBlockAt(x, z).getLocation().add(0, 1, 0));
					if (target == null) {
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!target.getChunk().isLoaded()) {
						if (!target.getChunk().load(true)) {
							MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						MessageUtils.info(player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2c", new Object[] {target.getBlockX(), target.getBlockY(), target.getBlockZ(), target.getWorld().getName(), p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Z", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
