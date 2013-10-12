package de.zeltclan.tare.zeltcmds.cmds;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.utils.LocationUtils;

public class CmdPortM2D extends CmdParent {

	private final String msg;

	public CmdPortM2D(Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_port_m2d"), p_perm, p_permExt, p_listener);
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("command_console_no_use"));
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					if (!p_player.hasMetadata("ZeltCmds_Death_Last_x") || !p_player.hasMetadata("ZeltCmds_Death_Last_y") || !p_player.hasMetadata("ZeltCmds_Death_Last_z") || !p_player.hasMetadata("ZeltCmds_Death_Last_World")) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("no_death_last"));
						break;
					}
					int x = 0;
					List<MetadataValue> metadata = p_player.getMetadata("ZeltCmds_Death_Last_x");
					for (MetadataValue value : metadata) {
						x = value.asInt();
						break;
					}
					int y = 0;
					metadata = p_player.getMetadata("ZeltCmds_Death_Last_y");
					for (MetadataValue value : metadata) {
						y = value.asInt();
						break;
					}
					int z = 0;
					metadata = p_player.getMetadata("ZeltCmds_Death_Last_z");
					for (MetadataValue value : metadata) {
						z = value.asInt();
						break;
					}
					World world = p_player.getWorld();
					metadata = p_player.getMetadata("ZeltCmds_Death_Last_World");
					for (MetadataValue value : metadata) {
						world = p_player.getServer().getWorld(value.asString());
						break;
					}
					if (world == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_args[0]}));
						break;
					}
					final Location target = LocationUtils.getSafeLocation(world.getBlockAt(x, y, z).getLocation().add(0, 1, 0));
					if (target == null) {
						p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("teleport_bad_location"));
						break;
					}
					if (!target.getChunk().isLoaded()) {
						if (!target.getChunk().load(true)) {
							p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("chunk_not_load"));
							break;
						}
					}
					p_player.teleport(target, TeleportCause.COMMAND);
					if (msg != null) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_port_m2d", new Object[] {p_player.getName()});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}

}
