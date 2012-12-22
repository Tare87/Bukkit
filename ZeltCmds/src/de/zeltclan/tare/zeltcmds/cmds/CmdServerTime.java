package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.Msg;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;

public final class CmdServerTime extends CmdParent {
	
	private final long time;
	private final long rel_time;
	private final String msg;
	
	public CmdServerTime(long p_time, Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_servertime", new Object[] {p_time}), p_perm, p_permExt);
		rel_time = p_time;
		time = p_time - (p_time < 6000 ? -18000 : 6000);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			for (World world : p_sender.getServer().getWorlds()) {
				world.setTime(time);
				if (msg != null) {
					Msg.broadcast(world, msg);
				}
			}
			break;
		default:
			Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			Msg.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					for (World world : p_player.getServer().getWorlds()) {
						world.setTime(time);
						if (msg != null) {
							Msg.broadcast(world, msg);
						}
					}
					return ZeltCmds.getLanguage().getString("log_servertime", new Object[] {rel_time, p_player.getDisplayName()});
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
