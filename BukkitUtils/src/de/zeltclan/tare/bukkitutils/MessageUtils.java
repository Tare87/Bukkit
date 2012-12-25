package de.zeltclan.tare.bukkitutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Static class to send messages to the Bukkitserver
 * @author Mario
 *
 */
public class MessageUtils {
	
	/**
	 * Sends a message to all players online on the Bukkitserver with color ChatColor.YELLOW
	 * @param p_msg - String to send
	 */
	public static void broadcast(String p_msg) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.YELLOW + p_msg);
		}
	}
	
	/**
	 * Sends a message to all players online on a given world with color ChatColor.YELLOW
	 * @param p_world - World receivers are on
	 * @param p_msg - String to send
	 */
	public static void broadcast(World p_world, String p_msg) {
		for (Player player : p_world.getPlayers()) {
			if (player.isOnline()) {
				player.sendMessage(ChatColor.YELLOW + p_msg);
			}
		}
	}
	
	/**
	 * Sends a simple message to a CommandSender
	 * @param p_sender - CommandSender that gets the message
	 * @param p_msg - String to send
	 */
	public static void msg(CommandSender p_sender, String p_msg) {
		p_sender.sendMessage(p_msg);
	}
	
	/**
	 * Sends a simple message to a Player if online
	 * @param p_player - Player that gets the message
	 * @param p_msg - String to send
	 */
	public static void msg(Player p_player, String p_msg) {
		if (p_player.isOnline()) {
			p_player.sendMessage(p_msg);
		}
	}
	
	/**
	 * Sends a info message to a CommandSender with color ChatColor.GREEN
	 * @param p_sender - CommandSender that gets the message
	 * @param p_msg - String to send
	 */
	public static void info(CommandSender p_sender, String p_msg) {
		p_sender.sendMessage(ChatColor.GREEN + p_msg);
	}
	
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
	
	/**
	 * Sends a warning message to a CommandSender with color ChatColor.RED
	 * @param p_sender - CommandSender that gets the message
	 * @param p_msg - String to send
	 */
	public static void warning(CommandSender p_sender, String p_msg) {
		p_sender.sendMessage(ChatColor.RED + p_msg);
	}
	
	/**
	 * Sends a warning message to a Player if online with color ChatColor.RED
	 * @param p_player - Player that gets the message
	 * @param p_msg - String to send
	 */
	public static void warning(Player p_player, String p_msg) {
		if (p_player.isOnline()) {
			p_player.sendMessage(ChatColor.RED + p_msg);
		}
	}
	
	/**
	 * Sends a message to all players online that are Ops on the Bukkitserver with color ChatColor.GOLD
	 * @param p_msg - String to send
	 */
	public static void sendToOps(String p_msg) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.isOp()) {
				player.sendMessage(ChatColor.GOLD + p_msg);
			}
		}
	}
}
