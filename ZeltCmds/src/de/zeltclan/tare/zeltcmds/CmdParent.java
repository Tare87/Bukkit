package de.zeltclan.tare.zeltcmds;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;

public abstract class CmdParent {

	private final String description;
	
	private final Permission normalPermission;
	private final Permission extendedPermission;
	
	protected CmdParent(String p_description, Permission p_perm, Permission p_permExtend) {
		description = p_description + " (" + p_perm.getDefault().name() + ")";
		
		normalPermission = p_perm;
		extendedPermission = p_permExtend;
		Bukkit.getServer().getPluginManager().addPermission(normalPermission);
		if (extendedPermission != null) {
			Bukkit.getServer().getPluginManager().addPermission(extendedPermission);
		}
	}
	
	protected abstract void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args);
	
	protected abstract String executePlayer(Player p_player, String p_cmd, String[] p_args);
	
	protected boolean checkPerm(Player p_sender, Boolean p_extend) {
		if (p_sender.hasPermission(p_extend  ? extendedPermission : normalPermission)) {
			return true;
		}
		// Send message to CommandSender that permission is denied
		MessageUtils.warning(p_sender, ZeltCmds.getLanguage().getString("permission_deny"));
		return false;
	}
	
	protected String getDescription() {
		return description;
	}
	
	protected void removePermissions() {
		Bukkit.getServer().getPluginManager().removePermission(normalPermission);
		if (extendedPermission != null) {
			Bukkit.getServer().getPluginManager().removePermission(extendedPermission);
		}
	}
}
