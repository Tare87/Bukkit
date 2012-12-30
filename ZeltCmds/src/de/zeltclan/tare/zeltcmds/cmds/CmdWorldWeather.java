package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdWorldWeather extends CmdParent {
	
	public static enum Types implements Type {
		RAIN, STORM, SUN;
	}
	
	private final Types type;
	private final String msg;

	public CmdWorldWeather(Types p_type, Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_worldweather_" + p_type.name().toLowerCase()), p_perm, p_permExt);
		type = p_type;
		msg = (p_msg.isEmpty() ? null : p_msg);
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		case 1:
			final World world = p_sender.getServer().getWorld(p_args[0]);
			if (world == null) {
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
				break;
			}
			this.changeWeather(world);
			break;
		default:
			MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		final World world;
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					world = p_player.getWorld();
					this.changeWeather(world);
					return ZeltCmds.getLanguage().getString("log_worldweather", new Object[] {type.name(), p_player.getDisplayName(), world.getName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					world = p_player.getServer().getWorld(p_args[0]);
					if (world == null) {
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					this.changeWeather(world);
					return ZeltCmds.getLanguage().getString("log_worldweather", new Object[] {type.name(), p_player.getDisplayName(), world.getName()});
				}
				break;
			default:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_world", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private void changeWeather(World p_world) {
		switch (type) {
			case RAIN:
				p_world.setStorm(true);
				p_world.setThundering(false);
				break;
			case STORM:
				p_world.setStorm(true);
				p_world.setThundering(true);
				break;
			case SUN:
				p_world.setStorm(false);
				p_world.setThundering(false);
				break;
			default:
				break;
		}
		p_world.setWeatherDuration(24000);
		if (msg != null) {
			MessageUtils.broadcast(p_world, msg);
		}
	}

}
