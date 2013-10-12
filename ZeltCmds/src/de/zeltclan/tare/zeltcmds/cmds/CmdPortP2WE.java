package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public class CmdPortP2WE extends CmdParent {

	private final String msg;
	
	public CmdPortP2WE(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_p2we"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {p_cmd}));
				break;
			case 5:
				final int y;
				try {
					y = Integer.parseInt(p_args[3]);
				} catch (NumberFormatException e) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[3]}));
					break;
				}
				if (y < 1) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("teleport_bad_location"));
					break;
				}
				final int x;
				try {
					x = Integer.parseInt(p_args[2]);
				} catch (NumberFormatException e) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[2]}));
					break;
				}
				final int z;
				try {
					z = Integer.parseInt(p_args[4]);
				} catch (NumberFormatException e) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[4]}));
					break;
				}
				final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
				if (!off_player.isOnline()) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
					break;
				}
				final Player player = off_player.getPlayer();
				final World world = player.getServer().getWorld(p_args[1]);
				if (world == null) {
					this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
					break;
				}
				if (!world.isChunkLoaded(x, z)) {
					if (!world.loadChunk(x, z, true)) {
						this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("chunk_not_load"));
						break;
					}
				}
				player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
				if (msg != null) {
					player.sendMessage(ChatColor.GREEN + msg);
				}
				break;
			default:
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
				this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {p_cmd}));
				break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
			case 1:
			case 2:
			case 3:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
			case 4:
				if (this.checkPerm(p_player, false)) {
					final int y;
					try {
						y = Integer.parseInt(p_args[3]);
					} catch (NumberFormatException e) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[3]}));
						break;
					}
					if (y < 1) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					final int x;
					try {
						x = Integer.parseInt(p_args[2]);
					} catch (NumberFormatException e) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[2]}));
						break;
					}
					final int z;
					try {
						z = Integer.parseInt(p_args[4]);
					} catch (NumberFormatException e) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("not_integer", new Object[] {p_args[4]}));
						break;
					}
					final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
					if (!off_player.isOnline()) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
						break;
					}
					final Player player = off_player.getPlayer();
					final World world = player.getServer().getWorld(p_args[1]);
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[1]}));
						break;
					}
					if (!world.isChunkLoaded(x, z)) {
						if (!world.loadChunk(x, z, true)) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					player.teleport(new Location(world, (x+0.5), y, (z+0.5)), TeleportCause.COMMAND);
					if (msg != null) {
						player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_p2we", new Object[] {x, y, z, world.getName(), p_player.getDisplayName(), player.getDisplayName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player_World_X_Y_Z", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
