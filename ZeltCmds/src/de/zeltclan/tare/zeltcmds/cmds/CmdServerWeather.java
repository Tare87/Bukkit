package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdServerWeather extends CmdParent {
	
	public static enum Types implements Type {
		RAIN, STORM, SUN;
	}
	
	private final Types type;
	private final String msg;

	public CmdServerWeather(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_serverweather_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		msg = (p_msg.isEmpty() ? null : p_msg);
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			for (World world : p_sender.getServer().getWorlds()) {
				this.changeWeather(world);
			}
			if (msg != null) {
				p_sender.getServer().broadcastMessage(ChatColor.GREEN + msg);
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					for (World world : p_player.getServer().getWorlds()) {
						this.changeWeather(world);
					}
					if (msg != null) {
						p_player.getServer().broadcastMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_serverweather", new Object[] {type.name(), p_player.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
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
	}

}
