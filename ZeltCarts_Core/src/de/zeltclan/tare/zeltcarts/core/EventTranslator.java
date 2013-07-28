package de.zeltclan.tare.zeltcarts.core;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsCancellableEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsEnterEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsExitEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsMoveEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsPlayerEnterEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsPlayerExitEvent;

class EventTranslator implements Listener {
	
	final private HashMap<Integer, String> minecartMap = new HashMap<Integer, String>();
	
	EventTranslator() {
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMinecartMove(VehicleMoveEvent p_event) {
		if (p_event.getVehicle() instanceof Minecart) {
			final Minecart minecart = (Minecart) p_event.getVehicle();
			final int minecartID = minecart.getEntityId();
			final Location location = p_event.getFrom();
			final String locationString = location.getWorld().getName() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
			if (!minecartMap.containsKey(minecartID) || !minecartMap.get(minecartID).equals(locationString)) {
				minecartMap.put(minecartID, locationString);
				minecart.getServer().getPluginManager().callEvent(new ZeltCartsMoveEvent(minecart, location.getBlock()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMinecartEnter(VehicleEnterEvent p_event) {
		if (!p_event.isCancelled()) {
			if (!(p_event.getEntered() instanceof Player)) {
				if (p_event.getVehicle() instanceof Minecart) {
					final Minecart minecart = (Minecart) p_event.getVehicle();
					final Block block = minecart.getLocation().getBlock();
					final ZeltCartsEnterEvent zc_event = new ZeltCartsEnterEvent(minecart, block, p_event.getEntered());
					minecart.getServer().getPluginManager().callEvent(zc_event);
					p_event.setCancelled(zc_event.isCancelled());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMinecartExit(VehicleExitEvent p_event) {
		if (!p_event.isCancelled()) {
			if (!(p_event.getExited() instanceof Player)) {
				if (p_event.getVehicle() instanceof Minecart) {
					final Minecart minecart = (Minecart) p_event.getVehicle();
					final Block block = minecart.getLocation().getBlock();
					final ZeltCartsExitEvent zc_event = new ZeltCartsExitEvent(minecart, block, p_event.getExited());
					minecart.getServer().getPluginManager().callEvent(zc_event);
					p_event.setCancelled(zc_event.isCancelled());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMinecartInteract(PlayerInteractEntityEvent p_event) {
		if (p_event.getRightClicked() instanceof Minecart) {
			final Minecart minecart = (Minecart) p_event.getRightClicked();
			final Block block = minecart.getLocation().getBlock();
			final ZeltCartsCancellableEvent zc_event;
			if (minecart.getPassenger() != null) {
				if (minecart.getPassenger().equals(p_event.getPlayer())) {
					zc_event = new ZeltCartsPlayerExitEvent(minecart, block, p_event.getPlayer());
					minecart.getServer().getPluginManager().callEvent(zc_event);
				} else {
					zc_event = null;
				}
			} else {
				zc_event = new ZeltCartsPlayerEnterEvent(minecart, block, p_event.getPlayer());
				minecart.getServer().getPluginManager().callEvent(zc_event);
			}
			if (zc_event != null) {
				p_event.setCancelled(zc_event.isCancelled());
			}
		}
	}
}