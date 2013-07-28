package de.zeltclan.tare.zeltcarts.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

public class ZeltCartsEnterEvent extends ZeltCartsCancellableEvent {
	
	private Entity entity;
	
	public ZeltCartsEnterEvent(final Minecart p_minecart, final Block p_rail, final Entity p_entity) {
		super(p_minecart, p_rail);
		entity = p_entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
}
