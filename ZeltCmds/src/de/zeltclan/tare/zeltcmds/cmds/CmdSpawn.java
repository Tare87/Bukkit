package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdSpawn extends CmdParent {
	
	public static enum Types implements Type {
		CURSOR, PLAYER;
	}
	
	private final Types type;
	private final String msg;
	
	public CmdSpawn(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_spawn_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		msg = (!p_msg.isEmpty() ? p_msg : null);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (type) {
		case CURSOR:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("command_console_no_use"));
			break;
		default:
			switch (p_args.length) {
			case 0:
			case 1:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Mob_Player", new Object[] {p_cmd}));
				break;
			case 2:
				final EntityType entityType = EntityType.fromName(p_args[0]);
				if (entityType == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
					break;
				}
				if (!entityType.isAlive()) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
					break;
				}
				if (!entityType.isSpawnable()) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
					break;
				}
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[1]);
				if (off_player.isOnline()) {
					final Location location = off_player.getPlayer().getLocation();
					location.getWorld().spawn(location, entityType.getEntityClass());
					if (msg != null) {
						off_player.getPlayer().sendMessage(ChatColor.GREEN + msg);
					}
				} else {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				}
				break;
			default:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Mob_Player", new Object[] {p_cmd}));
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
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Mob", new Object[] {"/" + p_cmd}));
				break;
			case 1:
				final EntityType entityType = EntityType.fromName(p_args[0]);
				if (entityType == null) {
					p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
					break;
				}
				if (!entityType.isAlive()) {
					p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
					break;
				}
				if (!entityType.isSpawnable()) {
					p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
					break;
				}
				final Location location = p_player.getTargetBlock(null, 100).getLocation();
				if (location != null) {
					location.setY(location.getY()+1);
					location.getWorld().spawn(location, entityType.getEntityClass());
					if (msg != null) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_spawn_cursor", new Object[] {p_player.getDisplayName(), entityType.name()});
				} else {
					p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("spawn_bad_location"));
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Mob", new Object[] {"/" + p_cmd}));
				break;
			}
			break;
		default:
			switch (p_args.length) {
			case 0:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Mob_player", new Object[] {p_cmd}));
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					final EntityType entityType = EntityType.fromName(p_args[0]);
					if (entityType == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
						break;
					}
					if (!entityType.isAlive()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
						break;
					}
					if (!entityType.isSpawnable()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
						break;
					}
					final Location location = p_player.getLocation();
					location.getWorld().spawn(location, entityType.getEntityClass());
					if (msg != null) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_spawn_self", new Object[] {p_player.getDisplayName(), entityType.name()});
				}
				break;
			case 2:
				if (this.checkPerm(p_player, true)) {
					final EntityType entityType = EntityType.fromName(p_args[0]);
					if (entityType == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_known", new Object[] {p_args[0]}));
						break;
					}
					if (!entityType.isAlive()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("entity_not_alive", new Object[] {entityType.name()}));
						break;
					}
					if (!entityType.isSpawnable()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("spawn_deny", new Object[] {entityType.name()}));
						break;
					}
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[1]);
					if (off_player.isOnline()) {
						final Location location = off_player.getPlayer().getLocation();
						location.getWorld().spawn(location, entityType.getEntityClass());
						if (msg != null) {
							off_player.getPlayer().sendMessage(ChatColor.GREEN + msg);
						}
						return ZeltCmds.getLanguage().getString("log_spawn_player", new Object[] {p_player.getDisplayName(), off_player.getPlayer().getDisplayName(), entityType.name()});
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Mob_player", new Object[] {p_cmd}));
				break;
			}
		}
		return null;
	}
}
