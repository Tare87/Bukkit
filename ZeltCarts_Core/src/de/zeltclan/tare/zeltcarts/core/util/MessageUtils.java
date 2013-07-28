package de.zeltclan.tare.zeltcarts.core.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Static class to send messages to the Bukkitserver
 * @author Mario
 *
 */
public class MessageUtils {

	/**
	 * Sends a info message to a Player if online with color ChatColor.GREEN
	 * @param p_player - Player that gets the message
	 * @param p_msg - String to send
	 */
	public static void info(Player p_player, String p_msg) {
		if (p_player.isOnline()) {
			p_player.sendMessage(ChatColor.GREEN + p_msg);
		}
	}

}
