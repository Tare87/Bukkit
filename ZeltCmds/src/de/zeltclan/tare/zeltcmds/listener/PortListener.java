package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class PortListener implements Listener {
	
	final private Plugin plugin;
	
	public PortListener(final Plugin p_plugin) {
		plugin = p_plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent p_event) {
		if (!p_event.isCancelled() && p_event.getCause().equals(TeleportCause.COMMAND)) {
			final Location from = p_event.getFrom();
			final Player player = p_event.getPlayer();
			player.setMetadata("ZeltCmds_Port_Last_x", new FixedMetadataValue(plugin, String.valueOf(from.getBlockX())));
			player.setMetadata("ZeltCmds_Port_Last_y", new FixedMetadataValue(plugin, String.valueOf(from.getBlockY())));
			player.setMetadata("ZeltCmds_Port_Last_z", new FixedMetadataValue(plugin, String.valueOf(from.getBlockZ())));
			player.setMetadata("ZeltCmds_Port_Last_World", new FixedMetadataValue(plugin, String.valueOf(from.getWorld().getName())));
		}
	}
}