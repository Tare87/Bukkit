package de.zeltclan.tare.bukkitutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.zeltclan.tare.bukkitutils.LogUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;

public class UpdateNotifier implements Listener {
	
	final private LanguageUtils lang;
	
	final private String name;
	final private String version;
	final private URL fileFeed;
	
	private boolean enabled;
	
	public UpdateNotifier(JavaPlugin p_plugin, String p_url, Boolean p_notifyOP, String p_language) {
		enabled = true;
		name = p_plugin.getDescription().getName();
		version = p_plugin.getDescription().getVersion();
		lang = new LanguageUtils("UpdateNotifier_localization", p_language);
		URL temp;
		try {
			temp = new URL(p_url);
		} catch (MalformedURLException e) {
			LogUtils.warning("[" + name + "]", e);
			temp = null;
		}
		fileFeed = temp;
		if (fileFeed != null) {
			HashMap<String,String> result = this.checkUpdate();
			if (result.containsKey("error")) {
				LogUtils.warning("[" + name + "] " + result.get("error"));
				return;
			}
			if (result.containsKey("link")) {
				LogUtils.info("[" + name + "] " + lang.getString("update_version", new Object[]{name, result.get("version")}));
				LogUtils.info("[" + name + "] " + lang.getString("update_changelog", new Object[]{result.get("link")}));
				LogUtils.info("[" + name + "] " + lang.getString("update_download", new Object[]{result.get("jarLink")}));
			}
			// Register OP_Notifier if 
			if (p_notifyOP) {
				p_plugin.getServer().getPluginManager().registerEvents(this, p_plugin);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent p_event) {
		if (enabled) {
			final Player player = p_event.getPlayer();
			if (player.isOp()) {
				HashMap<String,String> result = this.checkUpdate();
				if (result.containsKey("error")) {
					LogUtils.warning("[" + name + "] " + result.get("error"));
					return;
				}
				if (result.containsKey("link")) {
					MessageUtils.info(player, lang.getString("update_version", new Object[]{name, result.get("version")}));
					MessageUtils.info(player, lang.getString("update_changelog", new Object[]{result.get("link")}));
					MessageUtils.info(player, lang.getString("update_download", new Object[]{result.get("jarLink")}));
				}
			}
		}
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	private HashMap<String,String> checkUpdate() {
		HashMap<String,String> result = new HashMap<String,String> ();
		try {
			InputStream input = fileFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			
			Node lastFile = document.getElementsByTagName("item").item(0);
			NodeList children = lastFile.getChildNodes();
			
			result.put("version", children.item(1).getTextContent().replaceAll("[a-zA-Z ]", ""));
			if (!VersionUtils.isNewer(version, result.get("version"))) {
				return result;
			}
			
			result.put("link", children.item(3).getTextContent());
			
			input.close();
			
			input = (new URL(result.get("link")).openConnection().getInputStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("<li class=\"user-action user-action-download\">")) {
					result.put("jarLink", line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\"")));
					break;
				}
			}
			
			reader.close();
			input.close();
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		return result;
	}
}
