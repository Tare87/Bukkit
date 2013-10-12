package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class DeathListener implements Listener {
	
	final private Plugin plugin;
	
	public DeathListener(final Plugin p_plugin) {
		plugin = p_plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent p_event) {
		final Player player = p_event.getEntity();
		final Location location = player.getLocation();
		player.setMetadata("ZeltCmds_Death_Last_x", new FixedMetadataValue(plugin, String.valueOf(location.getBlockX())));
		player.setMetadata("ZeltCmds_Death_Last_y", new FixedMetadataValue(plugin, String.valueOf(location.getBlockY())));
		player.setMetadata("ZeltCmds_Death_Last_z", new FixedMetadataValue(plugin, String.valueOf(location.getBlockZ())));
		player.setMetadata("ZeltCmds_Death_Last_World", new FixedMetadataValue(plugin, String.valueOf(location.getWorld().getName())));
	}
}