package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdLocation extends CmdParent {

	public static enum Types implements Type {
		BEDSPAWN, SPAWN;
	}
	
	private final Types type;
	private final String msg;
	
	public CmdLocation(Types p_type, Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_location_" + p_type.name().toLowerCase()), p_perm, p_permExt);
		type = p_type;
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("command_console_no_use"));
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (type) {
		case BEDSPAWN:
			return this.setHome(p_player, p_cmd, p_args);
		case SPAWN:
			return this.setSpawn(p_player, p_cmd, p_args);
		}
		return null;
	}
	
	private String setHome(Player p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_sender, false)) {
					p_sender.setBedSpawnLocation(p_sender.getLocation());
					if (msg != null) {
						MessageUtils.info(p_sender, msg);
					}
					return ZeltCmds.getLanguage().getString("log_bedspawn_self", new Object[] {p_sender.getDisplayName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_sender, true)) {
					final OfflinePlayer off_target = p_sender.getServer().getOfflinePlayer(p_args[0]);
					if (off_target.isOnline()) {
						final Player target = off_target.getPlayer();
						target.setBedSpawnLocation(p_sender.getLocation());
						if (msg != null) {
							MessageUtils.info(target, msg);
						}
						return ZeltCmds.getLanguage().getString("log_bedspawn_player", new Object[] {p_sender.getDisplayName(), target.getDisplayName()});
					} else {
						MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_target.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private String setSpawn(Player p_sender, String p_cmd, String[] p_args) {
		if (this.checkPerm(p_sender, false)) {
			switch (p_args.length) {
				case 0:
					p_sender.getWorld().setSpawnLocation(p_sender.getLocation().getBlockX(), p_sender.getLocation().getBlockY(), p_sender.getLocation().getBlockZ());
					if (msg != null) {
						MessageUtils.info(p_sender, msg);
					}
					return ZeltCmds.getLanguage().getString("log_spawn", new Object[] {p_sender.getDisplayName(), p_sender.getWorld().getName()});
				default:
					MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
					MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
					break;
			}
		}
		return null;
	}

}
