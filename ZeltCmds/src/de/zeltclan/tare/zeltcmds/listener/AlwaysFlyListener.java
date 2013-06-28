package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class AlwaysFlyListener implements Listener {
	
	public AlwaysFlyListener() {
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent p_event) {
		if (!p_event.isCancelled() && p_event.getPlayer().hasMetadata("ZeltCmds_Player_AlwaysFly") && !p_event.isFlying()) {
			p_event.getPlayer().setAllowFlight(true);
			p_event.getPlayer().teleport(p_event.getPlayer().getLocation().add(0.0,0.0001,0.0), TeleportCause.PLUGIN);
			p_event.setCancelled(true);
		}
	}
}