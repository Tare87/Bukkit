package de.zeltclan.tare.zeltcarts.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;

public class ZeltCartsMoveEvent extends ZeltCartsEvent {
	
	public ZeltCartsMoveEvent(final Minecart p_minecart, final Block p_rail) {
		super(p_minecart, p_rail);
	}
}
