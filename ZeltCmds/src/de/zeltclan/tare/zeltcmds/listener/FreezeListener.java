package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class FreezeListener implements Listener {
	
	public FreezeListener() {
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent p_event) {
		if (!p_event.isCancelled() && p_event.getPlayer().hasMetadata("ZeltCmds_Player_Freeze")) {
			p_event.getPlayer().teleport(p_event.getPlayer().getLocation(), TeleportCause.PLUGIN);
			p_event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent p_event) {
		if (!p_event.isCancelled() && p_event.getPlayer().hasMetadata("ZeltCmds_Player_Freeze")) {
			p_event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent p_event) {
		if (!p_event.isCancelled() && p_event.getPlayer().hasMetadata("ZeltCmds_Player_Freeze")) {
			p_event.setCancelled(true);
		}
	}
}