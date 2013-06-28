package de.zeltclan.tare.zeltcmds.runnable;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class DelayCommandRunnable implements Runnable {
	
	final private Event event;
	
	final private String cmd;
	
	public DelayCommandRunnable(Event p_event, String p_cmd) {
		event = p_event;
		cmd = p_cmd;
	}

	@Override
	public void run() {
		if (event instanceof PlayerCommandPreprocessEvent) {
			((PlayerCommandPreprocessEvent)event).getPlayer().chat("/" + cmd);
		} else if (event instanceof ServerCommandEvent) {
			ServerCommandEvent oldEvent = (ServerCommandEvent)event;
			ServerCommandEvent sCommand = new ServerCommandEvent(oldEvent.getSender(), cmd);
			oldEvent.getSender().getServer().getPluginManager().callEvent(sCommand);
			oldEvent.getSender().getServer().dispatchCommand(sCommand.getSender(), sCommand.getCommand());
		}
	}

}
