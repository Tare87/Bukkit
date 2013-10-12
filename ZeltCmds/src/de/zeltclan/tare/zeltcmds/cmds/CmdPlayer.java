package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdPlayer extends CmdParent {

	public static enum Types implements Type {
		ANVIL, CLEANCHAT, CLEARINVENTORY, ENCHANTING, ENDERCHEST, FEED, HEAL, KILL, LEVELDOWN, LEVELRESET, LEVELUP, STARVE, WORKBENCH;
	}
	
	private final Types type;
	private final String msg;
	
	public CmdPlayer(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_player_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		msg = (!p_msg.isEmpty() ? p_msg : null);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				this.action(player);
				if (msg != null) {
					player.sendMessage(ChatColor.GREEN + msg);
				}
			} else {
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
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
					this.action(p_player);
					if (msg != null) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_player_self", new Object[] {type.name(), p_player.getName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (off_player.isOnline()) {
						final Player player = off_player.getPlayer();
						this.action(player);
						if (msg != null) {
							player.sendMessage(ChatColor.GREEN + msg);
						}
						return ZeltCmds.getLanguage().getString("log_player_player", new Object[] {type.name(), p_player.getName(), player.getName()});
					} else {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					}
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private void action (Player p_player) {
		switch (type) {
			case ANVIL:
				p_player.openInventory(p_player.getServer().createInventory(null, InventoryType.ANVIL));
				break;
			case CLEANCHAT:
				for (int i = 0; i < 60; i++) {
					p_player.sendRawMessage("");
				}
				break;
			case CLEARINVENTORY:
				p_player.getInventory().clear();
				break;
			case ENCHANTING:
				p_player.openEnchanting(null, true);
				break;
			case ENDERCHEST:
				p_player.openInventory(p_player.getEnderChest());
				break;
			case FEED:
				p_player.setFoodLevel(20);
				p_player.setSaturation(10F);
				break;
			case HEAL:
				p_player.setHealth(p_player.getMaxHealth());
				break;
			case KILL:
				p_player.setHealth(0D);
				break;
			case LEVELDOWN:
				p_player.setLevel(p_player.getLevel()-1);
				break;
			case LEVELRESET:
				p_player.setLevel(0);
				p_player.setExp(0F);
				break;
			case LEVELUP:
				p_player.setLevel(p_player.getLevel()+1);
				break;
			case STARVE:
				p_player.setFoodLevel(0);
				p_player.setSaturation(0F);
				break;
			case WORKBENCH:
				p_player.openWorkbench(null, true);
				break;
		}
	}
}
