package de.zeltclan.tare.zeltcmds;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.permissions.PermissionDefault;

import de.zeltclan.tare.zeltcmds.cmds.CmdGive;
import de.zeltclan.tare.zeltcmds.cmds.CmdPlayerInfo;
import de.zeltclan.tare.zeltcmds.cmds.CmdLocation;
import de.zeltclan.tare.zeltcmds.cmds.CmdMode;
import de.zeltclan.tare.zeltcmds.cmds.CmdPlayer;
import de.zeltclan.tare.zeltcmds.cmds.CmdPlayerToggle;
import de.zeltclan.tare.zeltcmds.cmds.CmdServerInfo;
import de.zeltclan.tare.zeltcmds.cmds.CmdServerWeather;
import de.zeltclan.tare.zeltcmds.cmds.CmdSpawn;
import de.zeltclan.tare.zeltcmds.cmds.CmdWorldWeather;
import de.zeltclan.tare.zeltcmds.enums.*;

class CmdChooser {
	
	private final static TreeMap<String,Category> categoryMap;
	private final static HashMap<Category,TreeMap<String,Type>> typeMap;
	
	static {
		categoryMap = new TreeMap<String,Category>();
		categoryMap.put("give" ,Category.GIVE);
		categoryMap.put("mode" ,Category.MODE);
		categoryMap.put("player" ,Category.PLAYER);
		categoryMap.put("teleport" ,Category.PORT);
		categoryMap.put("port" ,Category.PORT);
		categoryMap.put("serverinfo" ,Category.SERVERINFO);
		categoryMap.put("servertime" ,Category.SERVERTIME);
		categoryMap.put("stime" ,Category.SERVERTIME);
		categoryMap.put("serverweather" ,Category.SERVERWEATHER);
		categoryMap.put("sweather" ,Category.SERVERWEATHER);
		categoryMap.put("set" ,Category.SET);
		categoryMap.put("spawn" ,Category.SPAWN);
		categoryMap.put("sudo" ,Category.SUDO);
		categoryMap.put("worldtime" ,Category.WORLDTIME);
		categoryMap.put("wtime" ,Category.WORLDTIME);
		categoryMap.put("time" ,Category.WORLDTIME);
		categoryMap.put("worldweather" ,Category.WORLDWEATHER);
		categoryMap.put("wweather" ,Category.WORLDWEATHER);
		categoryMap.put("weather" ,Category.WORLDWEATHER);
		typeMap = new HashMap<Category,TreeMap<String,Type>>();
		// Category.GIVE
		final TreeMap<String,Type> giveMap = new TreeMap<String,Type>();
		giveMap.put("item", CmdGive.Types.ITEM);
		giveMap.put("stack", CmdGive.Types.STACK);
		giveMap.put("chest", CmdGive.Types.CHEST);
		typeMap.put(Category.GIVE, giveMap);
		// Category.MODE
		final TreeMap<String,Type> modeMap = new TreeMap<String,Type>();
		modeMap.put("adventure", CmdMode.Types.ADVENTURE);
		modeMap.put("a", CmdMode.Types.ADVENTURE);
		modeMap.put("adventure_creative", CmdMode.Types.ADVENTURE_CREATIVE);
		modeMap.put("ac", CmdMode.Types.ADVENTURE_CREATIVE);
		modeMap.put("adventure_survival", CmdMode.Types.ADVENTURE_SURVIVAL);
		modeMap.put("as", CmdMode.Types.ADVENTURE_SURVIVAL);
		modeMap.put("adventure_creative_survival", CmdMode.Types.ADVENTURE_CREATIVE_SURVIVAL);
		modeMap.put("acs", CmdMode.Types.ADVENTURE_CREATIVE_SURVIVAL);
		modeMap.put("adventure_survival_creative", CmdMode.Types.ADVENTURE_SURVIVAL_CREATIVE);
		modeMap.put("asc", CmdMode.Types.ADVENTURE_SURVIVAL_CREATIVE);
		modeMap.put("creative", CmdMode.Types.CREATIVE);
		modeMap.put("c", CmdMode.Types.CREATIVE);
		modeMap.put("creative_adventure", CmdMode.Types.CREATIVE_ADVENTURE);
		modeMap.put("ca", CmdMode.Types.CREATIVE_ADVENTURE);
		modeMap.put("creative_survival", CmdMode.Types.CREATIVE_SURVIVAL);
		modeMap.put("cs", CmdMode.Types.CREATIVE_SURVIVAL);
		modeMap.put("creative_adventure_survival", CmdMode.Types.CREATIVE_ADVENTURE_SURVIVAL);
		modeMap.put("cas", CmdMode.Types.CREATIVE_ADVENTURE_SURVIVAL);
		modeMap.put("creative_survival_adventure", CmdMode.Types.CREATIVE_SURVIVAL_ADVENTURE);
		modeMap.put("csa", CmdMode.Types.CREATIVE_SURVIVAL_ADVENTURE);
		modeMap.put("survival", CmdMode.Types.SURVIVAL);
		modeMap.put("s", CmdMode.Types.SURVIVAL);
		modeMap.put("survival_adventure", CmdMode.Types.SURVIVAL_ADVENTURE);
		modeMap.put("sa", CmdMode.Types.SURVIVAL_ADVENTURE);
		modeMap.put("survival_creative", CmdMode.Types.SURVIVAL_CREATIVE);
		modeMap.put("sc", CmdMode.Types.SURVIVAL_CREATIVE);
		modeMap.put("survival_adventure_creative", CmdMode.Types.SURVIVAL_ADVENTURE_CREATIVE);
		modeMap.put("sac", CmdMode.Types.SURVIVAL_ADVENTURE_CREATIVE);
		modeMap.put("survival_creative_adventure", CmdMode.Types.SURVIVAL_CREATIVE_ADVENTURE);
		modeMap.put("sca", CmdMode.Types.SURVIVAL_CREATIVE_ADVENTURE);
		typeMap.put(Category.MODE, modeMap);
		// Category.PLAYER
		final TreeMap<String,Type> playerMap = new TreeMap<String,Type>();
		playerMap.put("alwaysfly", CmdPlayerToggle.Types.ALWAYSFLY);
		playerMap.put("build", CmdPlayerToggle.Types.BUILD);
		playerMap.put("cleanchat", CmdPlayer.Types.CLEANCHAT);
		playerMap.put("clean", CmdPlayer.Types.CLEANCHAT);
		playerMap.put("cc", CmdPlayer.Types.CLEANCHAT);
		playerMap.put("clearinventory", CmdPlayer.Types.CLEARINVENTORY);
		playerMap.put("clear", CmdPlayer.Types.CLEARINVENTORY);
		playerMap.put("ci", CmdPlayer.Types.CLEARINVENTORY);
		playerMap.put("direction", CmdPlayerInfo.Types.DIRECTION);
		playerMap.put("dir", CmdPlayerInfo.Types.DIRECTION);
		playerMap.put("enchanting", CmdPlayer.Types.ENCHANTING);
		playerMap.put("enchantment", CmdPlayer.Types.ENCHANTING);
		playerMap.put("enderchest", CmdPlayer.Types.ENDERCHEST);
		playerMap.put("feed", CmdPlayer.Types.FEED);
		playerMap.put("fly", CmdPlayerToggle.Types.FLY);
		playerMap.put("freeze", CmdPlayerToggle.Types.FREEZE);
		playerMap.put("heal", CmdPlayer.Types.HEAL);
		playerMap.put("info", CmdPlayerInfo.Types.INFO);
		playerMap.put("ip", CmdPlayerInfo.Types.IP);
		playerMap.put("item", CmdPlayerInfo.Types.ITEM);
		playerMap.put("kill", CmdPlayer.Types.KILL);
		playerMap.put("leveldown", CmdPlayer.Types.LEVELDOWN);
		playerMap.put("lvl-", CmdPlayer.Types.LEVELDOWN);
		playerMap.put("levelreset", CmdPlayer.Types.LEVELRESET);
		playerMap.put("lvl0", CmdPlayer.Types.LEVELRESET);
		playerMap.put("levelup", CmdPlayer.Types.LEVELUP);
		playerMap.put("lvl+", CmdPlayer.Types.LEVELUP);
		playerMap.put("mute", CmdPlayerToggle.Types.MUTE);
		playerMap.put("position", CmdPlayerInfo.Types.POSITION);
		playerMap.put("pos", CmdPlayerInfo.Types.POSITION);
		playerMap.put("seen", CmdPlayerInfo.Types.SEEN);
		playerMap.put("starve", CmdPlayer.Types.STARVE);
		playerMap.put("time", CmdPlayerInfo.Types.TIME);
		playerMap.put("workbench", CmdPlayer.Types.WORKBENCH);
		typeMap.put(Category.PLAYER, playerMap);
		// Category.PORT
		final TreeMap<String,Type> portMap = new TreeMap<String,Type>();
		portMap.put("a2m", Port.A2M);
		portMap.put("a2p", Port.A2P);
		portMap.put("a2w", Port.A2W);
		portMap.put("m2b", Port.M2B);
		portMap.put("m2c", Port.M2C);
		portMap.put("m2d", Port.M2D);
		portMap.put("death", Port.M2D);
		portMap.put("m2e", Port.M2E);
		portMap.put("m2h", Port.M2H);
		portMap.put("m2l", Port.M2L);
		portMap.put("back", Port.M2L);
		portMap.put("m2p", Port.M2P);
		portMap.put("m2s", Port.M2S);
		portMap.put("m2w", Port.M2W);
		portMap.put("m2wc", Port.M2WC);
		portMap.put("m2we", Port.M2WE);
		portMap.put("mod", Port.MoD);
		portMap.put("blink", Port.MoD);
		portMap.put("p2b", Port.P2B);
		portMap.put("p2c", Port.P2C);
		portMap.put("p2d", Port.P2D);
		portMap.put("p2e", Port.P2E);
		portMap.put("p2h", Port.P2H);
		portMap.put("p2l", Port.P2L);
		portMap.put("p2m", Port.P2M);
		portMap.put("p2p", Port.P2P);
		portMap.put("p2s", Port.P2S);
		portMap.put("p2w", Port.P2W);
		portMap.put("p2wc", Port.P2WC);
		portMap.put("p2we", Port.P2WE);
		portMap.put("w2m", Port.W2M);
		portMap.put("w2p", Port.W2P);
		portMap.put("w2s", Port.W2S);
		portMap.put("w2w", Port.W2W);
		typeMap.put(Category.PORT, portMap);
		// Category.SERVERINFO
		final TreeMap<String,Type> serverinfoMap = new TreeMap<String,Type>();
		serverinfoMap.put("blacklist", CmdServerInfo.Types.BLACKLIST);
		serverinfoMap.put("banlist", CmdServerInfo.Types.BLACKLIST);
		serverinfoMap.put("info", CmdServerInfo.Types.INFO);
		serverinfoMap.put("information", CmdServerInfo.Types.INFO);
		serverinfoMap.put("onlinelist", CmdServerInfo.Types.ONLINELIST);
		serverinfoMap.put("online", CmdServerInfo.Types.ONLINELIST);
		serverinfoMap.put("oplist", CmdServerInfo.Types.OPLIST);
		serverinfoMap.put("ops", CmdServerInfo.Types.OPLIST);
		serverinfoMap.put("ram", CmdServerInfo.Types.RAM);
		serverinfoMap.put("whitelist", CmdServerInfo.Types.WHITELIST);
		serverinfoMap.put("worldlist", CmdServerInfo.Types.WORLDLIST);
		serverinfoMap.put("worlds", CmdServerInfo.Types.WORLDLIST);
		typeMap.put(Category.SERVERINFO, serverinfoMap);
		// Category.SERVERTIME
		typeMap.put(Category.SERVERTIME, null);
		// Category.SERVERWEATHER
		final TreeMap<String,Type> serverweatherMap = new TreeMap<String,Type>();
		serverweatherMap.put("rain", CmdServerWeather.Types.RAIN);
		serverweatherMap.put("storm", CmdServerWeather.Types.STORM);
		serverweatherMap.put("sun", CmdServerWeather.Types.SUN);
		typeMap.put(Category.SERVERWEATHER, serverweatherMap);
		// Category.SET
		final TreeMap<String,Type> setMap = new TreeMap<String,Type>();
		setMap.put("bedspawn", CmdLocation.Types.BEDSPAWN);
		setMap.put("home", CmdLocation.Types.BEDSPAWN);
		setMap.put("spawn", CmdLocation.Types.SPAWN);
		typeMap.put(Category.SET, setMap);
		// Category.SPAWN
		final TreeMap<String,Type> spawnMap = new TreeMap<String,Type>();
		spawnMap.put("cursor", CmdSpawn.Types.CURSOR);
		spawnMap.put("player", CmdSpawn.Types.PLAYER);
		typeMap.put(Category.SPAWN, spawnMap);
		// Category.SUDO
		final TreeMap<String,Type> sudoMap = new TreeMap<String,Type>();
		sudoMap.put("all", Sudo.ALL);
		sudoMap.put("console", Sudo.CONSOLE);
		sudoMap.put("player", Sudo.PLAYER);
		sudoMap.put("world", Sudo.WORLD);
		typeMap.put(Category.SUDO, sudoMap);
		// Category.WORLDTIME
		typeMap.put(Category.WORLDTIME, null);
		// Category.WORLDWEATHER
		final TreeMap<String,Type> worldweatherMap = new TreeMap<String,Type>();
		worldweatherMap.put("rain", CmdWorldWeather.Types.RAIN);
		worldweatherMap.put("storm", CmdWorldWeather.Types.STORM);
		worldweatherMap.put("sun", CmdWorldWeather.Types.SUN);
		typeMap.put(Category.WORLDWEATHER, worldweatherMap);
	}
	
