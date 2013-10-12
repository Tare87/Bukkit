package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.utils.LocationUtils;

public class CmdPortMoD extends CmdParent {

	private final String msg;

	public CmdPortMoD(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_mod"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("command_console_no_use"));
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Distance", new Object[] {"/" + p_cmd}));
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					final int distance;
					try {
						distance = Integer.parseInt(p_args[0]);
					} catch (NumberFormatException e) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[0]}));
						break;
					}
					final Vector direction = p_player.getLocation().getDirection().setY(0).normalize();
					final Location target = LocationUtils.getSafeLocation(p_player.getLocation().add(direction.multiply(distance)));
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
					p_player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_mod", new Object[] {distance, target.getWorld().getName(), p_player.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Distance", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
