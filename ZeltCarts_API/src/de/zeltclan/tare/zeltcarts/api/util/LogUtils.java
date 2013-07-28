package de.zeltclan.tare.zeltcarts.api.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

/**
 * Static class to log messages to the console of Bukkit
 * @author Mario
 *
 */
public class LogUtils {

	private static final Logger log = Logger.getLogger("Minecraft");

	/**
	 * Create message with prefix of JavaPlugin and send it to method without prefix
	 * @param p_plugin - JavaPlugin to get prefix from
	 * @param p_e - Exception to send to Logger
	 */
	public static void severe(Plugin p_plugin, Exception p_e) {
		LogUtils.severe("[" + p_plugin.getDescription().getName() + "] " + "Exception occured: ", p_e);
	}

	/**
	 * Create message with prefix of JavaPlugin and send it to method without prefix
	 * @param p_plugin - JavaPlugin to get prefix from
	 * @param p_msg - String to send to Logger
	 */
	public static void severe(Plugin p_plugin, String p_msg) {
		LogUtils.severe("[" + p_plugin.getDescription().getName() + "] " + p_msg);
	}

	/**
	 * Logs an exception with level Level.SEVERE
	 * @param p_msg - String to send to Logger
	 */
	public static void severe(String p_msg) {
		log.log(Level.SEVERE, p_msg);
	}

	/**
	 * Logs an exception with level Level.SEVERE
	 * @param p_msg - String to send to Logger
	 * @param p_e - Exception to send to Logger
	 */
	public static void severe(String p_msg, Exception p_e) {
		log.log(Level.SEVERE, p_msg, p_e);
	}

	/**
	 * Create message with prefix of JavaPlugin and send it to method without prefix
	 * @param p_plugin - JavaPlugin to get prefix from
	 * @param p_msg - String to send to Logger
	 */
	public static void warning(Plugin p_plugin, String p_msg) {
		LogUtils.warning("[" + p_plugin.getDescription().getName() + "] " + p_msg);
	}

	/**
	 * Logs an exception with level Level.WARNING
	 * @param p_msg - String to send to Logger
	 */
	public static void warning(String p_msg) {
		log.log(Level.WARNING, p_msg);
	}
}
