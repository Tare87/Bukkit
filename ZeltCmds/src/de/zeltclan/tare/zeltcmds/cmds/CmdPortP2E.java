package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.Msg;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;

public class CmdPortP2E extends CmdParent {

	private final String msg;
	
	public CmdPortP2E(Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2e"), p_perm, p_permExt);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
			case 3:
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Y_Z", new Object[] {p_cmd}));
				break;
			case 4:
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				final int x;
				try {
					x = Integer.parseInt(p_args[1]);
				} catch (NumberFormatException e) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
					break;
				}
				final int y;
				try {
					y = Integer.parseInt(p_args[2]);
				} catch (NumberFormatException e) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
					break;
				}
				final int z;
				try {
					z = Integer.parseInt(p_args[3]);
				} catch (NumberFormatException e) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[3]}));
					break;
				}
				final World world = player.getWorld();
				if (!world.isChunkLoaded(x, z)) {
					if (!world.loadChunk(x, z, true)) {
						Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				if (y < 2) {
					Msg.warning(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
				if (msg != null) {
					Msg.info(player, msg);
				}
				break;
			default:
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Y_Z", new Object[] {p_cmd}));
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
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
			case 4:
				if (this.checkPerm(p_player, false)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					final int x;
					try {
						x = Integer.parseInt(p_args[1]);
					} catch (NumberFormatException e) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[1]}));
						break;
					}
					final int y;
					try {
						y = Integer.parseInt(p_args[2]);
					} catch (NumberFormatException e) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[2]}));
						break;
					}
					final int z;
					try {
						z = Integer.parseInt(p_args[3]);
					} catch (NumberFormatException e) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("not_number", new Object[] {p_args[3]}));
						break;
					}
					final World world = player.getWorld();
					if (!world.isChunkLoaded(x, z)) {
						if (!world.loadChunk(x, z, true)) {
							Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					if (y < 2) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
					if (msg != null) {
						Msg.info(player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2e", new Object[] {x, y, z, world.getName(), p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Player_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
