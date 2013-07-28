package de.zeltclan.tare.zeltcarts.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.zeltclan.tare.zeltcarts.api.util.BlockUtils;

class ZeltCartsEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Minecart minecart;
	
	private final Block rail;
	
	ZeltCartsEvent(final Minecart p_minecart, final Block p_rail) {
		minecart = p_minecart;
		if (BlockUtils.isRail(p_rail) ) {
			rail = p_rail;
		} else {
			rail = null;
		}
	}
	
	public Minecart getMinecart() {
		return minecart;
	}
	
	public Block getRail() {
		return rail;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
