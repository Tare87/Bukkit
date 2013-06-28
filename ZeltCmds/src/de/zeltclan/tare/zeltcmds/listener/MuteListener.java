package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener {
	
	public MuteListener() {
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(AsyncPlayerChatEvent p_event) {
		if (!p_event.isCancelled() && p_event.getPlayer().hasMetadata("ZeltCmds_Player_Mute")) {
			p_event.setCancelled(true);
		}
	}
}