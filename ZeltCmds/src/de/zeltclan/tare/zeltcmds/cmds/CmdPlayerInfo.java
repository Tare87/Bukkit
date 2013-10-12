package de.zeltclan.tare.zeltcmds.cmds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdPlayerInfo extends CmdParent {

	public static enum Types implements Type {
		DIRECTION, INFO, IP, ITEM, POSITION, SEEN, TIME, WEATHER;
	}
	private final Types type;
	
	public CmdPlayerInfo(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener) {
		super(ZeltCmds.getLanguage().getString("description_playerinfo_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			for (String msg : this.getInformation(p_sender.getServer().getOfflinePlayer(p_args[0]))) {
				this.getPlugin().getLogger().info(msg);
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					for (String msg : this.getInformation(p_player)) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_playerinfo_self", new Object[] {type.name(), p_player.getName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					for (String msg : this.getInformation(p_player.getServer().getOfflinePlayer(p_args[0]))) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_playerinfo_player", new Object[] {type.name(), p_player.getName(), p_args[0]});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private List<String> getInformation (OfflinePlayer p_player) {
		List<String> result = new ArrayList<String>();
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
					result.add(ZeltCmds.getLanguage().getString("info_display", new Object[] {player.getDisplayName()}));
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
					if (player.isSprinting()) {
						result.add(ZeltCmds.getLanguage().getString("info_sprint"));
					} else {
						result.add(ZeltCmds.getLanguage().getString("info_no_sprint"));
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
				if (p_player.getPlayer().isPlayerTimeRelative()) {
					long time = p_player.getPlayer().getPlayerTime();
					time = time - (time >= 18000 ? 18000 : -6000);
					String hour = Integer.toString((int)(time / 1000));
					time %= 1000;
					String minute = Integer.toString((int)(1.000 * time / 1000 * 60));
					result.add(ZeltCmds.getLanguage().getString("info_time_" + ((p_player.getPlayer().getPlayerTimeOffset() == 0) ? "server" : "relative"), new Object[] {hour.length() < 2 ? "0" + hour : hour, minute.length() < 2 ? "0" + minute : minute}));
				} else {
					long time = p_player.getPlayer().getPlayerTimeOffset();
					time = time - (time >= 18000 ? 18000 : -6000);
					String hour = Integer.toString((int)(time / 1000));
					time %= 1000;
					String minute = Integer.toString((int)(1.000 * time / 1000 * 60));
					result.add(ZeltCmds.getLanguage().getString("info_time_absolute", new Object[] {hour.length() < 2 ? "0" + hour : hour, minute.length() < 2 ? "0" + minute : minute}));
				}
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		case WEATHER:
			if (p_player.isOnline()) {
				WeatherType weather = p_player.getPlayer().getPlayerWeather();
				if (weather == null) {
					result.add(ZeltCmds.getLanguage().getString("info_weather_server", new Object[] {p_player.getPlayer().getWorld().hasStorm() ? "downfall" : "clear"}));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_weather_player", new Object[] {weather.name().toLowerCase()}));
				}
				if (p_player.getPlayer().getWorld().isThundering()) {
					result.add(ZeltCmds.getLanguage().getString("info_weather_thunder"));
				} else {
					result.add(ZeltCmds.getLanguage().getString("info_weather_no_thunder"));
				}
			} else {
				result.add(ZeltCmds.getLanguage().getString(p_player.getFirstPlayed() != 0 ? "info_isoffline" : "info_unknown")); 
			}
			break;
		}
		return result;
	}
}
