package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.Msg;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;

public class CmdPortM2S extends CmdParent {

	private final String msg;
	
	public CmdPortM2S(Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_m2s"), p_perm, p_permExt);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("command_console_no_use"));
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					final Location spawn = p_player.getWorld().getSpawnLocation();
					if (!p_player.getWorld().getChunkAt(spawn).isLoaded()) {
						if (!p_player.getWorld().getChunkAt(spawn).load(false)) {
							Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					p_player.teleport(spawn, TeleportCause.COMMAND);
					if (msg != null) {
						Msg.info(p_player, msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_m2s", new Object[] {p_player.getDisplayName()});
				}
				break;
			default:
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				Msg.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}