package de.zeltclan.tare.zeltcmds;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import de.zeltclan.tare.zeltcmds.cmds.*;
import de.zeltclan.tare.zeltcmds.enums.Category;
import de.zeltclan.tare.zeltcmds.enums.Default;
import de.zeltclan.tare.zeltcmds.enums.PermissionExtension;
import de.zeltclan.tare.zeltcmds.enums.Port;
import de.zeltclan.tare.zeltcmds.enums.Type;

public class CmdBuilder {

	private CmdBuilder() {
		
	}
	
	protected static CmdParent build(String p_cmd, Category p_category, Type p_type, PermissionDefault p_dperm, String p_msg) {
		Permission permission = new Permission("zeltcmds.cmd." + p_cmd, ZeltCmds.getLanguage().getString("permission_description_type", new Object[] {p_category.name(), p_type.name()}), p_dperm);
		PermissionExtension extension = CmdChooser.getPermissionExtension(p_category, p_type);
		Permission permissionExtended;
		if (extension == PermissionExtension.NOEXTENSION) {
			permissionExtended = null;
		} else {
			permissionExtended = new Permission("zeltcmds.cmd." + p_cmd + "." + extension.name().toLowerCase(), ZeltCmds.getLanguage().getString("permission_description_type_extended", new Object[] {p_category.name(), p_type.name(), extension.name()}), p_dperm);
		}
		switch (p_category) {
		case MODE:
			return new CmdMode(((CmdMode.Types)p_type), permission, permissionExtended, p_msg);
		case PLAYER:
			if (p_type instanceof CmdPlayerInfo.Types) {
				return new CmdPlayerInfo(((CmdPlayerInfo.Types)p_type), permission, permissionExtended);
			} else if (p_type instanceof CmdPlayer.Types) {
				return new CmdPlayer(((CmdPlayer.Types)p_type), permission, permissionExtended, p_msg);
			}
			break;
		case PORT:
			switch ((Port)p_type) {
			case A2M:
				return new CmdPortA2M(permission, permissionExtended, p_msg);
			case A2P:
				return new CmdPortA2P(permission, permissionExtended, p_msg);
			case A2W:
				return new CmdPortA2W(permission, permissionExtended, p_msg);
			case M2B:
				return new CmdPortM2B(permission, permissionExtended, p_msg);
			case M2C:
				return new CmdPortM2C(permission, permissionExtended, p_msg);
			case M2E:
				return new CmdPortM2E(permission, permissionExtended, p_msg);
			case M2H:
				return new CmdPortM2H(permission, permissionExtended, p_msg);
			case M2P:
				return new CmdPortM2P(permission, permissionExtended, p_msg);
			case M2S:
				return new CmdPortM2S(permission, permissionExtended, p_msg);
			case M2W:
				return new CmdPortM2W(permission, permissionExtended, p_msg);
			case M2WC:
				return new CmdPortM2WC(permission, permissionExtended, p_msg);
			case M2WE:
				return new CmdPortM2WE(permission, permissionExtended, p_msg);
			case P2B:
				return new CmdPortP2B(permission, permissionExtended, p_msg);
			case P2C:
				return new CmdPortP2C(permission, permissionExtended, p_msg);
			case P2E:
				return new CmdPortP2E(permission, permissionExtended, p_msg);
			case P2H:
				return new CmdPortP2H(permission, permissionExtended, p_msg);
			case P2M:
				return new CmdPortP2M(permission, permissionExtended, p_msg);
			case P2P:
				return new CmdPortP2P(permission, permissionExtended, p_msg);
			case P2S:
				return new CmdPortP2S(permission, permissionExtended, p_msg);
			case P2W:
				return new CmdPortP2W(permission, permissionExtended, p_msg);
			case P2WC:
				return new CmdPortP2WC(permission, permissionExtended, p_msg);
			case P2WE:
				return new CmdPortP2WE(permission, permissionExtended, p_msg);
			case W2M:
				return new CmdPortW2M(permission, permissionExtended, p_msg);
			case W2P:
				return new CmdPortW2P(permission, permissionExtended, p_msg);
			case W2S:
				return new CmdPortW2S(permission, permissionExtended, p_msg);
			case W2W:
				return new CmdPortW2W(permission, permissionExtended, p_msg);
			}
			break;
		case SERVERINFO:
			return new CmdServerInfo(((CmdServerInfo.Types)p_type), permission, permissionExtended);
		case SERVERWEATHER:
			return new CmdServerWeather(((CmdServerWeather.Types)p_type), permission, permissionExtended, p_msg);
		case SET:
			if (p_type instanceof CmdLocation.Types) {
				return new CmdLocation(((CmdLocation.Types)p_type), permission, permissionExtended, p_msg);
			}
			break;
		case SPAWN:
			if (p_type instanceof CmdSpawn.Types) {
				return new CmdSpawn(((CmdSpawn.Types)p_type), permission, permissionExtended, p_msg);
			}
			break;
		case WORLDWEATHER:
			return new CmdWorldWeather(((CmdWorldWeather.Types)p_type), permission, permissionExtended, p_msg);
		default:
			break;
		}
		return null;
	}
	
	protected static CmdParent build(String p_cmd, Category p_category, String p_type, PermissionDefault p_dperm, String p_msg) {
		Permission permission = new Permission("zeltcmds.cmd." + p_cmd, ZeltCmds.getLanguage().getString("permission_description_param", new Object[] {p_category.name(), p_type}), p_dperm);
		PermissionExtension extension = CmdChooser.getPermissionExtension(p_category, Default.NOCHANGE);
		Permission permissionExtended;
		if (extension == PermissionExtension.NOEXTENSION) {
			permissionExtended = null;
		} else {
			permissionExtended = new Permission("zeltcmds.cmd." + p_cmd + extension.name().toLowerCase(), ZeltCmds.getLanguage().getString("permission_description_param_extended", new Object[] {p_category.name(), p_type, extension.name()}), p_dperm);
		}
		switch (p_category) {
		case SERVERTIME:
			return new CmdServerTime(Long.parseLong(p_type), permission, permissionExtended, p_msg);
		case WORLDTIME:
			return new CmdWorldTime(Long.parseLong(p_type), permission, permissionExtended, p_msg);
		default:
			return null;
		}
	}

}
