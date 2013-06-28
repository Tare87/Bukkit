package de.zeltclan.tare.bukkitutils;

import org.bukkit.block.Block;

public class BlockUtils {
	public static boolean isRail(final Block p_block) {
		switch (p_block.getType()) {
			case ACTIVATOR_RAIL:
			case DETECTOR_RAIL:
			case POWERED_RAIL:
			case RAILS:
				return true;
			default:
				return false;
		}
	}

	public static boolean isContainer(final Block p_block) {
		switch (p_block.getType()) {
			case CHEST:
			case DISPENSER:
			case DROPPER:
			case HOPPER:
			case HOPPER_MINECART:
			case LOCKED_CHEST:
			case STORAGE_MINECART:
			case TRAPPED_CHEST:
				return true;
			default:
				return false;
		}
	}
}
