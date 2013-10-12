package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdPlayerSet extends CmdParent {

	public static enum Types implements Type {
		SETFLYSPEED, SETMAXAIR, SETMAXHEALTH, SETTIMEABSOLUTE, SETTIMERELATIVE, SETWALKSPEED, SETWEATHER;
	}
	
	private final Types type;
	private final String[] msg;
	
	public CmdPlayerSet(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_playerset_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
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
		final OfflinePlayer off_player;
		switch (p_args.length) {
		case 0:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_param", new Object[] {p_cmd}));
			break;
		case 1:
			off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				this.reset(player);
				if (msg != null && msg[1] != null) {
					player.sendMessage(ChatColor.GREEN + msg[1]);
				}
			} else {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
			}
			break;
		case 2:
			off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				switch (type) {
					case SETFLYSPEED:
						final float flyspeed;
						try {
							flyspeed = Float.parseFloat(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setFlySpeed(flyspeed);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETMAXAIR:
						final int maxair;
						try {
							maxair = Integer.parseInt(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setMaximumAir(maxair);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETMAXHEALTH:
						final double maxhealth;
						try {
							maxhealth = Double.parseDouble(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_double", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setMaxHealth(maxhealth);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETTIMEABSOLUTE:
						final long timeabs;
						try {
							timeabs = Long.parseLong(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setPlayerTime(timeabs, false);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETTIMERELATIVE:
						final long timerel;
						try {
							timerel = Long.parseLong(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setPlayerTime(timerel, true);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETWALKSPEED:
						final float walkspeed;
						try {
							walkspeed = Float.parseFloat(p_args[1]);
						} catch (NumberFormatException e) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[1]}));
							break;
						}
						try {
							player.setWalkSpeed(walkspeed);
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
						}
						break;
					case SETWEATHER:
						final WeatherType weather;
						try {
							weather = WeatherType.valueOf(p_args[1].toUpperCase());
						} catch (IllegalArgumentException e) {
							this.getPlugin().getLogger().warning(e.getLocalizedMessage());
							break;
						}
						if (weather == null) {
							this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_weathertype", new Object[] {p_args[1]}));
							break;
						}
						player.setPlayerWeather(weather);
						break;
				}
				if (msg != null && msg[0] != null) {
					player.sendMessage(ChatColor.GREEN + msg[0]);
				}
			} else {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_param", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		final OfflinePlayer off_player;
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					this.reset(p_player);
					if (msg != null && msg[1] != null) {
						p_player.sendMessage(ChatColor.GREEN + msg[1]);
					}
					return ZeltCmds.getLanguage().getString("log_playerset_self_reset", new Object[] {type.name(), p_player.getName()});
				}
				break;
			case 1:
				off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline() && off_player.getFirstPlayed() == 0) {
					if (this.checkPerm(p_player, false)) {
						switch (type) {
							case SETFLYSPEED:
								final float flyspeed;
								try {
									flyspeed = Float.parseFloat(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setFlySpeed(flyspeed);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETMAXAIR:
								final int maxair;
								try {
									maxair = Integer.parseInt(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setMaximumAir(maxair);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETMAXHEALTH:
								final double maxhealth;
								try {
									maxhealth = Double.parseDouble(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_double", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setMaxHealth(maxhealth);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETTIMEABSOLUTE:
								final long timeabs;
								try {
									timeabs = Long.parseLong(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setPlayerTime(timeabs, false);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETTIMERELATIVE:
								final long timerel;
								try {
									timerel = Long.parseLong(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setPlayerTime(timerel, true);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETWALKSPEED:
								final float walkspeed;
								try {
									walkspeed = Float.parseFloat(p_args[0]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setWalkSpeed(walkspeed);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETWEATHER:
								final WeatherType weather = WeatherType.valueOf(p_args[0]);
								if (weather == null) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_weathertype", new Object[] {p_args[0]}));
									break;
								}
								try {
									p_player.setPlayerWeather(weather);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
						}
						if (msg != null && msg[0] != null) {
							p_player.sendMessage(ChatColor.GREEN + msg[0]);
						}
						return ZeltCmds.getLanguage().getString("log_playerset_self_set", new Object[] {type.name(), p_player.getName(), p_args[0]});
					}
				} else {
					if (this.checkPerm(p_player, true)) {
						final Player player = off_player.getPlayer();
						this.reset(player);
						if (msg != null && msg[1] != null) {
							player.sendMessage(ChatColor.GREEN + msg[1]);
						}
						return ZeltCmds.getLanguage().getString("log_playerset_player_reset", new Object[] {type.name(), p_player.getName(), player.getName()});
					}
				}
				break;
			case 2:
				if (this.checkPerm(p_player, true)) {
					off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (off_player.isOnline()) {
						final Player player = off_player.getPlayer();
						switch (type) {
							case SETFLYSPEED:
								final float flyspeed;
								try {
									flyspeed = Float.parseFloat(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setFlySpeed(flyspeed);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETMAXAIR:
								final int maxair;
								try {
									maxair = Integer.parseInt(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setMaximumAir(maxair);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETMAXHEALTH:
								final double maxhealth;
								try {
									maxhealth = Double.parseDouble(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_double", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setMaxHealth(maxhealth);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETTIMEABSOLUTE:
								final long timeabs;
								try {
									timeabs = Long.parseLong(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setPlayerTime(timeabs, false);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETTIMERELATIVE:
								final long timerel;
								try {
									timerel = Long.parseLong(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_long", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setPlayerTime(timerel, true);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETWALKSPEED:
								final float walkspeed;
								try {
									walkspeed = Float.parseFloat(p_args[1]);
								} catch (NumberFormatException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_float", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setWalkSpeed(walkspeed);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
							case SETWEATHER:
								final WeatherType weather = WeatherType.valueOf(p_args[1]);
								if (weather == null) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_weathertype", new Object[] {p_args[1]}));
									break;
								}
								try {
									player.setPlayerWeather(weather);
								} catch (IllegalArgumentException e) {
									p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + e.getLocalizedMessage());
								}
								break;
						}
						if (msg != null && msg[0] != null) {
							player.sendMessage(ChatColor.GREEN + msg[0]);
						}
						return ZeltCmds.getLanguage().getString("log_playerset_player_set", new Object[] {type.name(), p_player.getName(), player.getName(), p_args[1]});
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player_param", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private void reset(Player p_player) {
		switch (type) {
		case SETFLYSPEED:
			p_player.setFlySpeed(0.1F);
			break;
		case SETMAXAIR:
			p_player.setMaximumAir(300);
			break;
		case SETMAXHEALTH:
			p_player.resetMaxHealth();
			break;
		case SETTIMEABSOLUTE:
		case SETTIMERELATIVE:
			p_player.resetPlayerTime();
			break;
		case SETWALKSPEED:
			p_player.setWalkSpeed(0.2F);
			break;
		case SETWEATHER:
			p_player.resetPlayerWeather();
			break;
	}
	}
}
