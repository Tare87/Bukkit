package de.zeltclan.tare.zeltcarts.test;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsEnterEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsExitEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsMoveEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsPlayerEnterEvent;
import de.zeltclan.tare.zeltcarts.api.event.ZeltCartsPlayerExitEvent;
import de.zeltclan.tare.zeltcarts.api.service.DataManager;
import de.zeltclan.tare.zeltcarts.test.util.LogUtils;

class TestListener implements Listener {
	
	final private Plugin plugin;
	
	final private DataManager dataManager;
	
	TestListener(Plugin p_plugin, DataManager p_manager) {
		plugin = p_plugin;
		dataManager = p_manager;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZeltCartsMove(ZeltCartsMoveEvent p_event) {
		List<String> dataList = dataManager.getBlock(plugin, p_event.getRail());
		if (!dataList.isEmpty()) {
			LogUtils.info(plugin, "Found data at " + p_event.getRail().getLocation().getBlockX() + " / " + p_event.getRail().getLocation().getBlockY() + " / " + p_event.getRail().getLocation().getBlockZ());
			for (String data : dataList) {
				LogUtils.info(plugin, data);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZeltCartsEnter(ZeltCartsEnterEvent p_event) {
		LogUtils.info(plugin, "An entity entered a minecart at " + p_event.getRail().getLocation().getBlockX() + " / " + p_event.getRail().getLocation().getBlockY() + " / " + p_event.getRail().getLocation().getBlockZ());
		LogUtils.info(plugin, "Entityclass: " + p_event.getEntity().getClass().getSimpleName());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZeltCartsExit(ZeltCartsExitEvent p_event) {
		LogUtils.info(plugin, "An entity exited a minecart at " + p_event.getRail().getLocation().getBlockX() + " / " + p_event.getRail().getLocation().getBlockY() + " / " + p_event.getRail().getLocation().getBlockZ());
		LogUtils.info(plugin, "Entityclass: " + p_event.getEntity().getClass().getSimpleName());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZeltCartsPlayerEnter(ZeltCartsPlayerEnterEvent p_event) {
		LogUtils.info(plugin, "A player entered a minecart at " + p_event.getRail().getLocation().getBlockX() + " / " + p_event.getRail().getLocation().getBlockY() + " / " + p_event.getRail().getLocation().getBlockZ());
		LogUtils.info(plugin, "Playername: " + p_event.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onZeltCartsPlayerExit(ZeltCartsPlayerExitEvent p_event) {
		LogUtils.info(plugin, "A player exited a minecart at " + p_event.getRail().getLocation().getBlockX() + " / " + p_event.getRail().getLocation().getBlockY() + " / " + p_event.getRail().getLocation().getBlockZ());
		LogUtils.info(plugin, "Playername: " + p_event.getPlayer().getName());
	}
	
}