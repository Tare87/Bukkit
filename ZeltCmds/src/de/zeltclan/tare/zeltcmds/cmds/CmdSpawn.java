package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdSpawn extends CmdParent {
	
	public static enum Types implements Type {
		CURSOR, PLAYER;
	}
	
	private final Types type;
	private final String msg;
	
	public CmdSpawn(Types p_type, Permission p_perm, Permission p_permExt, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_spawn_" + p_type.name().toLowerCase()), p_perm, p_permExt);
		type = p_type;
		msg = (!p_msg.isEmpty() ? p_msg : null);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (type) {
		case CURSOR:
			MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("command_console_no_use"));
			break;
		default:
			switch (p_args.length) {
			case 0:
			case 1:
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob_Player", new Object[] {p_cmd}));
				break;
			case 2:
				final EntityType entityType = EntityType.fromName(p_args[0]);
				if (entityType == null) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
					break;
				}
				if (!entityType.isAlive()) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
					break;
				}
				if (!entityType.isSpawnable()) {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
					break;
				}
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[1]);
				if (off_player.isOnline()) {
					final Location location = off_player.getPlayer().getLocation();
					location.getWorld().spawn(location, entityType.getEntityClass());
				} else {
					MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				}
				break;
			default:
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.msg(p_sender, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob_Player", new Object[] {p_cmd}));
				break;
			}
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (type) {
		case CURSOR:
			switch (p_args.length) {
			case 0:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob", new Object[] {"/" + p_cmd}));
				break;
			case 1:
				final EntityType entityType = EntityType.fromName(p_args[0]);
				if (entityType == null) {
					MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
					break;
				}
				if (!entityType.isAlive()) {
					MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
					break;
				}
				if (!entityType.isSpawnable()) {
					MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
					break;
				}
				final Location location = p_player.getTargetBlock(null, 100).getLocation();
				if (location != null) {
					location.setY(location.getY()+1);
					location.getWorld().spawn(location, entityType.getEntityClass());
					if (msg != null) {
						MessageUtils.info(p_player, msg);
					}
					return ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("log_spawn_cursor", new Object[] {p_player.getDisplayName(), entityType.name()});
				} else {
					MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("spawn_bad_location"));
				}
				break;
			default:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob", new Object[] {"/" + p_cmd}));
				break;
			}
			break;
		default:
			switch (p_args.length) {
			case 0:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob_player", new Object[] {p_cmd}));
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					final EntityType entityType = EntityType.fromName(p_args[0]);
					if (entityType == null) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
						break;
					}
					if (!entityType.isAlive()) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
						break;
					}
					if (!entityType.isSpawnable()) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
						break;
					}
					final Location location = p_player.getLocation();
					location.getWorld().spawn(location, entityType.getEntityClass());
					if (msg != null) {
						MessageUtils.info(p_player, msg);
					}
					return ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("log_spawn_self", new Object[] {p_player.getDisplayName(), entityType.name()});
				}
				break;
			case 2:
				if (this.checkPerm(p_player, true)) {
					final EntityType entityType = EntityType.fromName(p_args[0]);
					if (entityType == null) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
						break;
					}
					if (!entityType.isAlive()) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
						break;
					}
					if (!entityType.isSpawnable()) {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
						break;
					}
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[1]);
					if (off_player.isOnline()) {
						final Location location = off_player.getPlayer().getLocation();
						location.getWorld().spawn(location, entityType.getEntityClass());
						if (msg != null) {
							MessageUtils.info(off_player.getPlayer(), msg);
						}
						return ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("log_spawn_player", new Object[] {p_player.getDisplayName(), off_player.getPlayer().getDisplayName(), entityType.name()});
					} else {
						MessageUtils.msg(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("usage_Mob_player", new Object[] {p_cmd}));
				break;
			}
		}
		return null;
	}
}
