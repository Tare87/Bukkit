package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class DeathListener implements Listener {
	
	final private Plugin plugin;
	
	public DeathListener(final Plugin p_plugin) {
		plugin = p_plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent p_event) {
		final Entity entity = p_event.getEntity();
		if (entity instanceof Player) {
			final Location location = entity.getLocation();
			entity.setMetadata("ZeltCmds_Death_Last_x", new FixedMetadataValue(plugin, String.valueOf(location.getBlockX())));
			entity.setMetadata("ZeltCmds_Death_Last_y", new FixedMetadataValue(plugin, String.valueOf(location.getBlockY())));
			entity.setMetadata("ZeltCmds_Death_Last_z", new FixedMetadataValue(plugin, String.valueOf(location.getBlockZ())));
			entity.setMetadata("ZeltCmds_Death_Last_World", new FixedMetadataValue(plugin, String.valueOf(location.getWorld().getName())));
		}
	}
}