	private CmdChooser() {
		
	}
	
	static PermissionDefault getDefaultPermission(String p_perm) {
		PermissionDefault result = PermissionDefault.getByName(p_perm);
		return (result != null ? result : PermissionDefault.FALSE);
	}
	
	static Set<String> listCategories() {
		return categoryMap.keySet();
	}
	
	static Set<String> listTypes(Category p_category) {
		final TreeMap<String,Type> types = typeMap.get(p_category);
		if (types != null) {
			return types.keySet();
		}
		return null;
	}
	
	static Category getCategory(String p_category) {
		if (categoryMap.containsKey(p_category.toLowerCase())) {
			return categoryMap.get(p_category.toLowerCase());
		}
		return Category.NOCMD;
	}
	
	static Type getType(Category p_category, String p_type) {
		if (typeMap.containsKey(p_category)) {
			final TreeMap<String,Type> types = typeMap.get(p_category);
			if (types != null) {
				if (types.containsKey(p_type.toLowerCase())) {
					return types.get(p_type.toLowerCase());
				}
			} else {
				return Default.NOCHANGE;
			}
		}
		return Default.NOTYPE;
	}
	
	static MessageType getMessageType(Category p_category, Type p_type) {
		switch (p_category) {
		case PLAYER:
			if (p_type instanceof CmdPlayerInfo.Types) {
				return MessageType.NOMESSAGE;
			} else {
				return MessageType.MESSAGE;
			}
		case SERVERINFO:
			return MessageType.NOMESSAGE;
		case SUDO:
			if (p_type.equals(Sudo.CONSOLE)) {
				return MessageType.NOMESSAGE;
			} else {
				return MessageType.MESSAGE;
			}
		default:
			return MessageType.MESSAGE;
		}
	}
	
