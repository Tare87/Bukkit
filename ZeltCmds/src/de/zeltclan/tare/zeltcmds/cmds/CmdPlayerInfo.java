package de.zeltclan.tare.zeltcmds.cmds;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdPlayerInfo extends CmdParent {

	public static enum Types implements Type {
		DIRECTION, INFO, IP, ITEM, POSITION, SEEN, TIME;
	}
	private final Types type;
	
	public CmdPlayerInfo(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener) {
		super(ZeltCmds.getLanguage().getString("description_info_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			final String[] info = this.getInformation(p_sender.getServer().getOfflinePlayer(p_args[0]));
			for (String msg : info) {
				MessageUtils.msg(p_sender, msg);
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
		final String[] info;
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					info = this.getInformation(p_player);
					for (String msg : info) {
						MessageUtils.msg(p_player, msg);
					}
					return (ZeltCmds.getLanguage().getString("log_info_self", new Object[] {type.name(), p_player.getDisplayName()}));
				}
				break;
			case 1:
				if (this.checkPerm(p_player, false)) {
					info = this.getInformation(p_player.getServer().getOfflinePlayer(p_args[0]));
					for (String msg : info) {
						MessageUtils.msg(p_player, msg);
					}
					return (ZeltCmds.getLanguage().getString("log_info_player", new Object[] {type.name(), p_player.getDisplayName(), p_args[0]}));
				}
				break;
			default:
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private String[] getInformation (OfflinePlayer p_player) {
		ArrayList<String> result = new ArrayList<String>();
		switch (type) {
		case DIRECTION:
			if (p_player.isOnline()) {
				final Player player = p_player.getPlayer();
				final String dir;
				final int deg = ((int)player.getLocation().getYaw() + 180 + 360) % 360;
				if ((deg < 23) || (deg >= 338)) {
					dir = "north";
				} else if ((deg >= 23) && (deg < 68)) {
					dir = "north-east";
				} else if ((deg >= 68) && (deg < 113)) {
					dir = "east";
				} else if ((deg >= 113) && (deg < 158)) {
					dir = "south-east";
				} else if ((deg >= 158) && (deg < 203)) {
					dir = "south";
				} else if((deg >= 203) && (deg < 248)) {
					dir = "south-west";
				} else if ((deg >= 248) && (deg < 293)) {
					dir = "west";
				} else {
					dir = "north-west";
				}
				result.add(ZeltCmds.getLanguage().getString("direction_" + dir));
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		case INFO:
			result.add(ZeltCmds.getLanguage().getString("info_name", new Object[] {p_player.getName()}));
			if (p_player.getFirstPlayed() != 0) {
				result.add(ZeltCmds.getLanguage().getString("info_first", new Object[] {new Date(p_player.getFirstPlayed())}));
				if (p_player.isOnline()) {
					result.add(ZeltCmds.getLanguage().getString("info_isonline"));
					final Player player = p_player.getPlayer();
					result.add(ZeltCmds.getLanguage().getString("info_location", new Object[] {player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getWorld().getName()}));
					result.add(ZeltCmds.getLanguage().getString("info_mode", new Object[] {player.getGameMode().name()}));
					if (player.getAllowFlight()) {
						result.add(ZeltCmds.getLanguage().getString("info_fly_allow"));
					} else {
						result.add(ZeltCmds.getLanguage().getString("info_fly_disallow"));
					}
					if (player.isFlying()) {
						result.add(ZeltCmds.getLanguage().getString("info_fly"));
					} else {
						result.add(ZeltCmds.getLanguage().getString("info_no_fly"));
					}
					if (player.isSneaking()) {
						result.add(ZeltCmds.getLanguage().getString("info_sneak"));
					} else {
						result.add(ZeltCmds.getLanguage().getString("info_no_sneak"));
					}
					String[] metaList = {"Mute", "Freeze", "AlwaysFly", "Build"};
					for (String meta : metaList) { 
						if (player.hasMetadata("ZeltCmds_Player_" + meta)) {
							result.add(ZeltCmds.getLanguage().getString("info_" + meta.toLowerCase()));
						} else {
							result.add(ZeltCmds.getLanguage().getString("info_no_" + meta.toLowerCase()));
						}
					}
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_isoffline"));
					result.add(ZeltCmds.getLanguage().getString("info_seen", new Object[] {new Date(p_player.getLastPlayed())}));
				}
				if (p_player.getBedSpawnLocation() != null) {
					result.add(ZeltCmds.getLanguage().getString("info_bedspawn", new Object[] {p_player.getBedSpawnLocation().getBlockX(), p_player.getBedSpawnLocation().getBlockY(), p_player.getBedSpawnLocation().getBlockZ(), p_player.getBedSpawnLocation().getWorld().getName()}));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_no_bedspawn"));
				}
				if (p_player.isOp()) {
					result.add(ZeltCmds.getLanguage().getString("info_op"));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_no_op"));
				}
				if (p_player.isWhitelisted()) {
					result.add(ZeltCmds.getLanguage().getString("info_whitelist"));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_no_whitelist"));
				}
				if (p_player.isBanned()) {
					result.add(ZeltCmds.getLanguage().getString("info_ban"));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_no_ban"));
				}
			} else {
				result.add(ZeltCmds.getLanguage().getString("info_unknown"));
			}
			break;
		case IP:
			if (p_player.isOnline()) {
				result.add(ZeltCmds.getLanguage().getString("info_ip", new Object[] {p_player.getPlayer().getAddress().getAddress().toString().replace("/", "")}));
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		case ITEM:
			if (p_player.isOnline()) {
				final ItemStack item = p_player.getPlayer().getItemInHand();
				result.add(ZeltCmds.getLanguage().getString("info_item_name", new Object[] {item.getType().name()}));
				result.add(ZeltCmds.getLanguage().getString("info_item_id", new Object[] {item.getTypeId(), item.getData().getData()}));
				if (item.getType().getMaxDurability() > 0) {
					result.add(ZeltCmds.getLanguage().getString("info_item_durability", new Object[]{item.getType().getMaxDurability() - item.getDurability()}));
				}
				if (item.getItemMeta().hasEnchants()) {
					result.add(ZeltCmds.getLanguage().getString("info_item_enchantment"));
					for (Enchantment enchantment : item.getEnchantments().keySet()) {
						result.add(enchantment.getName() + " " + item.getEnchantmentLevel(enchantment));
					}
				}
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		case POSITION:
			if (p_player.isOnline()) {
				final Player player = p_player.getPlayer();
				result.add(ZeltCmds.getLanguage().getString("info_location", new Object[] {player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getWorld().getName()}));
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		case SEEN:
			if (p_player.isOnline()) {
				result.add(ZeltCmds.getLanguage().getString("info_isonline"));
			} else if (p_player.getFirstPlayed() != 0) {
				result.add(ZeltCmds.getLanguage().getString("info_seen", new Object[] {new Date(p_player.getLastPlayed())}));
			} else {
				result.add(ZeltCmds.getLanguage().getString("info_unknown"));
			}
			break;
		case TIME:
			if (p_player.isOnline()) {
				long time = p_player.getPlayer().getWorld().getTime();
				time = time - (time >= 18000 ? 18000 : -6000);
				String hour = Integer.toString((int)(time / 1000));
				time %= 1000;
				String minute = Integer.toString((int)(1.000 * time / 1000 * 60));
				result.add(ZeltCmds.getLanguage().getString("info_time", new Object[] {hour.length() < 2 ? "0" + hour : hour, minute.length() < 2 ? "0" + minute : minute}));
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		}
		return result.toArray(new String[result.size()]);
	}
}
