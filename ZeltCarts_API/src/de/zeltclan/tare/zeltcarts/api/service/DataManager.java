package de.zeltclan.tare.zeltcarts.api.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.zeltclan.tare.zeltcarts.api.util.BlockUtils;
import de.zeltclan.tare.zeltcarts.api.util.LanguageUtils;
import de.zeltclan.tare.zeltcarts.api.util.LogUtils;

public class DataManager {
	
	/**
	 * Variables of the class
	 */
	
	private LanguageUtils lang;

	final private Plugin plugin;
	
	final private boolean broadcast;
	
	private FileConfiguration data = null;
	final private File dataFile;
	private boolean dataChanged = false;
	
	/**
	 * Constructor
	 * @param p_plugin - The constructing plugin aka root
	 * @param p_lang - Tag of language to use for localization
	 * @param p_intervall - Intervall in ticks for saving data
	 */

	public DataManager(Plugin p_plugin, String p_lang, long p_intervall, boolean p_broadcast) {
		lang = new LanguageUtils("ZeltCarts_API_localization", p_lang);
		plugin = p_plugin;
		broadcast = p_broadcast;
		if (plugin.getDataFolder().exists() || plugin.getDataFolder().mkdirs()) {
			dataFile = new File(plugin.getDataFolder(), "Data.yml");
			try {
				if (!dataFile.exists()) {
					dataFile.createNewFile();
				}
				data = YamlConfiguration.loadConfiguration(dataFile);
			} catch (Exception e) {
				LogUtils.severe(plugin, e);
				plugin.onDisable();
			}
		} else {
			dataFile = null;
			LogUtils.severe(plugin, lang.getString("create_datafolder_failed"));
			plugin.onDisable();
		}
		if (p_intervall > 0) {
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
				public void run() {
					save(plugin);
				}
			}, p_intervall, p_intervall);
		}
	}
	
	// --- Internal Methods --- //
	
	/**
	 * Returns the path of data for given plugin and location
	 * @param p_plugin - Plugin that owns the data
	 * @param p_loc - Location of the data
	 * @return String representing path for data
	 */
	
	private String getPathString(Plugin p_plugin, Location p_loc) {
		return p_plugin.getName() + "." + p_loc.getWorld().getName() + "." + p_loc.getBlockX() + "." + p_loc.getBlockZ() + "." + p_loc.getBlockY();
		
	}
	
	// --- General Public Methods --- //
	
	/**
	 * Returns the size of datalist for given plugin and block
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block from where the data is
	 * @return size of datalist
	 */
	
	public int getSize(Plugin p_plugin, Block p_rail) {
		return this.getBlock(p_plugin, p_rail).size();
	}
	
	/**
	 * Returns entry of given index from datalist for given plugin and block
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block from where the data is
	 * @param p_index - Needed index of datalist
	 * @return entry of datalist for valid index, else null
	 */
	
	public String getIndex(Plugin p_plugin, Block p_rail, int p_index) {
		if (p_index < 0) {
			return null;
		}
		final List<String> dataList = getBlock(p_plugin, p_rail);
		if ((dataList.size()-1) < p_index) {
			return null;
		}
		return dataList.get(p_index);
	}
	
	/**
	 * Returns datalist for given plugin and block
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block from where the data is
	 * @return datalist
	 */
	
	public List<String> getBlock(Plugin p_plugin, Block p_rail) {
		if (p_plugin == null || p_rail == null) {
			return new ArrayList<String>();
		}
		final String path = this.getPathString(p_plugin, p_rail.getLocation());
		List<String> dataList = data.getStringList(path);
		if (dataList == null) {
			return new ArrayList<String>();
		}
		return dataList;
	}
	
	/**
	 * Sets data for a given plugin and block. Existing data is overwritten
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to set the data
	 * @param p_data - datalist to set
	 */
	
	public void set(Plugin p_plugin, Block p_rail, List<String> p_data) {
		if (p_plugin == null || p_rail == null || p_data == null) {
			return;
		}
		if (p_data.isEmpty()) {
			data.set(this.getPathString(p_plugin, p_rail.getLocation()), null);
		} else {
			data.set(this.getPathString(p_plugin, p_rail.getLocation()), p_data);
		}
		dataChanged = true;
	}
	
	/**
	 * Sets a single entry of data for a given plugin and block. Exsting data is overwritten
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to set the data
	 * @param p_data - entry of data to set
	 */
	
	public void set(Plugin p_plugin, Block p_rail, String p_data) {
		List<String> dataList = new ArrayList<String>();
		dataList.add(p_data);
		this.set(p_plugin, p_rail, dataList);
	}
	
	/**
	 * Adds data for a given plugin and block. Data is added to the end of existing data
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to add the data
	 * @param p_data - datalist to add
	 */
	
	public void add(Plugin p_plugin, Block p_rail, List<String> p_data) {
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		dataList.addAll(p_data);
		this.set(p_plugin, p_rail, dataList);
	}
	
	/**
	 * Adds a single entry of data for a given plugin and block. Entry is added to the end of existing data
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to add the data
	 * @param p_data - entry of data to add
	 */
	
	public void add(Plugin p_plugin, Block p_rail, String p_data) {
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		dataList.add(p_data);
		this.set(p_plugin, p_rail, dataList);
	}
	
	/**
	 * Inserts data for a given plugin and block. Data is added after a given index of existing data. Does nothing for invalid index
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to insert the data
	 * @param p_data - datalist to insert
	 * @param p_index - index after which the datalist is added
	 */
	
	public void insert(Plugin p_plugin, Block p_rail, List<String> p_data, int p_index) {
		if (p_index < 0) {
			return;
		}
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		if ((dataList.size()-1) < p_index) {
			return;
		}
		dataList.addAll(p_index, p_data);
		this.set(p_plugin, p_rail, dataList);
	}
	
	/**
	 * Inserts a single entry of data for a given plugin and block. Entry is added after a given index of existing data. Does nothing for invalid index
	 * @param p_plugin - Plugin that owns the data
	 * @param p_rail - Block where to insert the data
	 * @param p_data - entry of data to insert
	 * @param p_index - index after which the entry is added
	 */
	
	public void insert(Plugin p_plugin, Block p_rail, String p_data, int p_index) {
		if (p_index < 0) {
			return;
		}
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		if ((dataList.size()-1) < p_index) {
			return;
		}
		dataList.add(p_index, p_data);
		this.set(p_plugin, p_rail, dataList);
	}
	
	public void sort(Plugin p_plugin, Block p_rail) {
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		Collections.sort(dataList);
		this.set(p_plugin, p_rail, dataList);
	}
	
	public void removeIndex(Plugin p_plugin, Block p_rail, int p_index) {
		if (p_index < 0) {
			return;
		}
		final List<String> dataList = this.getBlock(p_plugin, p_rail);
		if ((dataList.size()-1) < p_index) {
			return;
		}
		dataList.remove(p_index);
		this.set(p_plugin, p_rail, dataList);
	}
	
	public void removeBlock(Plugin p_plugin, Block p_rail) {
		if (data.contains(this.getPathString(p_plugin, p_rail.getLocation()))) {
			data.set(this.getPathString(p_plugin, p_rail.getLocation()), null);
			dataChanged = true;
		}
	}
	
	public void removeWorld(Plugin p_plugin, String p_world) {
		if (p_plugin == null || p_world == null) {
			return;
		}
		if (data.contains(p_plugin.getName() + "." + p_world)) {
			data.set(p_plugin.getName() + "." + p_world, null);
			dataChanged = true;
		}
	}
	
	public void removeAll(Plugin p_plugin) {
		if (p_plugin == null) {
			return;
		}
		if (data.contains(p_plugin.getName())) {
			data.set(p_plugin.getName(), null);
			dataChanged = true;
		}
	}
	
	public void clearPlugin(Plugin p_root, String p_plugin) {
		if (!plugin.equals(p_root)) {
			return;
		}
		if (data.contains(p_plugin)) {
			data.set(p_plugin, null);
			dataChanged = true;
		}
	}
	
	public void clearWorld(Plugin p_root, String p_world) {
		if (!plugin.equals(p_root)) {
			return;
		}
		boolean changed = false;
		for (String pluginName : data.getKeys(false)) {
			if (data.contains(pluginName + "." + p_world)) {
				data.set(pluginName + "." + p_world, null);
				changed = true;
			}
		}
		if (changed) {
			dataChanged = true;
		}
	}
	
	public void cleanData(Plugin p_root) {
		if (!plugin.equals(p_root)) {
			return;
		}
		boolean changed = false;
		for (String pluginName : data.getKeys(false)) {
			final Plugin checkPlugin = plugin.getServer().getPluginManager().getPlugin(pluginName);
			if (checkPlugin == null) {
				data.set(pluginName, null);
				changed = true;
				continue;
			}
			for (String world : data.getConfigurationSection(pluginName).getKeys(false)) {
				final World checkWorld = this.plugin.getServer().getWorld(world);
				if (checkWorld == null) {
					data.set(pluginName + "." + world, null);
					changed = true;
					continue;
				}
				for (String x : data.getConfigurationSection(pluginName).getConfigurationSection(world).getKeys(false)) {
					for (String z : data.getConfigurationSection(pluginName).getConfigurationSection(world).getConfigurationSection(x).getKeys(false)) {
						for (String y : data.getConfigurationSection(pluginName).getConfigurationSection(world).getConfigurationSection(x).getConfigurationSection(z).getKeys(false)) {
							final Block block = checkWorld.getBlockAt(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
							if (!BlockUtils.isRail(block)) {
								data.set(pluginName + "." + world + "." + x + "." + z + "." + y, null);
								changed = true;
							}
						}
					}
				}
			}
		}
		if (changed) {
			dataChanged = true;
		}
	}
	
	public synchronized void save(Plugin p_root) {
		if (plugin.equals(p_root)) {
			if (dataChanged) {
				if (plugin.getDataFolder().exists() || plugin.getDataFolder().mkdirs()) {
					try {
						if (broadcast) {
							plugin.getServer().broadcastMessage(lang.getString("save_start", new Object[] {plugin.getName()}));
						}
						data.save(dataFile);
						if (broadcast) {
							plugin.getServer().broadcastMessage(lang.getString("save_finish", new Object[] {plugin.getName()}));
						}
						dataChanged = false;
					} catch (Exception e) {
						LogUtils.severe(plugin, e);
						if (broadcast) {
							plugin.getServer().broadcastMessage(lang.getString("save_fail", new Object[] {plugin.getName()}));
						}
					}
				} else {
					LogUtils.severe(plugin, lang.getString("create_datafolder_failed"));
					plugin.onDisable();
				}
			}
		} else {
			LogUtils.warning(plugin, lang.getString("save_no_permission", new Object[] {p_root.getName()}));
		}
	}
}