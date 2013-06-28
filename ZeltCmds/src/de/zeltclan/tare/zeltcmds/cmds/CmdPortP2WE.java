package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortP2WE extends CmdParent {

	private final String msg;
	
	public CmdPortP2WE(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2we"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {p_cmd}));
				break;
			case 5:
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				final World world = player.getServer().getWorld(p_args[1]);
				if (world == null) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
					break;
				}
				final int x;
				try {
					x = Integer.parseInt(p_args[2]);
				} catch (NumberFormatException e) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
					break;
				}
				final int y;
				try {
					y = Integer.parseInt(p_args[3]);
				} catch (NumberFormatException e) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[3]}));
					break;
				}
				final int z;
				try {
					z = Integer.parseInt(p_args[4]);
				} catch (NumberFormatException e) {
					MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[4]}));
					break;
				}
				if (!world.isChunkLoaded(x, z)) {
					if (!world.loadChunk(x, z, true)) {
						MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				if (y < 0) {
					MessageUtils.warning(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
				if (msg != null) {
					MessageUtils.info(player, msg);
				}
				break;
			default:
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
			case 3:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
			case 4:
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					final World world = player.getServer().getWorld(p_args[1]);
					if (world == null) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
						break;
					}
					final int x;
					try {
						x = Integer.parseInt(p_args[2]);
					} catch (NumberFormatException e) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
						break;
					}
					final int y;
					try {
						y = Integer.parseInt(p_args[3]);
					} catch (NumberFormatException e) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[3]}));
						break;
					}
					final int z;
					try {
						z = Integer.parseInt(p_args[4]);
					} catch (NumberFormatException e) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[4]}));
						break;
					}
					if (!world.isChunkLoaded(x, z)) {
						if (!world.loadChunk(x, z, true)) {
							MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					if (y < 0) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
					if (msg != null) {
						MessageUtils.info(player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2we", new Object[] {x, y, z, world.getName(), p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
