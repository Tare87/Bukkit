package de.zeltclan.tare.zeltcarts.api.util;

import org.bukkit.block.Block;

public class BlockUtils {
	public static boolean isRail(final Block p_block) {
		if (p_block == null) {
			return false;
		}
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
}
