package de.zeltclan.tare.bukkitutils;

import org.bukkit.entity.Entity;

public class EntityUtils {
	public static boolean isMinecart(final Entity p_entity) {
		switch (p_entity.getType()) {
			case MINECART:
			case MINECART_CHEST:
			case MINECART_FURNACE:
			case MINECART_HOPPER:
			case MINECART_MOB_SPAWNER:
			case MINECART_TNT:
				return true;
			default:
				return false;
		}
	}
}
