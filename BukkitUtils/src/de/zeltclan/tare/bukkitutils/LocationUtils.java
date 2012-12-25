package de.zeltclan.tare.bukkitutils;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;

public class LocationUtils {
	
	private static final HashSet<Integer> materials_passable = new HashSet<Integer>();
	private static final HashSet<Integer> materials_damaging = new HashSet<Integer>();
	
	static {
		// air
		materials_passable.add(Material.AIR.getId());
		// climbable
		materials_passable.add(Material.LADDER.getId());
		materials_passable.add(Material.VINE.getId());
		// deco
		materials_passable.add(Material.DEAD_BUSH.getId());
		materials_passable.add(Material.LONG_GRASS.getId());
		materials_passable.add(Material.WATER_LILY.getId());
		// door
		materials_passable.add(Material.IRON_DOOR_BLOCK.getId());
		materials_passable.add(Material.TRAP_DOOR.getId());
		materials_passable.add(Material.WOODEN_DOOR.getId());
		// plant
		materials_passable.add(Material.BROWN_MUSHROOM.getId());
		materials_passable.add(Material.MELON_STEM.getId());
		materials_passable.add(Material.NETHER_WARTS.getId());
		materials_passable.add(Material.PUMPKIN_STEM.getId());
		materials_passable.add(Material.RED_MUSHROOM.getId());
		materials_passable.add(Material.RED_ROSE.getId());
		materials_passable.add(Material.SAPLING.getId());
		materials_passable.add(Material.SEEDS.getId());
		materials_passable.add(Material.SUGAR_CANE_BLOCK.getId());
		materials_passable.add(Material.YELLOW_FLOWER.getId());
		// rail
		materials_passable.add(Material.DETECTOR_RAIL.getId());
		materials_passable.add(Material.POWERED_RAIL.getId());
		materials_passable.add(Material.RAILS.getId());
		// redstone
		materials_passable.add(Material.DIODE_BLOCK_OFF.getId());
		materials_passable.add(Material.DIODE_BLOCK_ON.getId());
		materials_passable.add(Material.LEVER.getId());
		materials_passable.add(Material.REDSTONE_TORCH_OFF.getId());
		materials_passable.add(Material.REDSTONE_TORCH_ON.getId());
		materials_passable.add(Material.REDSTONE_WIRE.getId());
		materials_passable.add(Material.STONE_BUTTON.getId());
		materials_passable.add(Material.STONE_PLATE.getId());
		materials_passable.add(Material.WOOD_PLATE.getId());
		// sign
		materials_passable.add(Material.SIGN_POST.getId());
		materials_passable.add(Material.WALL_SIGN.getId());
		// torch
		materials_passable.add(Material.TORCH.getId());
		
		// bed
		materials_damaging.add(Material.BED_BLOCK.getId());
		// cactus
		materials_damaging.add(Material.CACTUS.getId());
		// fire
		materials_damaging.add(Material.FIRE.getId());
		// lava
		materials_damaging.add(Material.LAVA.getId());
		materials_damaging.add(Material.STATIONARY_LAVA.getId());
	}

	public static Location getSafeLocation(final Location p_location) {
		if (p_location == null || p_location.getWorld() == null) {
			return null;
		}
		
		Location location_safe = null;
		for (int radius = 0; radius < 5; radius++) {
			location_safe = findSafeLocationOnCycle(p_location, radius);
			if (location_safe != null) {
				return normalizeLocation(location_safe);
			}
		}
		
		for (int radius = 0; radius < 5; radius++) {
			location_safe = findSafeTopLocationOnCycle(p_location, radius);
			if (location_safe != null) {
				return normalizeLocation(location_safe);
			}
		}
		
		return null;
	}
	
	private static Location findSafeLocationOnCycle(Location p_location, final int p_radius) {
		if (p_radius == 0) {
			Location location_check = findSafeLocation(p_location);
			if (location_check != null) {
				return location_check;
			}
			return null;
		}
		final int steps = 2 * p_radius;
		p_location.add(-p_radius, 0, -p_radius);
		for (int side = 0; side < 4; side++) {
			for (int step = 0; step < steps; step++) {
				Location location_check = findSafeLocation(p_location);
				if (location_check != null) {
					return location_check;
				}
				switch (side) {
					case 0:
						p_location = p_location.add(1, 0, 0);
						break;
					case 1:
						p_location = p_location.add(0, 0, 1);
						break;
					case 2:
						p_location = p_location.subtract(1, 0, 0);
						break;
					case 3:
						p_location = p_location.subtract(0, 0, 1);
						break;						
				}
			}
		}
		return null;
	}
	
	private static Location findSafeLocation(final Location p_location) {
		Location location_check = p_location;
		if (isPassable(location_check)) {
			location_check = getGround(location_check);
			if (location_check != null && !isDamaging(location_check)) {
				return location_check.add(0, 1, 0);
			}
		}
		return null;
	}
	
	private static boolean isPassable(Location p_location) {
		return (materials_passable.contains(p_location.getBlock().getTypeId()) && materials_passable.contains(p_location.add(0, 1, 0).getBlock().getTypeId()));
	}
	
	private static Location getGround(Location p_location) {
		do {
			p_location = p_location.subtract(0, 1, 0);
			if (p_location.getBlockY() < 0) {
				return null;
			}
		} while (materials_passable.contains(p_location.getBlock().getTypeId()));
		return p_location;
	}
	
	private static boolean isDamaging(Location p_location) {
		return materials_damaging.contains(p_location.getBlock().getTypeId());
	}
	
	private static Location findSafeTopLocationOnCycle(Location p_location, final int p_radius) {
		if (p_radius == 0) {
			Location location_check = findSafeTopLocation(p_location);
			if (location_check != null) {
				return location_check;
			}
			return null;
		}
		final int steps = 2 * p_radius;
		p_location.add(-p_radius, 0, -p_radius);
		for (int side = 0; side < 4; side++) {
			for (int step = 0; step < steps; step++) {
				Location location_check = findSafeTopLocation(p_location);
				if (location_check != null) {
					return location_check;
				}
				switch (side) {
					case 0:
						p_location = p_location.add(1, 0, 0);
						break;
					case 1:
						p_location = p_location.add(0, 0, 1);
						break;
					case 2:
						p_location = p_location.subtract(1, 0, 0);
						break;
					case 3:
						p_location = p_location.subtract(0, 0, 1);
						break;						
				}
			}
		}
		return null;
	}
	
	private static Location findSafeTopLocation(final Location p_location) {
		Location location_check = p_location.getWorld().getHighestBlockAt(p_location).getLocation();
		if (!isDamaging(location_check)) {
				return location_check.add(0, 1, 0);
		}
		return null;
	}
	
	private static Location normalizeLocation(Location p_location) {
		p_location.setX(p_location.getBlockX() + 0.5);
		p_location.setY(p_location.getBlockY() + 0.2);
		p_location.setZ(p_location.getBlockZ() + 0.5);
		return p_location;
	}
}
