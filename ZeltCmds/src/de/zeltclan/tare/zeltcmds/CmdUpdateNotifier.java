package de.zeltclan.tare.zeltcmds;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.zeltclan.tare.bukkitutils.FileFeedReader;
import de.zeltclan.tare.bukkitutils.LogUtils;
import de.zeltclan.tare.bukkitutils.MessageUtils;

class CmdUpdateNotifier implements Listener {
	
	final private URL fileFeed;
	final private String version;
	
	CmdUpdateNotifier(String p_url, String p_version) {
		URL temp;
		try {
			temp = new URL(p_url);
		} catch (MalformedURLException e) {
			LogUtils.warning(ZeltCmds.getLanguage().getString("prefix"), e);
			temp = null;
		}
		fileFeed = temp;
		version = p_version;
	}
	
	URL getFileFeed() {
		return fileFeed;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent p_event) {
		final Player player = p_event.getPlayer();
		if (player.isOp()) {
			HashMap<String,String> result = FileFeedReader.checkUpdate(fileFeed, version);
			if (result.containsKey("error")) {
				LogUtils.warning(ZeltCmds.getLanguage().getString("prefix") + " " + result.get("error"));
				return;
			}
			if (result.containsKey("link")) {
				MessageUtils.info(player, ZeltCmds.getLanguage().getString("prefix") + " " + ZeltCmds.getLanguage().getString("update_version", new Object[]{result.get("version")}));
				MessageUtils.info(player, ZeltCmds.getLanguage().getString("update_changelog", new Object[]{result.get("link")}));
				MessageUtils.info(player, ZeltCmds.getLanguage().getString("update_download", new Object[]{result.get("jarLink")}));
			}
		}
	}
}
