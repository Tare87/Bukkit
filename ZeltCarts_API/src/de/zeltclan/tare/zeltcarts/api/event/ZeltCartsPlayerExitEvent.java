package de.zeltclan.tare.zeltcarts.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class ZeltCartsPlayerExitEvent extends ZeltCartsCancellableEvent {
	
	private Player player;
	
	public ZeltCartsPlayerExitEvent(final Minecart p_minecart, final Block p_rail, final Player p_player) {
		super(p_minecart, p_rail);
		player = p_player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
