package de.zeltclan.tare.zeltcmds.cmds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdWorldInfo extends CmdParent {

	public static enum Types implements Type {
		ENTITY, GAMERULE, INFO, LIVINGENTITY, WEATHER;
	}
	private final Types type;
	
	public CmdWorldInfo(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener) {
		super(ZeltCmds.getLanguage().getString("description_worldinfo_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
	}

	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_not_enough"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		case 1:
			for (String msg : this.getInformation(p_args[0])) {
				this.getPlugin().getLogger().info(msg);
			}
			break;
		default:
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("arguments_too_many"));
			this.getPlugin().getLogger().warning(ZeltCmds.getLanguage().getString("usage_World", new Object[] {p_cmd}));
			break;
		}
	}

	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
			case 0:
				if (this.checkPerm(p_player, false)) {
					for (String msg : this.getInformation(p_player.getWorld().getName())) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_worldinfo", new Object[] {type.name(), p_player.getName(), p_player.getWorld().getName()});
				}
				break;
			case 1:
				if (this.checkPerm(p_player, true)) {
					for (String msg : this.getInformation(p_args[0])) {
						p_player.sendMessage(ChatColor.GREEN + msg);
					}
					return ZeltCmds.getLanguage().getString("log_worldinfo", new Object[] {type.name(), p_player.getName(), p_args[0]});
				}
				break;
			default:
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
				p_player.sendMessage(ChatColor.RED + "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_world", new Object[] {"/" + p_cmd}));
				break;
		}
		return null;
	}
	
	private List<String> getInformation (String p_world) {
		List<String> result = new ArrayList<String>();
		World world = this.getPlugin().getServer().getWorld(p_world);
		if (world == null) {
			result.add(ZeltCmds.getLanguage().getString("world_not_found", new Object[] {p_world}));
			return result;
		}
		switch (type) {
		case ENTITY:
			TreeMap<String, Integer> map_entity = new TreeMap<String, Integer>();
			for (Entity entity : world.getEntities()) {
				if (map_entity.containsKey(entity.getType().name())) {
					map_entity.put(entity.getType().name(), (map_entity.get(entity.getType().name()) + 1));
				} else {
					map_entity.put(entity.getType().name(), 1);
				}
			}
			Entry<String, Integer> entityEntry;
			int sum = 0;
			while ((entityEntry = map_entity.pollFirstEntry()) != null) {
				sum += entityEntry.getValue();
				result.add(ZeltCmds.getLanguage().getString("worldinfo_entity_entry", new Object[] {entityEntry.getKey(), entityEntry.getValue()}));
			}
			result.add(0, ZeltCmds.getLanguage().getString("worldinfo_entity", new Object[] {sum}));
			break;
		case GAMERULE:
			result.add(ZeltCmds.getLanguage().getString("worldinfo_gamerule"));
			for (String rule : world.getGameRules()) {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_gamerule_entry", new Object[] {rule, world.getGameRuleValue(rule)}));
			}
			break;
		case INFO:
			result.add(ZeltCmds.getLanguage().getString("worldinfo_name", new Object[] {world.getName()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_loadedChunks", new Object[] {world.getLoadedChunks().length}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_worldType", new Object[] {world.getWorldType().getName()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_environment", new Object[] {world.getEnvironment().name()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_seed", new Object[] {String.valueOf(world.getSeed())}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_maxHeight", new Object[] {world.getMaxHeight()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_seaLevel", new Object[] {world.getSeaLevel()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_spawn", new Object[] {world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), world.getKeepSpawnInMemory()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_difficulty", new Object[] {world.getDifficulty().name()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_pvp", new Object[] {world.getPVP()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_generateStructures", new Object[] {world.canGenerateStructures()}));
			if (world.getAllowAnimals()) {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_animals_spawn", new Object[] {world.getAnimalSpawnLimit(), world.getTicksPerAnimalSpawns()}));
			} else {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_animals_no"));
			}
			if (world.getAllowMonsters()) {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_monsters_spawn", new Object[] {world.getMonsterSpawnLimit(), world.getTicksPerMonsterSpawns()}));
			} else {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_monsters_no"));
			}
			result.add(ZeltCmds.getLanguage().getString("worldinfo_ambient_limit", new Object[] {world.getAmbientSpawnLimit()}));
			result.add(ZeltCmds.getLanguage().getString("worldinfo_water_limit", new Object[] {world.getWaterAnimalSpawnLimit()}));
			break;
		case LIVINGENTITY:
			TreeMap<String, Integer> map_livingEntity = new TreeMap<String, Integer>();
			for (Entity entity : world.getLivingEntities()) {
				if (map_livingEntity.containsKey(entity.getType().name())) {
					map_livingEntity.put(entity.getType().name(), (map_livingEntity.get(entity.getType().name()) + 1));
				} else {
					map_livingEntity.put(entity.getType().name(), 1);
				}
			}
			Entry<String, Integer> livingEntityEntry;
			int sumLiving = 0;
			while ((livingEntityEntry = map_livingEntity.pollFirstEntry()) != null) {
				sumLiving += livingEntityEntry.getValue();
				result.add(ZeltCmds.getLanguage().getString("worldinfo_livingEntity_entry", new Object[] {livingEntityEntry.getKey(), livingEntityEntry.getValue()}));
			}
			result.add(0, ZeltCmds.getLanguage().getString("worldinfo_livingEntity", new Object[] {sumLiving}));
			break;
		case WEATHER:
			if (world.hasStorm()) {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_weather_storm"));
			} else {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_weather_sun"));
			}
			if (world.isThundering()) {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_weather_thunder"));
			} else {
				result.add(ZeltCmds.getLanguage().getString("worldinfo_weather_no_thunder"));
			}
			int duration = world.getWeatherDuration() / 20;
			String minute = Integer.toString(duration / 60);
			String second = Integer.toString(duration % 60);
			result.add(ZeltCmds.getLanguage().getString("worldinfo_weather_duration", new Object[] {minute, second.length() < 2 ? "0" + second : second}));
			break;
		}
		return result;
	}
}
