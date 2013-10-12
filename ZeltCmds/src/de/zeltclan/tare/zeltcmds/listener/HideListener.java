package de.zeltclan.tare.zeltcmds.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class HideListener implements Listener {
	
	public HideListener() {
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent p_event) {
		final Player player = p_event.getPlayer();
		for (Player other : player.getServer().getOnlinePlayers()) {
			if (!player.equals(other)) {
				if (player.hasMetadata("ZeltCmds_Player_Hide")) {
					other.hidePlayer(player);
				}
				if (other.hasMetadata("ZeltCmds_Player_Hide")) {
					player.hidePlayer(other);
				}
			}
		}
	}
}