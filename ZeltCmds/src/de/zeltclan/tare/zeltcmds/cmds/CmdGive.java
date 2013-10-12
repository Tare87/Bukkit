package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;
import de.zeltclan.tare.zeltcmds.utils.ItemUtils;

public final class CmdGive extends CmdParent {

	public static enum Types implements Type {
		ITEM, STACK, CHEST;
	}
	private final Types type;
	private final String msg;
	
	public CmdGive(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_give_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		msg = (!p_msg.isEmpty() ? p_msg : null);
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
		case 1:
		case 2:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_Item_Amount", new Object[] {p_cmd}));
			break;
		case 3:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				final ItemStack item = ItemUtils.getItemStack(p_args[1]);
				if (item != null && item.getTypeId() != 0) {
					int amount;
					try {
						amount = Integer.parseInt(p_args[2]);
					} catch (NumberFormatException e) {
						this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[2]}));
						break;
					}
					switch (type) {
					case STACK:
						amount *= item.getMaxStackSize();
						break;
					case CHEST:
						amount *= item.getMaxStackSize() * 27;
						break;
					default:
						break;
					}
					item.setAmount(item.getMaxStackSize());
					while (amount > item.getMaxStackSize()) {
						player.getWorld().dropItemNaturally(player.getLocation(), item);
						amount -= item.getMaxStackSize();
					}
					item.setAmount(amount);
					player.getWorld().dropItemNaturally(player.getLocation(), item);
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
				} else {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("item_not_found", new Object[] {p_args[1]}));
				}
			} else {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_Item_Amount", new Object[] {p_cmd}));
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player_Item_Amount", new Object[] {"/" + p_cmd}));
				break;
			case 2:
				if (this.checkPerm(p_player, false)) {
					final ItemStack item = ItemUtils.getItemStack(p_args[0]);
					if (item != null && item.getTypeId() != 0) {
						int amount;
						try {
							amount = Integer.parseInt(p_args[1]);
						} catch (NumberFormatException e) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[2]}));
							break;
						}
						switch (type) {
						case STACK:
							amount *= item.getMaxStackSize();
							break;
						case CHEST:
							amount *= item.getMaxStackSize() * 27;
							break;
						default:
							break;
						}
						item.setAmount(item.getMaxStackSize());
						while (amount > item.getMaxStackSize()) {
							p_player.getWorld().dropItemNaturally(p_player.getLocation(), item);
							amount -= item.getMaxStackSize();
						}
						item.setAmount(amount);
						p_player.getWorld().dropItemNaturally(p_player.getLocation(), item);
						if (msg != null) {
							p_player.sendMessage(ChatColor.GREEN + msg);
						}
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("item_not_found", new Object[] {p_args[1]}));
					}
					return ZeltCmds.getLanguage().getString("log_give_self", new Object[] {type.name(), p_player.getName(),p_args[1], p_args[0]});
				}
				break;
			case 3:
				if (this.checkPerm(p_player, true)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (off_player.isOnline()) {
						final Player player = off_player.getPlayer();
						final ItemStack item = ItemUtils.getItemStack(p_args[1]);
						if (item != null) {
							int amount;
							try {
								amount = Integer.parseInt(p_args[2]);
							} catch (NumberFormatException e) {
								p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[2]}));
								break;
							}
							switch (type) {
							case STACK:
								amount *= item.getMaxStackSize();
								break;
							case CHEST:
								amount *= item.getMaxStackSize() * 27;
								break;
							default:
								break;
							}
							item.setAmount(item.getMaxStackSize());
							while (amount > item.getMaxStackSize()) {
								player.getWorld().dropItemNaturally(player.getLocation(), item);
								amount -= item.getMaxStackSize();
							}
							item.setAmount(amount);
							player.getWorld().dropItemNaturally(player.getLocation(), item);
							if (msg != null) {
								player.sendMessage(ChatColor.GREEN + msg);
							}
						} else {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("item_not_found", new Object[] {p_args[1]}));
						}
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
					return ZeltCmds.getLanguage().getString("log_give_player", new Object[] {type.name(), p_player.getName(), p_args[0], p_args[2], p_args[1]});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player_Item_Amount", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
}
