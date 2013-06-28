package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.LocationUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortM2S extends CmdParent {

	private final String msg;
	
	public CmdPortM2S(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_m2s"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("command_console_no_use"));
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					if (p_player.getWorld().getSpawnLocation() == null) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_spawn"));
						break;
					}
					final Location spawn = LocationUtils.getSafeLocation(p_player.getWorld().getSpawnLocation());
					if (spawn == null) {
						MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!spawn.getChunk().isLoaded()) {
						if (!spawn.getChunk().load(true)) {
							MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					p_player.teleport(spawn, TeleportCause.COMMAND);
					if (msg != null) {
						MessageUtils.info(p_player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_m2s", new Object[] {p_player.getDisplayName()});
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