	static PermissionExtension getPermissionExtension(Category p_category, Type p_type) {
		switch (p_category) {
		case GIVE:
			return PermissionExtension.PLAYER;
		case MODE:
			return PermissionExtension.PLAYER;
		case PLAYER:
			if (p_type instanceof CmdPlayer.Types || p_type instanceof CmdPlayerToggle.Types) {
				return PermissionExtension.PLAYER;
			} else {
				return PermissionExtension.NOEXTENSION;
			}
		case SET:
			if (p_type instanceof CmdLocation.Types && p_type == CmdLocation.Types.BEDSPAWN) {
				return PermissionExtension.PLAYER;
			} else {
				return PermissionExtension.NOEXTENSION;
			}
		case SPAWN:
			if (p_type instanceof CmdSpawn.Types && p_type == CmdSpawn.Types.PLAYER) {
				return PermissionExtension.PLAYER;
			} else {
				return PermissionExtension.NOEXTENSION;
			}
		case WORLDTIME:
			return PermissionExtension.WORLD;
		case WORLDWEATHER:
			return PermissionExtension.WORLD;
		default:
			return PermissionExtension.NOEXTENSION;
		}
	}
	
	static RequireListener getListener(Category p_category, Type p_type) {
		switch (p_category) {
		case PLAYER:
			if (p_type instanceof CmdPlayerToggle.Types) {
				switch ((CmdPlayerToggle.Types) p_type) {
				case ALWAYSFLY:
					return RequireListener.ALWAYSFLY;
				case BUILD:
					return RequireListener.BUILD;
				case FREEZE:
					return RequireListener.FREEZE;
				case MUTE:
					return RequireListener.MUTE;
				default:
					return RequireListener.NONE;
				}
			} else {
				return RequireListener.NONE;
			}
		case PORT:
			if (p_type instanceof Port) {
				switch ((Port) p_type) {
				case M2D:
				case P2D:
					return RequireListener.DEATH;
				case M2L:
				case P2L:
					return RequireListener.PORT;
				default:
					return RequireListener.NONE;
				}
			}
		default:
			return RequireListener.NONE;
		}
	}
}
