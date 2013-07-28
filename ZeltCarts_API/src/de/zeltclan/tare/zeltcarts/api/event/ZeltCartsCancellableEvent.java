package de.zeltclan.tare.zeltcarts.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;

public class ZeltCartsCancellableEvent extends ZeltCartsEvent {
	
	private boolean cancelled;
	
	ZeltCartsCancellableEvent(final Minecart p_minecart, final Block p_rail) {
		super(p_minecart, p_rail);
		cancelled = false;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean p_cancelled) {
		cancelled = p_cancelled;
	}
}
