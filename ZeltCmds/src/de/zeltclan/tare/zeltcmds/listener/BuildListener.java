package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BuildListener implements Listener {
	
	public BuildListener() {
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent p_event) {
		if (!p_event.isCancelled() && p_event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && p_event.getPlayer().hasMetadata("ZeltCmds_Player_Build") && !p_event.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
			p_event.setUseInteractedBlock(Event.Result.DENY);
			p_event.setUseItemInHand(Event.Result.ALLOW);
		}
	}
}