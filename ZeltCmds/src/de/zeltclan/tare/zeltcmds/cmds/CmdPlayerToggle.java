package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdPlayerToggle extends CmdParent {

	public static enum Types implements Type {
		ALWAYSFLY, BUILD, FLY, FREEZE, MUTE;
	}

	private static enum Results {
		ERROR, OFF, ON;
	}
	
	private final Types type;
	private final String[] msg;
	
	public CmdPlayerToggle(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_playertoggle_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		if (p_msg.isEmpty()) {
			msg = null;
		} else {
			msg = new String[2];
			String[] temp = p_msg.split("\\\\\\/", 2);
			if (temp.length == 2) {
				msg[0] = temp[0].isEmpty() ? null : temp[0];
				msg[1] = temp[1].isEmpty() ? null : temp[1];
			} else {
				msg[0] = temp[0];
				msg[1] = temp[0];
			}
		}
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				Results result = this.action(player);
				switch (result) {
				case ERROR:
					MessageUtils.warning(player, ZeltCmds.getLanguage().getString("playertoggle_error"));
					break;
				case OFF:
					if (msg != null && msg[1] != null) {
						MessageUtils.info(player, msg[1]);
					}
					break;
				case ON:
					if (msg != null && msg[0] != null) {
						MessageUtils.info(player, msg[0]);
					}
					break;
				}
			} else {
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
			}
			break;
		default:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					Results result = this.action(p_player);
					switch (result) {
					case ERROR:
						MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("playertoggle_error"));
						break;
					case OFF:
						if (msg != null && msg[1] != null) {
							MessageUtils.info(p_player, msg[1]);
						}
						break;
					case ON:
						if (msg != null && msg[0] != null) {
							MessageUtils.info(p_player, msg[0]);
						}
						break;
					}
					return ZeltCmds.getLanguage().getString("log_playertoggle_self", new Object[] {type.name(), result.name(), p_player.getDisplayName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (off_player.isOnline()) {
						final Player player = off_player.getPlayer();
						Results result = this.action(player);
						switch (result) {
						case ERROR:
							MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("playertoggle_error"));
							break;
						case OFF:
							if (msg != null) {
								MessageUtils.info(p_player, msg[1]);
							}
							break;
						case ON:
							if (msg != null) {
								MessageUtils.info(p_player, msg[0]);
							}
							break;
						}
						return "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("log_playertoggle_player", new Object[] {type.name(), result.name(), p_player.getDisplayName(), player.getDisplayName()});
					} else {
						MessageUtils.msg(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private Results action (Player p_player) {
		switch (type) {
		case ALWAYSFLY:
			if (p_player.hasMetadata("ZeltCmds_Player_AlwaysFly")) {
				p_player.removeMetadata("ZeltCmds_Player_AlwaysFly", this.getPlugin());
				p_player.setAllowFlight(p_player.getGameMode().equals(GameMode.CREATIVE));
				return Results.OFF;
			} else {
				p_player.setMetadata("ZeltCmds_Player_AlwaysFly", new FixedMetadataValue(this.getPlugin(), "true"));
				p_player.setAllowFlight(true);
				p_player.setFlying(true);
				return Results.ON;
			}
		case BUILD:
			if (p_player.hasMetadata("ZeltCmds_Player_Build")) {
				p_player.removeMetadata("ZeltCmds_Player_Build", this.getPlugin());
				return Results.OFF;
			} else {
				p_player.setMetadata("ZeltCmds_Player_Build", new FixedMetadataValue(this.getPlugin(), "true"));
				return Results.ON;
			}
		case FLY:
			if (p_player.getAllowFlight()) {
				p_player.setAllowFlight(false);
				return Results.OFF;
			} else {
				p_player.setAllowFlight(true);
				return Results.ON;
			}
		case FREEZE:
			if (p_player.hasMetadata("ZeltCmds_Player_Freeze")) {
				p_player.removeMetadata("ZeltCmds_Player_Freeze", this.getPlugin());
				return Results.OFF;
			} else {
				p_player.setMetadata("ZeltCmds_Player_Freeze", new FixedMetadataValue(this.getPlugin(), "true"));
				return Results.ON;
			}
		case MUTE:
			if (p_player.hasMetadata("ZeltCmds_Player_Mute")) {
				p_player.removeMetadata("ZeltCmds_Player_Mute", this.getPlugin());
				return Results.OFF;
			} else {
				p_player.setMetadata("ZeltCmds_Player_Mute", new FixedMetadataValue(this.getPlugin(), "true"));
				return Results.ON;
			}
		}
		return Results.ERROR;
	}
}
