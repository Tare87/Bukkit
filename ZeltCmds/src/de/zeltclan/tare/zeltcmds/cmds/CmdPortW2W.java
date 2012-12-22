package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.Msg;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;

public class CmdPortW2W extends CmdParent {

	private final String msg;
	
	public CmdPortW2W(Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_w2w"), p_perm, p_permExt);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World_World", new Object[] {p_cmd}));
				break;
			case 2:
				final World world = p_sender.getServer().getWorld(p_args[0]);
				if (world == null) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
					break;
				}
				final World target = p_sender.getServer().getWorld(p_args[1]);
				if (target == null) {
					Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
					break;
				}
				final Location spawn = target.getSpawnLocation();
				if (!target.getChunkAt(spawn).isLoaded()) {
					if (!target.getChunkAt(spawn).load(false)) {
						Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				for (Player player : world.getPlayers()) {
					if (player.isOnline()) {
						player.teleport(spawn, TeleportCause.COMMAND);
						if (msg != null) {
							Msg.info(player, msg);
						}
					}
				}
				break;
			default:
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World_World", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World_World", new Object[] {"/" + p_cmd}));
				break;
			case 2:
				if (this.checkPerm(p_player, false)) {
					final World world = p_player.getServer().getWorld(p_args[0]);
					if (world == null) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					final World target = p_player.getServer().getWorld(p_args[1]);
					if (target == null) {
						Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
						break;
					}
					final Location spawn = target.getSpawnLocation();
					if (!target.getChunkAt(spawn).isLoaded()) {
						if (!target.getChunkAt(spawn).load(false)) {
							Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					for (Player player : world.getPlayers()) {
						if (player.isOnline()) {
							player.teleport(spawn, TeleportCause.COMMAND);
							if (msg != null) {
								Msg.info(player, msg);
							}
						}
					}
					return ZeltCmds.getLanguage().getString("log_port_w2w", new Object[] {p_player.getDisplayName(), world.getName(), target.getName()});
				}
				break;
			default:
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World_World", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}