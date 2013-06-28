package de.zeltclan.tare.zeltcmds;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;

public abstract class CmdParent {

	private final Plugin plugin;
	
	private final String description;
	
	private final Permission normalPermission;
	private final Permission extendedPermission;
	
	private final RequireListener requireListener;
	
	protected CmdParent(String p_description, Permission p_perm, Permission p_permExtend, RequireListener p_listener) {
		plugin = Bukkit.getServer().getPluginManager().getPlugin("ZeltCmds");
		
		description = p_description + " (" + p_perm.getDefault().name() + ")";
		
		normalPermission = p_perm;
		extendedPermission = p_permExtend;
		Bukkit.getServer().getPluginManager().addPermission(normalPermission);
		if (extendedPermission != null) {
			Bukkit.getServer().getPluginManager().addPermission(extendedPermission);
		}
		
		requireListener = p_listener;
	}
	
	protected abstract void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args);
	
	protected abstract String executePlayer(Player p_player, String p_cmd, String[] p_args);
	
	protected Plugin getPlugin() {
		return plugin;
	}
	
	protected String getDescription() {
		return description;
	}
	
	protected boolean checkPerm(Player p_sender, Boolean p_extend) {
		if (p_sender.hasPermission(p_extend  ? extendedPermission : normalPermission)) {
			return true;
		}
		// Send message to CommandSender that permission is denied
		MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("permission_deny"));
		return false;
	}
	
	protected void removePermissions() {
		Bukkit.getServer().getPluginManager().removePermission(normalPermission);
		if (extendedPermission != null) {
			Bukkit.getServer().getPluginManager().removePermission(extendedPermission);
		}
	}
	
	protected RequireListener getListener() {
		return requireListener;
	}
}
